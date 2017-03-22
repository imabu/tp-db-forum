package park.services;

import park.mappers.ForumMapper;
import park.mappers.ThreadMapper;
import park.mappers.UserMapper;
import park.serverModel.ForumInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import park.serverModel.ThreadInfo;
import park.serverModel.UserInfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ForumService {
    private final JdbcTemplate template;

    public ForumService(JdbcTemplate template) {
        this.template = template;
    }

    public ForumInfo create(ForumInfo forum) {
        final String title = forum.getTitle();
        final String user = forum.getUser();
        final String slug = forum.getSlug();
        final int posts = forum.getPosts();
        final int threads = forum.getThreads();
        final UserInfo userInf = template.queryForObject("select * from users where lower(nickname)=lower(?)", new Object[]{user}, new UserMapper());
        final String nickname = userInf.getNickname();
        template.update("insert into forums(posts,slug,threads,title,\"user\") values(?,?,?,?,?)", posts, slug, threads, title, nickname);
        forum.setUser(nickname);
        return forum;
    }

    public ForumInfo details(String slug) {
        return template.queryForObject("select * from forums where lower(slug)=lower(?)", new Object[]{slug}, new ForumMapper());
    }

    public List<ThreadInfo> getThreads(String slug, Integer limit, Timestamp since, boolean desc) {
        ForumInfo forum = template.queryForObject("select * from forums where lower(slug)=lower(?)",
                new Object[]{slug},
                new ForumMapper());
        final List<Object> arg = new ArrayList<>();
        final StringBuilder sql = new StringBuilder("select * from threads where lower(forum)=lower(?) ");
        arg.add(slug);
        if (since != null) {
            if (desc) {
                sql.append("and created<=? ");
            }
            else sql.append("and created>=? ");
            arg.add(since);
        }
        sql.append("order by created ");
        if (desc) {
            sql.append("desc ");
        }
        if (limit != null) {
            sql.append("limit (?) ");
            arg.add(limit);
        }
        return template.query(sql.toString(),new ThreadMapper(), arg.toArray());
    }

    public List<UserInfo> getUsers(String slug, Integer limit, String since, boolean desc) {
        ForumInfo forum = template.queryForObject("select * from forums where lower(slug)=lower(?)",
                new Object[]{slug},
                new ForumMapper());

        final List<Object> arg = new ArrayList<>();
        final StringBuilder sql = new StringBuilder("select * from " +
                "(( select nickname, fullname, about, email " +
                "from users as u join threads as t on u.nickname=t.author " +
                "where lower(t.forum)=lower(?)");
        arg.add(slug);
        if (since != null) {
            if (desc) {
                sql.append(" and nickname<? ");
            }
            else sql.append(" and nickname>? ");
            arg.add(since);
        }

        sql.append(") union " +
                "( select nickname, fullname, about, email " +
                "from users as u join posts as t on u.nickname=t.author " +
                "where lower(t.forum)=lower(?)");
        arg.add(slug);
        if (since != null) {
            if (desc) {
                sql.append(" and nickname<? ");
            }
            else sql.append(" and nickname>? ");
            arg.add(since);
        }
        sql.append(")) as res");


        sql.append(" order by nickname ");
        if (desc) {
            sql.append("desc ");
        }

        if (limit != null) {
            sql.append("limit (?) ");
            arg.add(limit);
        }
        return template.query(sql.toString(),new UserMapper(), arg.toArray());
    }

}

