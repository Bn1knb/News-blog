package com.bn1knb.newsblog.service.user;

import com.bn1knb.newsblog.dao.PostRepository;
import com.bn1knb.newsblog.dao.UserRepository;
import com.bn1knb.newsblog.exception.*;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.State;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void delete(Long id) {
        assert userRepository.findById(id).isPresent();//TODO check better way
        userRepository.deleteById(id);
    }

    @Override
    public void update(Long id, UserRegistrationDto updatedDto) {
        User userToUpdate = findUserById(id);
        User updatedUser = updatedDto.toUser(passwordEncoder);

        updatedUser.setRole(userToUpdate.getRole());
        updatedUser.setState(userToUpdate.getState());
        updatedUser.setCreatedAt(userToUpdate.getCreatedAt());
        updatedUser.setId(id);

        save(updatedUser);
    }

    @Override             //TODO validate map values
    public void patch(Map<String, String> fields, Long id) {
        User user = findUserById(id);

        fields.forEach((k, v) -> {

            Field field = ReflectionUtils.findField(User.class, k);
            assert field != null;
            ReflectionUtils.makeAccessible(field);

            if (field.getName().equals("email")) {
                checkEmailAlreadyRegistered(v);
            }

            if (field.getName().equals("username")) {
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
}
