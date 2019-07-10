package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserDto userDto) {
        userDto.setRole(Role.USER);
        userDto.setState(State.INACTIVE);
        userRepository.save(userDto.toUser());
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findOneById(id)
                .orElseThrow(() -> new RuntimeException("User with id:" + id + " not found"));
    }
}
