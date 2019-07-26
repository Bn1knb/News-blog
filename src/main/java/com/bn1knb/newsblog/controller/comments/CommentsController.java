package com.bn1knb.newsblog.controller.comments;

import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.dto.CommentDto;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CommentsController {
    private final CommentService commentService;

    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResource> comment(@Valid @RequestBody CommentDto commentDto, Principal principal) {
        /*return ResponseEntity
                .created(*//*location*//*)
                .body(new PostResource(newComment));*/
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResource> getCommentById(@PathVariable("commentId") Long commentId) {
        //Comment existing = commentService.findCommentById(commentId);
        return ResponseEntity
                .ok().build();
    }

    @GetMapping
    public ResponseEntity<Resources<CommentResource>> getAllComments(@PageableDefault(size = 5) Pageable pageable) {
        /*List<PostResource> posts = postService
                .findAllPerPage(pageable)
                .stream()
                .map(PostResource::new)
                .collect(Collectors.toList());
        Link selfLink = linkTo(
                methodOn(PostsController.class)
                        .getAllPosts(pageable))
                .withSelfRel();
*/        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId) {
        //postService.delete(commentId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResource> editComment(@Valid @RequestBody CommentDto commentDto, @PathVariable("commentId") Long commentId) {
        /*commentService.update(commentId, editedComment);
        CommentResource comment = new CommentResource(commentService.findCommentById(commentId));*/
        return ResponseEntity
                .ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResource> patchComment(@PathVariable("commentId") Long commentId, @RequestBody Map<String, String> fields) {
        /*commentService.patch(fields, commentId);
        CommentResource comment = new CommentResource(CommentService.findCommentById(CommentId));*/
        return ResponseEntity
                .ok().build();
    }
}
