package park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import park.serverModel.PostFull;
import park.serverModel.PostInfo;
import park.services.PostService;

import java.util.List;

@RestController
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path="post/{id}/details")
    public ResponseEntity<PostFull> details(@PathVariable(value="id") int id,
                                            @RequestParam(value = "related", required = false) List<String> related){

        try {
            final PostFull post= postService.details(id, related);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping(path="post/{id}/details")
    public ResponseEntity<PostInfo> details(@PathVariable(value="id") int id, @RequestBody PostInfo body){

        try {
            final PostInfo post = postService.update(id, body);
            return ResponseEntity.status(HttpStatus.OK).body(post);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
