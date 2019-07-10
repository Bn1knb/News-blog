package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.model.dto.UserDto;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json", "text/html"})
    ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {

        userService.save(userDto);
        userDto = new UserDto(userService.findUserById(userDto.getId()));
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
