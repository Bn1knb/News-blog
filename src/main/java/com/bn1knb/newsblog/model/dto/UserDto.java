package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    private Role role;
    private State state;

    public void toUserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole();
        this.state = user.getState();
    }

    public User toUser(PasswordEncoder encoder) {
        return  User.builder()
                .id(id)
                .username(username)
                .password(encoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(createdAt)
                .email(email)
                .role(role)
                .state(state)
                .build();
    }
}
