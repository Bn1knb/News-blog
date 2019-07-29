package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Comment findCommentById(Long id);

    Page<Comment> findAllCommentsOfUser(User user, Pageable pageable);

    List<CommentResource> findCommentOfTheCurrentPost(Long postId, Pageable pageable);

    void delete(Long id, boolean hasPermission);

    Comment update(Long id, CommentDto editedComment, User author);

    Comment save(Comment comment);

    Comment save(CommentDto commentDto, Long postId, String authorName);

    boolean isAuthor(Long commentId, User author);

    boolean hasPermissionToDelete(Long commentId, User currentUser);
}
