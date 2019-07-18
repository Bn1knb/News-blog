package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void register(UserRegistrationDto userRegistrationDto);

    void save(User user);

    void delete(Long id);

    void update(Long userToUpdateId, UserRegistrationDto updatedDto);

    User findUserById(Long id);

    User findUserByName(String username);

    Page<User> findAllPerPage(Pageable pageable);

    void checkEmailAlreadyRegistered(String email);

    void checkUsernameAlreadyRegistered(String username);

    void checkUserId(Long id);
}
