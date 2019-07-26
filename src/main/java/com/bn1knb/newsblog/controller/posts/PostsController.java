package com.bn1knb.newsblog.controller.posts;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.PostDto;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.service.post.PostService;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("/posts")
@RestController
public class PostsController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostsController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PostResource> post(@Valid @RequestBody PostDto postDto, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName());
        Post newPost = postDto.toPost(currentUser);
        postService.post(newPost);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(newPost.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new PostResource(newPost));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResource> getPostById(@PathVariable("postId") Long postId) {
        Post post = postService.findPostById(postId);
        return ResponseEntity
                .ok(new PostResource(post));
    }

    @GetMapping
    public ResponseEntity<Resources<PostResource>> getAllPosts(@PageableDefault(size = 5) Pageable pageable) {
        List<PostResource> posts = postService
                .findAllPerPage(pageable)
                .stream()
                .map(PostResource::new)
                .collect(Collectors.toList());
        Link selfLink = linkTo(
                methodOn(PostsController.class)
                        .getAllPosts(pageable))
                .withSelfRel();
        return ResponseEntity.ok(new Resources<>(posts, selfLink));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable("postId") Long postId) {
        postService.delete(postId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResource> editPost(@Valid @RequestBody PostDto editedPost, @PathVariable("postId") Long postId) {
        postService.update(postId, editedPost);
        PostResource post = new PostResource(postService.findPostById(postId));
        return ResponseEntity
                .ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResource> patchPost(@PathVariable("postId") Long postId, @RequestBody Map<String, String> fields) {
        postService.patch(fields, postId);
        PostResource post = new PostResource(postService.findPostById(postId));
        return ResponseEntity
                .ok(post);
    }


}
