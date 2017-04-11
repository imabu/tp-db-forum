package park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import park.serverModel.PostInfo;
import park.serverModel.PostPage;
import park.serverModel.ThreadInfo;
import park.serverModel.VoteInfo;
import park.services.ThreadService;

import java.util.List;

@RestController
public class ThreadController {
    private ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @PostMapping(path = "/forum/{slug}/create")
    public ResponseEntity<?> threadCreate(@PathVariable(value = "slug") String slug, @RequestBody ThreadInfo body) {
        try {
            final ThreadInfo thread = threadService.create(body, slug);
            return ResponseEntity.status(HttpStatus.CREATED).body(thread);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(threadService.details(body.getSlug()));
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path = "/thread/{slug_or_id}/details")
    public ResponseEntity<ThreadInfo> threadDetails(@PathVariable(value = "slug_or_id") String slug_or_id) {
        try {
            final ThreadInfo thread = threadService.details(slug_or_id);
            return ResponseEntity.status(HttpStatus.OK).body(thread);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "/thread/{slug_or_id}/details")
    public ResponseEntity<ThreadInfo> threadDetails(@PathVariable(value = "slug_or_id") String slug_or_id, @RequestBody ThreadInfo body) {
        try {
            final ThreadInfo thread = threadService.update(slug_or_id, body);
            return ResponseEntity.status(HttpStatus.OK).body(thread);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "thread/{slug_or_id}/create")
    public ResponseEntity<List<PostInfo>> createPosts(@PathVariable(value = "slug_or_id") String slug_or_id,
                                                      @RequestBody List<PostInfo> body) {

        try {
            final List<PostInfo> posts = threadService.createPosts(body, slug_or_id);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "thread/{slug_or_id}/vote")
    public ResponseEntity<ThreadInfo> vote(@PathVariable(value = "slug_or_id") String slug_or_id,
                                           @RequestBody VoteInfo body) {
        try {
            final ThreadInfo thread = threadService.vote(slug_or_id, body);
            return ResponseEntity.status(HttpStatus.OK).body(thread);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    static String postMarker;

    @GetMapping(path = "/thread/{slug_or_id}/posts")
    public ResponseEntity<PostPage> getPosts(@PathVariable(value = "slug_or_id") String slug_or_id,
                                      @RequestParam(value = "limit", required = false) Integer limit,
                                      @RequestParam(value = "marker", required = false) String marker,
                                      @RequestParam(value = "sort", required = false, defaultValue = "flat") String sort,
                                      @RequestParam(value = "desc", required = false) boolean desc) {

        try {
            final List<PostInfo> posts = threadService.getPosts(slug_or_id, limit, marker, sort, desc);
            if (posts.size() != 0) {
                final PostInfo lastPost = posts.get(posts.size() - 1);
                if (sort.equals("flat")) postMarker = lastPost.getId().toString();
                else if (sort.equals("tree")) postMarker = lastPost.getPath();
                else if (sort.equals("parent_tree")) postMarker = lastPost.getPp().toString();

            }
            return ResponseEntity.status(HttpStatus.OK).body(new PostPage(posts, postMarker));
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
