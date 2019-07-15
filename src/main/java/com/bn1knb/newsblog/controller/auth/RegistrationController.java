package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.model.dto.UserDto;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {

        userService.save(userDto);
        userDto = new UserDto(userService.findUserById(userDto.getId()));
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
