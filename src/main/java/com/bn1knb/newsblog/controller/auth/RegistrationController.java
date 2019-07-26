package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {


    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResource> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.checkEmailAlreadyRegistered(userRegistrationDto.getEmail());
        userService.checkUsernameAlreadyRegistered(userRegistrationDto.getUsername());
        userService.register(userRegistrationDto);

        User user = userService
                .findUserByUsername(
                        userRegistrationDto
                                .getUsername()
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new UserResource(user));
    }
}
