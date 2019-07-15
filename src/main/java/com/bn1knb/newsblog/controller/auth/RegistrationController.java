package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> register(@RequestBody UserDto userDto) {

        userService.save(userDto);
        User user = userDto.toUser(passwordEncoder);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
