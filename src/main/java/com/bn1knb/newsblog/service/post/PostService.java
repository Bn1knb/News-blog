package com.bn1knb.newsblog.service.post;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostService {
    Post findPostById(Long id);

    Page<Post> findAllPerPage(Pageable pageable);

    void post(Post post);

    void delete(Long id);

    void update(Long id, PostDto editedPostDto);

    void patch(Map<String, String> fields, Long id);

    void save(Post post);
}