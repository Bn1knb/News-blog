package com.bn1knb.newsblog.controller.posts;

import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.service.post.PostService;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
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
    public ResponseEntity<PostResource> post(@Valid @RequestBody PostDto postDto,
                                             @RequestParam(value = "file", required = false) MultipartFile file,
                                             Principal principal) throws IOException {
        //byte[] newFile = file.getBytes();
        Post published = postService.save(postDto, principal.getName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(published.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new PostResource(published));
    }

    @GetMapping
    public ResponseEntity<Resources<PostResource>> getAllPosts(@PageableDefault(size = 5) Pageable pageable) {

        Link selfLink = linkTo(
                methodOn(PostsController.class)
                        .getAllPosts(pageable))
                .withSelfRel();

        Resources<PostResource> resources = new Resources<>(postService.findAllPerPage(pageable), selfLink);

        return ResponseEntity
                .ok(resources);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResource> getPostById(@PathVariable("postId") Long postId) {
        Post existing = postService.findPostById(postId);

        return ResponseEntity
                .ok(new PostResource(existing));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable("postId") Long postToDeleteId,
                                           Principal principal) throws AccessDeniedException {
        User currentUser = userService.findUserByUsername(principal.getName()); //TODO pod kapot
        postService.delete(postToDeleteId, postService.hasPermissionToDelete(postToDeleteId, currentUser));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResource> editPost(@Valid @RequestBody PostDto editedPost,
                                                 @PathVariable("postId") Long postId, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName());
        postService.update(postId, editedPost, currentUser);
        PostResource post = new PostResource(postService.findPostById(postId));

        return ResponseEntity
                .ok(post);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResource> patchPost(@PathVariable("postId") Long postId,
                                                  @RequestBody Map<String, String> fields) {
        postService.patch(fields, postId);
        PostResource post = new PostResource(postService.findPostById(postId));

        return ResponseEntity
                .ok(post);
    }
}
