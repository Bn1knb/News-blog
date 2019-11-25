package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Map;

public interface UserService {
    User register(UserRegistrationDto userRegistrationDto);

    User save(User user);

    void delete(Long id, boolean hasPermission) throws AccessDeniedException;

    User update(Long id, UserRegistrationDto updatedDto, boolean hasPermission);

    User patch(Map<String, String> fields, Long id);

    User findUserById(Long id);

    User findUserByUsername(String username);

    Post getPostOfUserWithId(Long userId, Long postId);

    List<UserResource> findAllPerPage(Pageable pageable);

    List<PostResource> getAllPostOfUserByUserId(Long id, Pageable pageable);

    void checkEmailAlreadyRegistered(String email);

    void checkUsernameAlreadyRegistered(String username);

    boolean hasPermissionToDelete(String username, Long userToDeleteId);

    boolean hasPermissionToUpdate(String username, Long userToPatchId);
}
