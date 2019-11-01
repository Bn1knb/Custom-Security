package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import com.bn1knb.newsblog.service.user.UserService;
import com.bn1knb.newsblog.utills.TokenValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;

@Validated
@RequestMapping("/users")
@RestController
public class UsersController {
    private static final String AUTH_TOKEN_COOKIE_NAME = "auth_token";
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    //TODO засунуть в юзер ресур подобное чтобы были не посты а пост ресурсы с линками на себя
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resources<UserResource>> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {

        Link selfLink = new Link("http://localhost:80/proxy/users")
                .withSelfRel();

        return ResponseEntity
                .ok(new Resources<>(userService.findAllPerPage(pageable), selfLink));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);

        return ResponseEntity
                .ok(new UserResource(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable("userId") @Min(2) Long userToDeleteId,
                                           @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) {

        String username = TokenValueUtils.decrypt(authToken);
        userService.delete(userToDeleteId, userService.hasPermissionToDelete(username, userToDeleteId));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResource> editUser(@Valid @RequestBody UserRegistrationDto editedUser,
                                                 @PathVariable("userId") Long userId,
                                                 @CookieValue(AUTH_TOKEN_COOKIE_NAME) String authToken) {

        String username = TokenValueUtils.decrypt(authToken);
        User user = userService.update(userId, editedUser, userService.hasPermissionToUpdate(username, userId));

        return ResponseEntity
                .ok(new UserResource(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResource> patchUser(@PathVariable("userId") Long userId,
                                                  @RequestBody Map<String, String> fields) {
        User user = userService.patch(fields, userId);

        return ResponseEntity
                .ok(new UserResource(user));
    }

    @GetMapping(path = "/{userId}/posts", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Resources<PostResource>> getPostsFromUser(@PathVariable("userId") Long userId,
                                                                    @PageableDefault(size = 5) Pageable pageable) {
        Link link = new Link("http://localhost/proxy/users/" + userId + "/posts")
                .withSelfRel();

        return ResponseEntity
                .ok(new Resources<>(userService.getAllPostOfUserByUserId(userId, pageable), link));
    }

    @GetMapping(path = "/{userId}/posts/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<PostResource> getPostFromUser(@PathVariable("userId") Long userId,
                                                        @PathVariable("postId") Long postId) {
        Post post = userService.getPostOfUserWithId(userId, postId);
        return ResponseEntity
                .ok(new PostResource(post));
    }
}
