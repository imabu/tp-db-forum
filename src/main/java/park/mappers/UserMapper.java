package park.mappers;


import org.springframework.jdbc.core.RowMapper;
import park.serverModel.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        final UserInfo user = new UserInfo();
        user.setNickname(rs.getString("nickname"));
        user.setEmail(rs.getString("email"));
        user.setAbout(rs.getString("about"));
        user.setFullname(rs.getString("fullname"));
        return user;
    }
}
