package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.exception.EmailAlreadyRegisteredException;
import com.bn1knb.newsblog.exception.UserNotFoundException;
import com.bn1knb.newsblog.exception.UsernameAlreadyRegisteredException;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void register(UserRegistrationDto userRegistrationDto) {
        User user = userRegistrationDto.toUser(passwordEncoder);
        user.setRole(Role.ROLE_USER);
        user.setState(State.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(this.findUserById(id));
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

    @Override
    public Page<User> findAllPerPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void checkEmailAlreadyRegistered(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    @Override
    public void checkUsernameAlreadyRegistered(String username) {
        if (userRepository.findOneByUsername(username).isPresent()) {
            throw new UsernameAlreadyRegisteredException();
        }
    }

    @Override
    public void checkUserId(Long id) {
        findUserById(id);
    }
}
