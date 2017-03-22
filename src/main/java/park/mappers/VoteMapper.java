package park.mappers;


import org.springframework.jdbc.core.RowMapper;
import park.serverModel.VoteInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<VoteInfo> {
    @Override
    public VoteInfo mapRow(ResultSet rs, int rowNum) throws SQLException{
        return new VoteInfo(rs.getString("nickname"), rs.getInt("voice"));
    }
}
