package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
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
import java.security.Principal;
import java.util.Map;

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

    //TODO засунуть в юзер ресур подобное чтобы были не посты а пост ресурсы с линками на себя
    @GetMapping
    public ResponseEntity<Resources<UserResource>> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {

        Link selfLink = linkTo(
                methodOn(UsersController.class)
                        .getAllUsers(pageable))
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
    public ResponseEntity<User> deleteUser(@PathVariable("userId") @Min(2) Long userToDeleteId, Principal principal) {
        userService.delete(userToDeleteId, userService.hasPermissionToDelete(principal.getName(), userToDeleteId));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResource> editUser(@Valid @RequestBody UserRegistrationDto editedUser,
                                                 @PathVariable("userId") Long userId, Principal principal) {
        User user = userService.update(userId, editedUser, userService.hasPermissionToUpdate(principal.getName(), userId));

        return ResponseEntity
                .ok(new UserResource(user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResource> patchUser(@PathVariable("userId") Long userId,
                                                  @RequestBody Map<String, String> fields) {
        User user = userService.patch(fields, userId);

        return ResponseEntity
                .ok(new UserResource(user));
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Resources<PostResource>> getPostsFromUser(@PathVariable("userId") Long userId,
                                                                    @PageableDefault(size = 5) Pageable pageable) {
        Link link = linkTo(methodOn(UsersController.class)
                .getPostsFromUser(userId, pageable)).withSelfRel();

        return ResponseEntity
                .ok(new Resources<>(userService.getAllPostOfUserByUserId(userId, pageable), link));
    }

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostResource> getPostFromUser(@PathVariable("userId") Long userId,
                                                        @PathVariable("postId") Long postId) {
        Post post = userService.getPostOfUserWithId(userId, postId);
        return ResponseEntity
                .ok(new PostResource(post));
    }
}
