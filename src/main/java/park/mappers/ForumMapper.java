package park.mappers;


import org.springframework.jdbc.core.RowMapper;
import park.serverModel.ForumInfo;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumMapper implements RowMapper<ForumInfo> {
    @Override
    public ForumInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        ForumInfo forum = new ForumInfo();
        forum.setTitle(rs.getString("title"));
        forum.setSlug(rs.getString("slug"));
        forum.setUser(rs.getString("user"));
        forum.setThreads(rs.getInt("threads"));
        forum.setPosts(rs.getInt("posts"));
        return forum;
    }
}
