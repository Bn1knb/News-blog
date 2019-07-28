package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment findCommentById(Long id);

    Page<Comment> findAllCommentsOfUser(User user, Pageable pageable);

    Page<Comment> findCommentOfTheCurrentPost(Post post, Pageable pageable);

    void comment(Comment comment);

    void delete(Long id, boolean hasPermission);

    void update(Long id, CommentDto editedComment, User author);

    void save(Comment comment);

    boolean isAuthor(Long commentId, User author);

    boolean hasPermissionToDelete(Long commentId, User currentUser);
}
