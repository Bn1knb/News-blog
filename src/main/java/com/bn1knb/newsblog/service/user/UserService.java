package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    void register(UserRegistrationDto userRegistrationDto);

    void save(User user);

    void delete(Long id);

    void update(Long id, UserRegistrationDto updatedDto);

    void patch(Map<String, String> fields, Long id);

    User findUserById(Long id);

    User findUserByUsername(String username);

    Post getPostOfUserWithId(User user, Long postId);

    Page<User> findAllPerPage(Pageable pageable);

    Page<Post> getAllPostOfUserByUserId(Long id, Pageable pageable);

    void checkEmailAlreadyRegistered(String email);

    void checkUsernameAlreadyRegistered(String username);
}
