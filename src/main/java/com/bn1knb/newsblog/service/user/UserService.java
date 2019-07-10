package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;

public interface UserService {
    void save(UserDto userDto);

    User findUserById(Long id);
}
