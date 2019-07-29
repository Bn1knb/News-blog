package com.bn1knb.newsblog.service.post;

import com.bn1knb.newsblog.dao.PostRepository;
import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.exception.PageNotFoundException;
import com.bn1knb.newsblog.exception.PostNotFoundException;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements com.bn1knb.newsblog.service.post.PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public List<PostResource> findAllPerPage(Pageable pageable) {
        if (postRepository.findAll(pageable).isEmpty()) {
            throw new PageNotFoundException();
        }

        return postRepository.findAll(pageable)
                .stream()
                .map(PostResource::new)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id, boolean hasPermission) {
        if (hasPermission) {
            Post postToDelete = findPostById(id);
            postRepository.delete(postToDelete);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override
    public Post update(Long postToUpdateId, PostDto editedPostDto, User author) {
        if (isAuthor(postToUpdateId, author)) {
            Post postToUpdate = findPostById(postToUpdateId);
            Post editedPost = editedPostDto.toPost(postToUpdate.getUser());

            editedPost.setUser(postToUpdate.getUser());
            editedPost.setCreatedAt(postToUpdate.getCreatedAt());
            editedPost.setApproved(postToUpdate.isApproved());
            editedPost.setId(postToUpdateId);

            return save(editedPost);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override             //TODO validate map values
    public Post patch(Map<String, String> fields, Long id) {

        Post editedPost = findPostById(id);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Post.class, k);
            assert field != null;
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, editedPost, v);
        });

        return save(editedPost);
    }

    @Override
    public Post save(PostDto postDto, String authorName) {
        User author = userService.findUserByUsername(authorName);
        Post newPost = postDto.toPost(author);
        return postRepository.save(newPost);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public boolean isAuthor(Long postId, User currentUser) {
        Post postToModify = findPostById(postId);
        return postToModify
                .getUser()
                .getId()
                .equals(currentUser.getId());
    }

    @Override
    public boolean hasPermissionToDelete(Long postId, User currentUser) {
        Post postToModify = findPostById(postId);
        return postToModify
                .getUser()
                .getId()
                .equals(currentUser.getId()) ||
                currentUser.getRole().equals(Role.ROLE_ADMIN) ||
                currentUser.getRole().equals(Role.ROLE_MODERATOR);
    }
}
