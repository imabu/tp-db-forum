package park.mappers;

import org.springframework.jdbc.core.RowMapper;
import park.serverModel.ThreadInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ThreadMapper implements RowMapper<ThreadInfo>{
    @Override
    public ThreadInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        final ThreadInfo thread = new ThreadInfo();
        thread.setTitle(rs.getString("title"));
        thread.setAuthor(rs.getString("author"));
        thread.setMessage(rs.getString("message"));
        thread.setId(rs.getInt("id"));
        thread.setForum(rs.getString("forum"));
        thread.setVotes(rs.getInt("votes"));
        thread.setSlug(rs.getString("slug"));
        thread.setCreated(rs.getTimestamp("created"));
        return thread;
    }
}
