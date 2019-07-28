package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.PostRepository;
import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.dto.UserRegistrationDto;
import com.bn1knb.newsblog.exception.*;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

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
    public void update(Long id, UserRegistrationDto updatedDto, User currentUser) throws AccessDeniedException {
        if (hasPermissionToUpdate(currentUser, id)) {
            User userToUpdate = findUserById(id);
            User updatedUser = updatedDto.toUser(passwordEncoder);

            updatedUser.setRole(userToUpdate.getRole());
            updatedUser.setState(userToUpdate.getState());
            updatedUser.setCreatedAt(userToUpdate.getCreatedAt());
            updatedUser.setId(id);

            save(updatedUser);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override             //TODO validate map values
    public void patch(Map<String, String> fields, Long id) {
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

        save(user);
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
    public Post getPostOfUserWithId(User user, Long postId) {
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
    public Page<Post> getAllPostOfUserByUserId(Long id, Pageable pageable) {
        User user = findUserById(id);

        return postRepository.findAllByUser(pageable, user);
    }

    @Override
    public Page<User> findAllPerPage(Pageable pageable) {
        if (userRepository.findAll(pageable).isEmpty()) {
            throw new PageNotFoundException();
        }

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
    public boolean hasPermissionToDelete(User currentUser, Long userToDeleteId) {
        return currentUser.getId().equals(userToDeleteId) || currentUser.getRole().equals(Role.ROLE_ADMIN);
    }

    @Override
    public boolean hasPermissionToUpdate(User currentUser, Long userToPatchId) {
        return currentUser.getId().equals(userToPatchId);
    }
}
