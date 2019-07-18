package com.bn1knb.newsblog.controller.users;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RequestMapping("/users")
@RestController
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public Page<User> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {
        return userService.findAllPerPage(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") @Min(2) Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@Valid @RequestBody UserRegistrationDto editedUser, @PathVariable("id") @Min(2) Long id) {
        userService.update(id, editedUser);

        return ResponseEntity
                .noContent()
                .build();
    }
}
