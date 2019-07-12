package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(UserDto userDto) {
        userDto.setRole(Role.ROLE_USER);
        userDto.setState(State.INACTIVE);
        userRepository.save(userDto.toUser(passwordEncoder));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findOneById(id)
                .orElseThrow(() -> new RuntimeException("User with id:" + id + " not found")); //TODO add custom exception with 404 status and create custom controllerAdvice
    }

    @Override
    public User findUserByName(String username) {
        return userRepository
                .findOneByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with name:" + username + " not found"));
    }
}
