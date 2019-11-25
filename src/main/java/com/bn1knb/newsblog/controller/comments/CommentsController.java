package com.bn1knb.newsblog.controller.comments;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentService commentService;


    @Autowired
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResource> getCommentById(@PathVariable("commentId") Long commentId) {
        Comment existing = commentService.findCommentById(commentId);
        return ResponseEntity
                .ok(new CommentResource(existing));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") Long commentId, Principal principal) {
        commentService.delete(commentId, commentService.hasPermissionToDelete(commentId, principal.getName()));

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResource> editComment(@PathVariable("commentId") Long commentId,
                                                       @Valid @RequestBody CommentDto editedComment, Principal principal) {
        Comment comment = commentService.update(commentId, editedComment, principal.getName());

        return ResponseEntity
                .ok(new CommentResource(comment));
    }
}
