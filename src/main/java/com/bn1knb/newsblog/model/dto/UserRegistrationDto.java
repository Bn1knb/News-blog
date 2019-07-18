package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto implements Serializable {

    @NotNull
    @Size(min = 3, max = 10)
    private String username;
    @NotNull
    @Size(min = 6, max = 25)
    private String password;
    @NotBlank(message = "Please provide a name")
    @Size(min = 1)
    private String firstName;
    @NotNull
    @Size(min = 1)
    private String lastName;
    @NotNull
    @Email
    private String email;

    public void toUserDto(User user) {
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
