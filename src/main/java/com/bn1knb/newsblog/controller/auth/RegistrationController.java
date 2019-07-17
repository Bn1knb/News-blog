package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

    @Qualifier(value = "UserServiceImpl")
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    ResponseEntity<User> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.checkEmailAlreadyRegistered(userRegistrationDto.getEmail());
        userService.checkUsernameAlreadyRegistered(userRegistrationDto.getUsername());
        userService.register(userRegistrationDto);

        User user = userService
                .findUserByName(
                        userRegistrationDto
                                .getUsername()
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }
}
