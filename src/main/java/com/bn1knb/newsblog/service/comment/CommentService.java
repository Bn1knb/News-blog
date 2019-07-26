package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommentService {
    Comment findCommentById(Long id);

    Page<Comment> findAllCommentsOfUser(User user, Pageable pageable);

    Page<Comment> findCommentOfTheCurrentPost(Post post, Pageable pageable);

    void comment(CommentDto newComment, User author, Post post);

    void delete(Long id);

    void update(Long id, CommentDto editedComment);

    void patch(Map<String, String> fields, Long id);

    void save(Comment comment);
}
