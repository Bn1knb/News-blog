package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void save(UserDto userDto);

    User findUserById(Long id);

    User findUserByName(String username);

    Page<User> findAllPerPage(Pageable pageable);
}
