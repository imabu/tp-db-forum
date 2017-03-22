package park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import park.serverModel.ForumInfo;
import park.serverModel.ThreadInfo;
import park.serverModel.UserInfo;
import park.services.ForumService;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController

public class ForumController {

    private final ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping(path = "/forum/create")
    public ResponseEntity<ForumInfo> createForum(@RequestBody ForumInfo body) {
        try {
            final ForumInfo forum = forumService.create(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(forum);
        } catch (DuplicateKeyException ex) {
            final String slug = body.getSlug();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(forumService.details(slug));
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path = "/forum/{slug}/details")
    public ResponseEntity<ForumInfo> forumGetOne(@PathVariable(value = "slug") String slug) {
        try {
            final ForumInfo forum = forumService.details(slug);
            return ResponseEntity.status(HttpStatus.OK).body(forum);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping(path = "forum/{slug}/threads")
    public ResponseEntity<List<ThreadInfo>> forumGetThreads(@PathVariable(value = "slug") String slug,
                                                            @RequestParam(value = "limit", required = false) Integer limit,
                                                            @RequestParam(value = "since", required = false) String since,
                                                            @RequestParam(value = "desc", required = false) boolean desc) {

        try {
            Timestamp sinceT=null;
            if(since!=null) {
                sinceT = Timestamp.valueOf((LocalDateTime.parse(since, DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
                sinceT = Timestamp.from(sinceT.toInstant().plusSeconds(10800));
            }

            final List<ThreadInfo> threads = forumService.getThreads(slug, limit, sinceT, desc);
            return ResponseEntity.status(HttpStatus.OK).body(threads);
        } catch (DataAccessException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
    @GetMapping(path = "forum/{slug}/users")
    public ResponseEntity<List<UserInfo>> forumGetUsers(@PathVariable(value = "slug") String slug,
                                                          @RequestParam(value = "limit", required = false) Integer limit,
                                                          @RequestParam(value = "since", required = false) String since,
                                                          @RequestParam(value = "desc", required = false) boolean desc){

        try {
            final List<UserInfo> users = forumService.getUsers(slug, limit, since, desc);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
