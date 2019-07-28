package com.bn1knb.newsblog.service.post;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.Map;

public interface PostService {
    Post findPostById(Long id);

    Page<Post> findAllPerPage(Pageable pageable);

    void post(Post post);

    void delete(Long id, boolean hasPermission) throws AccessDeniedException;

    void update(Long id, PostDto editedPostDto, User author);

    void patch(Map<String, String> fields, Long id);

    void save(Post post);

    boolean isAuthor(Long postId, User currentUser);

    boolean hasPermissionToDelete(Long postId, User currentUser);
}