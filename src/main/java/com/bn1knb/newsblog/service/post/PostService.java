package com.bn1knb.newsblog.service.post;

import com.bn1knb.newsblog.dto.PostDto;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.PostResource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface PostService {
    Post findPostById(Long id);

    List<PostResource> findAllPerPage(Pageable pageable);

    void delete(Long id, boolean hasPermission) throws AccessDeniedException;

    Post update(Long id, PostDto editedPostDto, String authorUsername);

    Post patch(Map<String, String> fields, Long id);



    Post save(PostDto postDto, String authorName, MultipartFile attachedFile) throws IOException;

    Post save(Post post);

    boolean isAuthor(Long postId, User currentUser);

    boolean hasPermissionToDelete(Long postId, String username);
}