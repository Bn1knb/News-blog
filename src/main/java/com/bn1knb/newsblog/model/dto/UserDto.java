package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private Date createdAt;
    private Role role;
    private State state;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole();
        this.state = user.getState();
    }

    public User toUser(PasswordEncoder encoder) {
        return  User.builder()
                .id(id)
                .username(username)
                .password(password)
                .firstName(firstname)
                .lastName(lastname)
                .createdAt(createdAt)
                .email(email)
                .role(role)
                .state(state)
                .build();
    }
}
