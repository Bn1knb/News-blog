package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.PostRepository;
import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.exception.*;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.model.hateoas.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private static final String USERNAME_FIELD = "username";
    private static final String EMAIL_FIELD = "email";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    @Override
    public User register(UserRegistrationDto userRegistrationDto) {
        checkEmailAlreadyRegistered(userRegistrationDto.getEmail());
        checkUsernameAlreadyRegistered(userRegistrationDto.getUsername());

        User user = userRegistrationDto.toUser(passwordEncoder);
        user.setRole(Role.ROLE_USER);
        user.setState(State.INACTIVE);

        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id, boolean hasPermission) throws AccessDeniedException {
        if (hasPermission) {
            User user = findUserById(id);
            user.setState(State.DELETED);
            save(user);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override
    public User update(Long id, UserRegistrationDto updatedDto, boolean hasPermission) throws AccessDeniedException {
        if (hasPermission) {
            User userToUpdate = findUserById(id);
            User updatedUser = updatedDto.toUser(passwordEncoder);

            updatedUser.setRole(userToUpdate.getRole());
            updatedUser.setState(userToUpdate.getState());
            updatedUser.setCreatedAt(userToUpdate.getCreatedAt());
            updatedUser.setId(id);

            return save(updatedUser);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override             //TODO validate map values
    public User patch(Map<String, String> fields, Long id) {
        User user = findUserById(id);

        fields.forEach((k, v) -> {

            Field field = ReflectionUtils.findField(User.class, k);
            assert field != null;
            ReflectionUtils.makeAccessible(field);

            if (field.getName().equals(EMAIL_FIELD)) {
                checkEmailAlreadyRegistered(v);
            }

            if (field.getName().equals(USERNAME_FIELD)) {
                checkUsernameAlreadyRegistered(v);
            }

            ReflectionUtils.setField(field, user, v);
        });

        return save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new); //TODO create custom controllerAdvice
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository
                .findOneByUsername(username)
                .orElseThrow(UsernameNotFoundException::new);
    }

    @Override
    public Post getPostOfUserWithId(Long userId, Long postId) {
        User user = findUserById(userId);

        return user
                .getPosts()
                .stream()
                .findAny()
                .filter(post -> post
                        .getId()
                        .equals(postId))
                .orElseThrow(PostNotFoundException::new);
    }


    @Override
    public List<PostResource> getAllPostOfUserByUserId(Long id, Pageable pageable) {
        User user = findUserById(id);

        return postRepository
                .findAllByUser(pageable, user)
                .stream()
                .map(PostResource::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResource> findAllPerPage(Pageable pageable) {
        if (userRepository.findAll(pageable).isEmpty()) {
            throw new PageNotFoundException();
        }

        return userRepository
                .findAll(pageable)
                .stream()
                .map(UserResource::new)
                .collect(Collectors.toList());
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
    public boolean hasPermissionToDelete(String username, Long userToDeleteId) {
        User currentUser = findUserByUsername(username);
        return currentUser.getId().equals(userToDeleteId) || currentUser.getRole().equals(Role.ROLE_ADMIN);
    }

    @Override
    public boolean hasPermissionToUpdate(String username, Long userToPatchId) {
        User currentUser = findUserByUsername(username);
        return currentUser.getId().equals(userToPatchId);
    }
}
