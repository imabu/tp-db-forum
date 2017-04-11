package park.services;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import park.mappers.*;
import park.serverModel.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadService {
    JdbcTemplate template;

    public ThreadService(JdbcTemplate template) {
        this.template = template;
    }

    public ThreadInfo create(ThreadInfo thread, String slug) {
        final String title = thread.getTitle();
        final String author = thread.getAuthor();
        final String message = thread.getMessage();
        Timestamp created = thread.getCreated();
        final String slugThread = thread.getSlug();
        if (created == null) {
            final String time = LocalDateTime.now().toString();
            created = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        }

        final UserInfo userInf = template.queryForObject("select * from users where lower(nickname)=lower(?)",
                new Object[]{author},
                new UserMapper());
        final String nickname = userInf.getNickname();

        final ForumInfo forumInf = template.queryForObject("select * from forums where lower(slug)=lower(?) ",
                new Object[]{slug},
                new ForumMapper());
        final String forumSlug = forumInf.getSlug();

        template.update("update forums set threads=threads+1 where lower(slug)=lower(?)", new Object[]{slug});
        final Integer id = template.queryForObject(
                "insert into threads (title,author,forum,message,slug,created) values(?,?,?,?,?,?) returning id",
                new Object[]{title, nickname, forumSlug, message, slugThread, created},
                Integer.class);
        return details(id.toString());
    }

    public ThreadInfo update(String slug_or_id, ThreadInfo threadUpdate) {
        ThreadInfo thread = details(slug_or_id);
        StringBuilder sql = new StringBuilder("update threads set ");
        final String title = threadUpdate.getTitle();
        final String message = threadUpdate.getMessage();
        List<Object> arg = new ArrayList<>();
        if (title != null) {
            sql.append(" title=?,");
            arg.add(title);
            thread.setTitle(title);
        }
        if (message != null) {
            sql.append(" message=?,");
            arg.add(message);
            thread.setMessage(message);
        }
        if (!arg.isEmpty()) {
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" where id=?");
            arg.add(thread.getId());
            template.update(sql.toString(), arg.toArray());
        }
        return thread;
    }

    public ThreadInfo details(String slug_or_id) {
        try {
            final Integer id = Integer.valueOf(slug_or_id);
            return template.queryForObject("select * from threads where id=?", new Object[]{id}, new ThreadMapper());
        } catch (NumberFormatException ex) {
            return template.queryForObject("select * from threads where lower(slug)=lower(?)", new Object[]{slug_or_id}, new ThreadMapper());
        }
    }

    @Transactional
    public List<PostInfo> createPosts(List<PostInfo> posts, String slug_or_id) {
        int k = 0;
        final ThreadInfo thread = details(slug_or_id);
        final String time = LocalDateTime.now().toString();
        final Timestamp created = Timestamp.valueOf(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));

        for (PostInfo post : posts) {
            final int parent = post.getParent();
            if (parent != 0) {
                final List<Integer> postsInDB = template.query("select id from posts where thread=(?)", new Object[]{thread.getId()}, new IdMapper());
                if (!postsInDB.contains(parent)) {
                    throw new DuplicateKeyException(null);
                }
            }
            final String author = post.getAuthor();
            final UserInfo userInf = template.queryForObject("select * from users where lower(nickname)=lower(?)",
                    new Object[]{author},
                    new UserMapper());
            final String nickname = userInf.getNickname();
            post.setAuthor(nickname);
            final String message = post.getMessage();
            String forum = post.getForum();
            if (forum == null) {
                forum = thread.getForum();
                post.setForum(forum);
            }
            int threadPost = post.getThread();
            if (threadPost == 0) {
                threadPost = thread.getId();
                post.setThread(threadPost);
            }
            post.setCreated(created);

            final Integer id = template.queryForObject("insert into posts(parent, author,message,forum,thread, created) values(?,?,?,?,?,?) returning id",
                    Integer.class,
                    new Object[]{parent, nickname, message, forum, threadPost, created});
            k++;
            post.setId(id);
        }
        template.update("update forums set posts=posts+? where lower(slug)=lower(?)", new Object[]{k, thread.getForum()});
        return posts;
    }

    public ThreadInfo vote(String slug_or_id, VoteInfo vote) {
        final ThreadInfo thread = details(slug_or_id);
        final int threadId = thread.getId();

        final UserInfo userInf = template.queryForObject("select * from users where lower(nickname)=lower(?)",
                new Object[]{vote.getNickname()},
                new UserMapper());
        final String nickname = userInf.getNickname();
        try {
            final VoteInfo voteUser = template.queryForObject("select * from uservotes where thread=? " +
                            "and lower(nickname)=lower(?)",
                    new Object[]{threadId, nickname}, new VoteMapper());
            final int chVote = -voteUser.getVoice() + vote.getVoice();
            template.update("update uservotes set voice=? where lower(nickname)=lower(?) and thread=? ",
                    new Object[]{vote.getVoice(), nickname, threadId});
            template.update("update threads set votes=votes+? where id=?",
                    new Object[]{chVote, threadId});
            thread.setVotes(thread.getVotes() + chVote);
        } catch (EmptyResultDataAccessException ex) {
            template.update("insert into uservotes values(?,?,?) ", new Object[]{nickname, threadId, vote.getVoice()});
            template.update("update threads set votes=votes+? where id=?", new Object[]{vote.getVoice(), threadId});
            thread.setVotes(thread.getVotes() + vote.getVoice());
        }
        return thread;
    }

    public List<PostInfo> getPosts(String slug_or_id, Integer limit, String marker, String sort, boolean desc) {
        final ThreadInfo thread = details(slug_or_id);
        if (sort.equals("flat")) {
            StringBuilder sql = new StringBuilder("select * from posts where thread=? ");
            if (marker != null) {
                if (desc) sql.append(" and id < ").append(marker);
                else sql.append(" and id > ").append(marker);
            }
            sql.append(" order by created  ");
            if (desc) {
                sql.append(" desc ");
            }
            sql.append(", id ");
            if (desc) {
                sql.append(" desc ");
            }
            if (limit != null) {
                sql.append(" limit ").append(limit);
            }
            return template.query(sql.toString(), new PostMapper(), new Object[]{thread.getId()});
        } else if (sort.equals("tree")) {
            StringBuilder sql = new StringBuilder("WITH RECURSIVE parent_limit(id, parent, author, message, thread, forum, created, isedited, path) as( " +
                    "    SELECT " +
                    "      id, parent, author, message, thread, forum, created, isedited, array[id] as path " +
                    "    FROM posts " +
                    "    WHERE parent = 0 AND thread = ? ");


            sql.append("), " +
                    "    recursetree(" +
                    "    id, parent, author, message, thread, forum, created, isedited, path) AS ( " +
                    "  SELECT    * " +
                    "  FROM parent_limit  " +
                    "  UNION " +
                    "  SELECT " +
                    "    p.id, p.parent, p.author, p.message, p.thread, p.forum, " +
                    "    p.created, p.isedited, rt.path||p.id as path " +
                    "  FROM recursetree rt " +
                    "    JOIN posts p ON rt.id = p.parent  " +
                    "  WHERE rt.thread = ? " +
                    ")" +
                    "SELECT * " +
                    "FROM recursetree ");
            if (marker != null) {
                if (desc) {
                    sql.append("where path < \'").append(marker).append("\'");
                } else {
                    sql.append("where path > \'").append(marker).append("\'");
                }
            }
            sql.append("  order by path ");
            if (desc) {
                sql.append(" desc ");
            }
            if (limit != null) {
                sql.append("    limit ").append(limit);
            }
            return template.query(sql.toString(), new PostMapper(), new Object[]{thread.getId(), thread.getId()});

        } else if (sort.equals("parent_tree")) {

            StringBuilder sql = new StringBuilder("WITH RECURSIVE parent_limit(id, parent, author, message, thread, forum, created, isedited, path) as( " +
                    "    SELECT " +
                    "      id, parent, author, message, thread, forum, created, isedited, array[id] as path " +
                    "    FROM posts " +
                    "    WHERE parent = 0 AND thread = ? ");
            if (marker != null) {
                if (desc) {
                    sql.append(" and  id < ").append(marker);
                } else {
                    sql.append(" and id > ").append(marker);
                }
            }
            if (desc) {
                sql.append(" order by created desc, id desc ");
            } else {
                sql.append(" order by created , id ");
            }
            if (limit != null) {
                sql.append("    limit ").append(limit);
            }

            sql.append("), " +
                    "    recursetree(" +
                    "    id, parent, author, message, thread, forum, created, isedited, path) AS ( " +
                    "  SELECT    * " +
                    "  FROM parent_limit  " +
                    "  UNION " +
                    "  SELECT " +
                    "    p.id, p.parent, p.author, p.message, p.thread, p.forum, " +
                    "    p.created, p.isedited, rt.path||p.id as path " +
                    "  FROM recursetree rt " +
                    "    JOIN posts p ON rt.id = p.parent  " +
                    "  WHERE rt.thread = ? " +
                    ")" +
                    "SELECT * , path[1] as pp " +
                    " FROM recursetree ");


            sql.append(" order by path ");
            if (desc) sql.append(" desc ");

            return template.query(sql.toString(), new PostSortMapper(), new Object[]{thread.getId(), thread.getId()});
        }

        return null;
    }
}
