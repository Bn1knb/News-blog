package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final Pageable FIRST_PAGE_WITH_FIVE_ElEMENTS = PageRequest.of(0, 5);

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> userById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @GetMapping //TODO add params and other methods
    List<User> users() {
        return userService.findAllPerPage(FIRST_PAGE_WITH_FIVE_ElEMENTS)
                .stream()
                .collect(Collectors.toList());
    }
}
