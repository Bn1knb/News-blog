package com.bn1knb.newsblog.dao;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findAllByUser(Pageable pageable, User user);
}
