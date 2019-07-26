package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Validated
@RequestMapping("/users")
@RestController
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);
        UserResource resource = new UserResource(user);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<Resources<UserResource>> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {
        List<UserResource> users = userService
                .findAllPerPage(pageable)
                .stream()
                .map(UserResource::new)
                .collect(Collectors.toList());
        Link selfLink = linkTo(
                methodOn(UsersController.class)
                        .getAllUsers(pageable))
                .withSelfRel();
        return ResponseEntity
                .ok(new Resources<>(users, selfLink));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") //TODO chane to verify auth and privileges
    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable("userId") @Min(2) Long userId) {
        userService.delete(userId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResource> editUser(@Valid @RequestBody UserRegistrationDto editedUser, @PathVariable("userId") Long userId) {
        userService.update(userId, editedUser);
        UserResource user = new UserResource(userService.findUserById(userId));
        return ResponseEntity
                .ok(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResource> patchUser(@PathVariable("userId") Long userId, @RequestBody Map<String, String> fields) {
        userService.patch(fields, userId);
        UserResource user = new UserResource(userService.findUserById(userId));
        return ResponseEntity
                .ok(user);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Resources<PostResource>> getPostsFromUser(@PathVariable("userId") Long userId, @PageableDefault(size = 5) Pageable pageable) {
        List<PostResource> userPosts = userService
                .getAllPostOfUserByUserId(userId, pageable)
                .stream()
                .map(PostResource::new)
                .collect(Collectors.toList());

        Link link = linkTo(methodOn(UsersController.class)
                .getPostsFromUser(userId, pageable)).withSelfRel();
        return ResponseEntity
                .ok(new Resources<>(userPosts, link));
    }

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostResource> getPostFromUser(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId) {
        User user = userService.findUserById(userId);
        Post post = userService.getPostOfUserWithId(user, postId);
        PostResource resource = new PostResource(post);
        return ResponseEntity
                .ok(resource);
    }
}
