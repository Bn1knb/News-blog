package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.dao.CommentRepository;
import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.exception.CommentNotFoundException;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Page<Comment> findAllCommentsOfUser(User user, Pageable pageable) {
        return commentRepository.findAllByUser(user, pageable);
    }

    @Override
    public Page<Comment> findCommentOfTheCurrentPost(Post post, Pageable pageable) {
        return commentRepository.findAllByPost(post, pageable);
    }

    @Override
    public void comment(Comment newComment) {
        save(newComment);
    }

    @Override
    public void delete(Long commentId, boolean hasPermission) {
        if (hasPermission) {
            Comment commentToDelete = findCommentById(commentId);
            commentRepository.delete(commentToDelete);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override
    public void update(Long id, CommentDto editedCommentDto, User user) {
        if (isAuthor(id, user)) {
            Comment commentToUpdate = findCommentById(id);
            Comment editedComment = editedCommentDto.toComment(commentToUpdate.getUser(), commentToUpdate.getPost());

            editedComment.setPost(commentToUpdate.getPost());
            editedComment.setUser(commentToUpdate.getUser());
            editedComment.setCreatedAt(commentToUpdate.getCreatedAt());
            editedComment.setId(id);

            save(editedComment);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public boolean isAuthor(Long commentId, User author) {
        Comment currentComment = findCommentById(commentId);
        return currentComment.getUser().getId().equals(author.getId());
    }

    @Override
    public boolean hasPermissionToDelete(Long commentId, User currentUser) {
        Comment currentComment = findCommentById(commentId);
        return currentComment
                .getUser()
                .getId()
                .equals(currentUser.getId()) ||
                currentUser
                        .getRole()
                        .equals(Role.ROLE_MODERATOR) ||
                currentUser
                        .getRole()
                        .equals(Role.ROLE_ADMIN);
    }
}
