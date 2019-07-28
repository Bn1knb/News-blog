package com.bn1knb.newsblog.controller.auth;

import com.bn1knb.newsblog.dto.AuthenticationLoginRequestDto;
import com.bn1knb.newsblog.dto.AuthenticationLoginResponseDto;
import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import com.bn1knb.newsblog.security.jwt.JwtTokenProvider;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResource> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.checkEmailAlreadyRegistered(userRegistrationDto.getEmail());
        userService.checkUsernameAlreadyRegistered(userRegistrationDto.getUsername());
        userService.register(userRegistrationDto);

        User user = userService
                .findUserByUsername(
                        userRegistrationDto
                                .getUsername()
                );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new UserResource(user));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationLoginRequestDto loginDto) {
        try {
            String username = loginDto.getUsername();
            String password = loginDto.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            userService.findUserByUsername(username);

            String token = jwtTokenProvider.createToken(username);
            AuthenticationLoginResponseDto responseDto = new AuthenticationLoginResponseDto(username, token);

            return ResponseEntity.ok(responseDto);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

    }
}
