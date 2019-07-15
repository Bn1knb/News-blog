package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public class UsersController {

    @Autowired
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) //TODO use optional Dto instead and check isPresent or throw 404
    ResponseEntity<User> userById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

}
