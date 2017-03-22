package park.mappers;


import org.springframework.jdbc.core.RowMapper;
import park.serverModel.PostInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<PostInfo> {
    @Override
    public PostInfo mapRow(ResultSet rs, int rowNum) throws SQLException{
        final PostInfo post=new PostInfo();
        post.setId(rs.getInt("id"));
        post.setForum(rs.getString("forum"));
        post.setAuthor(rs.getString("author"));
        post.setMessage(rs.getString("message"));
        post.setParent(rs.getInt("parent"));
        post.setThread(rs.getInt("thread"));
        post.setCreated(rs.getTimestamp("created"));
        post.setIsEdited(rs.getBoolean("isEdited"));
        return post;
    }
}
