package com.bn1knb.newsblog.model.hateoas;

import com.bn1knb.newsblog.controller.users.UsersController;
import com.bn1knb.newsblog.model.User;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class UserResource extends ResourceSupport {
    private final User user;

    public UserResource(final User user) {
        this.user = user;
        final Long userId = user.getId();
        add(linkTo(UsersController.class).slash(userId).withSelfRel());
        add(linkTo(methodOn(UsersController.class).getPostsFromUser(userId, Pageable.unpaged())).withRel("posts"));
    }
}
