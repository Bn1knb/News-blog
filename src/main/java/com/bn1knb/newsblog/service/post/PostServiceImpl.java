package com.bn1knb.newsblog.service.post;

import com.bn1knb.newsblog.dao.PostRepository;
import com.bn1knb.newsblog.exception.PageNotFoundException;
import com.bn1knb.newsblog.exception.PostNotFoundException;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public Page<Post> findAllPerPage(Pageable pageable) {
        if (postRepository.findAll(pageable).isEmpty()) {
            throw new PageNotFoundException();
        }

        return postRepository.findAll(pageable);
    }

    @Override
    public void post(Post post) {
        save(post);
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
    public void update(Long postToUpdateId, PostDto editedPostDto, User author) {
        if (isAuthor(postToUpdateId, author)) {
            Post postToUpdate = findPostById(postToUpdateId);
            Post editedPost = editedPostDto.toPost(postToUpdate.getUser());

            editedPost.setUser(postToUpdate.getUser());
            editedPost.setCreatedAt(postToUpdate.getCreatedAt());
            editedPost.setApproved(postToUpdate.isApproved());
            editedPost.setId(postToUpdateId);

            save(editedPost);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override             //TODO validate map values
    public void patch(Map<String, String> fields, Long id) {

        Post post = findPostById(id);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Post.class, k);
            assert field != null;
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, post, v);
        });

        save(post);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
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
