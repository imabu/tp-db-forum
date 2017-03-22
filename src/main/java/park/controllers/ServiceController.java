package park.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import park.serverModel.Status;

@RestController
@RequestMapping("/service")
public class ServiceController {

    JdbcTemplate template;
    public ServiceController(JdbcTemplate template){this.template=template;}

    @PostMapping(path="/clear")
    public ResponseEntity<Object> clear(){
        template.update("truncate users cascade");
        return ResponseEntity.ok(null);
    }
    @GetMapping("/status")
    public ResponseEntity<Object> status() {
        final int forum = template.queryForObject("SELECT COUNT(*) FROM forums", Integer.class);
        final int thread = template.queryForObject("SELECT COUNT(*) FROM threads", Integer.class);
        final int post = template.queryForObject("SELECT COUNT(*) FROM posts", Integer.class);
        final int user = template.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        return ResponseEntity.ok(new Status(forum, thread, post, user));
    }

}
