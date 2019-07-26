package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserRegistrationDto implements Serializable {

    @NotBlank
    @Size(min = 3, max = 10)
    private String username;
    @NotBlank
    @Size(min = 6, max = 25)
    private String password;
    @NotBlank()
    @Size(min = 1)
    private String firstName;
    @NotBlank
    @Size(min = 1)
    private String lastName;
    @NotBlank
    @Email
    private String email;

    public void toUserDto(final User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }

    public User toUser(PasswordEncoder encoder) {
        return User.builder()
                .username(username)
                .password(encoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }
}
