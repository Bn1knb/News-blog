package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.controller.comments.CommentsController;
import com.bn1knb.newsblog.controller.posts.PostsController;
import com.bn1knb.newsblog.controller.users.UsersController;
import com.bn1knb.newsblog.model.Comment;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class CommentResource extends ResourceSupport {
    private final Comment comment;

    public CommentResource(final Comment comment) {
        this.comment = comment;
        Long commentId = comment.getId();
        add(linkTo(methodOn(CommentsController.class).getCommentById(commentId)).withSelfRel());
        add(linkTo(methodOn(UsersController.class).getUserById(comment.getUser().getId())).withRel("author"));
        add(linkTo(methodOn(PostsController.class).getPostById(comment.getPost().getId())).withRel("post"));
    }
}
