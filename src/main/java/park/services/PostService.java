package park.services;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import park.mappers.ForumMapper;
import park.mappers.PostMapper;
import park.mappers.ThreadMapper;
import park.mappers.UserMapper;
import park.serverModel.*;

import java.util.List;

@Service
public class PostService {
    JdbcTemplate template;

    public PostService(JdbcTemplate template) {
        this.template = template;
    }

    public PostFull details(int id, List<String> related) {
        final PostInfo post = template.queryForObject("select * from posts where id=?", new Object[]{id}, new PostMapper());
        PostFull postFull = new PostFull();
        postFull.setPost(post);
        if (related != null) {
            if (related.contains("forum")) {
                final ForumInfo forum = template.queryForObject("select * from forums where lower(slug)=lower(?)",
                        new Object[]{post.getForum()}, new ForumMapper());
                postFull.setForum(forum);
            }
            if (related.contains("thread")) {
                final ThreadInfo thread = template.queryForObject("select * from threads where id=?",
                        new Object[]{post.getThread()}, new ThreadMapper());
                postFull.setThread(thread);
            }
            if (related.contains("user")) {
                final UserInfo user = template.queryForObject("select * from users where lower(nickname)=lower(?)",
                        new Object[]{post.getAuthor()}, new UserMapper());
                postFull.setAuthor(user);
            }
        }
        return postFull;
    }

    public PostInfo update(int id, PostInfo postUpdate) {
        final PostInfo post = template.queryForObject("select * from posts where id=?",
                new Object[]{id}, new PostMapper());
        final String message = postUpdate.getMessage();
        if(message!=null&&!message.equals(post.getMessage())){
            template.update("update posts set message=?, isedited=true where id=?",
                    new Object[]{message,id});
            post.setMessage(message);
            post.setIsEdited(true);
        }
        return post;
    }


}
