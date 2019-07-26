package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.controller.posts.PostsController;
import com.bn1knb.newsblog.controller.users.UsersController;
import com.bn1knb.newsblog.model.Post;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class PostResource extends ResourceSupport {
    private final Post post;

    public PostResource(final Post post) {
        this.post = post;
        final Long postId = post.getId();
        add(linkTo(methodOn(PostsController.class).getPostById(postId)).withSelfRel());
        add(linkTo(methodOn(UsersController.class).getUserById(post.getUser().getId())).withRel("author"));
    }
}
