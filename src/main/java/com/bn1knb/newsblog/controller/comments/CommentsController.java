package com.bn1knb.newsblog.controller.comments;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.service.comment.CommentService;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentsController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentsController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<CommentResource> comment(@Valid @RequestBody CommentDto commentDto, @PathVariable("postId") Long postId, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName());
        Post currentPost = postService.findPostById(postId);
        Comment newComment = commentDto.toComment(currentUser, currentPost);
        commentService.comment(newComment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(newComment.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new CommentResource(newComment));
    }

    @GetMapping("/comments")
    public ResponseEntity<Resources<CommentResource>> getAllCommentsOfPost(@PathVariable("postId") Long postId, @PageableDefault(size = 5) Pageable pageable) {
        Post currentPost = postService.findPostById(postId);
        List<CommentResource> comments = commentService
                .findCommentOfTheCurrentPost(currentPost, pageable)
                .stream()
                .map(CommentResource::new)
                .collect(Collectors.toList());
        Link selfLink = linkTo(
                methodOn(CommentsController.class)
                        .getAllCommentsOfPost(postId, pageable))
                .withSelfRel();

        return ResponseEntity
                .ok(new Resources<>(comments, selfLink));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResource> getCommentById(@PathVariable("commentId") Long commentId) {
        Comment existing = commentService.findCommentById(commentId);
        return ResponseEntity
                .ok(new CommentResource(existing));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName());
        commentService.delete(commentId, commentService.hasPermissionToDelete(commentId, currentUser));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResource> editComment(@PathVariable("commentId") Long commentId, @Valid @RequestBody CommentDto editedComment, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName());
        commentService.update(commentId, editedComment, currentUser);
        CommentResource comment = new CommentResource(commentService.findCommentById(commentId));

        return ResponseEntity
                .ok(comment);
    }
}
