package park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import park.serverModel.UserInfo;
import park.services.UserService;


@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/{nickname}/create")
    public ResponseEntity<?> createUser(@RequestBody UserInfo body, @PathVariable(value = "nickname") String nickname) {
        final String email = body.getEmail();
        try {
            UserInfo user = userService.create(body, nickname);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userService.getUser(nickname, email));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping(path = "/{nickname}/profile")
    public ResponseEntity<UserInfo> getUser(@PathVariable(value = "nickname") String nickname) {
        try {
            UserInfo user = userService.getUser(nickname);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(path = "/{nickname}/profile")
    public ResponseEntity<UserInfo> updateUser(@RequestBody UserInfo body, @PathVariable(value = "nickname") String nickname) {
        try {
            final UserInfo updUser = userService.updateUser(body, nickname);
            return ResponseEntity.status(HttpStatus.OK).body(updUser);
        } catch (DuplicateKeyException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
