package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.exception.EmailExistsException;
import com.bn1knb.newsblog.exception.UserNotFoundException;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        checkEmailExist(userDto.getEmail());
        userDto.setRole(Role.ROLE_USER);
        userDto.setState(State.INACTIVE);
        userRepository.save(userDto.toUser(passwordEncoder));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id:" + id + " not found")); //TODO create custom controllerAdvice
    }

    @Override
    public User findUserByName(String username) {
        return userRepository
                .findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name:" + username + " not found"));
    }

    private void checkEmailExist(String email) {
        if (userRepository.findByEmail(email).isPresent())  {
            throw new EmailExistsException();
        }
    }
}
