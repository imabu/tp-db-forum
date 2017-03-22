package park.services;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import park.mappers.UserMapper;
import park.serverModel.UserInfo;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final JdbcTemplate template;


    public UserService(JdbcTemplate template) {
        this.template = template;
    }

    public UserInfo create(UserInfo user, String nickname) {
        final String about = user.getAbout();
        final String fullname = user.getFullname();
        final String email = user.getEmail();
        user.setNickname(nickname);
        template.update("insert into users(nickname,fullname,email,about) values(?,?,?,?)", nickname, fullname, email, about);
        return user;

    }

    public List<UserInfo> getUser(String nickname, String email) {
        return template.query("select * from users where lower(nickname)=lower(?) or lower(email)=lower(?)", new Object[]{nickname, email}, new UserMapper());
    }

    public UserInfo getUser(String nickname) {
        return template.queryForObject("select * from users where lower(nickname)=lower(?)", new Object[]{nickname}, new UserMapper());
    }

    public UserInfo updateUser(UserInfo user, String nickname) {
        final String about = user.getAbout();
        final String fullname = user.getFullname();
        final String email = user.getEmail();
        user.setNickname(nickname);
        final StringBuilder sql = new StringBuilder("update users set ");
        final List<Object> arg = new ArrayList<>();
        if (fullname != null) {
            sql.append("fullname=?,");
            arg.add(fullname);
        }
        if (email != null) {
            sql.append("email=?,");
            arg.add(email);
        }
        if (about != null) {
            sql.append("about=?,");
            arg.add(about);
        }
        if (!arg.isEmpty()) {
            sql.deleteCharAt(sql.length()-1);
            sql.append(" where lower(nickname)=lower(?)");
            arg.add(nickname);
            template.update(sql.toString(), arg.toArray());
        }
        return template.queryForObject("select * from users where lower(nickname)=lower(?)", new Object[]{nickname}, new UserMapper());

    }

}
