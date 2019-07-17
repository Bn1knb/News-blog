package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final Pageable FIRST_PAGE_WITH_FIVE_ELEMENTS = PageRequest.of(0, 5);

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    //TODO check type page<>
    //TODO add params and other methods
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllPerPage(FIRST_PAGE_WITH_FIVE_ELEMENTS)
                .stream()
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@RequestBody User editedUser, @PathVariable("id") Long id) {
        userService.checkUserId(id);
        userService.save(editedUser);

        return ResponseEntity
                .noContent()
                .build();
    }
}
