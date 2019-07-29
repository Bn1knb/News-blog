package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.controller.comments.CommentsController;
import com.bn1knb.newsblog.dao.CommentRepository;
import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.exception.CommentNotFoundException;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import com.bn1knb.newsblog.service.post.PostService;
import com.bn1knb.newsblog.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
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
    public List<CommentResource> findCommentOfTheCurrentPost(Long postId, Pageable pageable) {
        Post currentPost = postService.findPostById(postId);
        return commentRepository
                .findAllByPost(currentPost, pageable)
                .stream()
                .map(CommentResource::new)
                .collect(Collectors.toList());
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
    public Comment update(Long id, CommentDto editedCommentDto, User user) {
        if (isAuthor(id, user)) {
            Comment commentToUpdate = findCommentById(id);
            Comment editedComment = editedCommentDto.toComment(commentToUpdate.getUser(), commentToUpdate.getPost());

            editedComment.setPost(commentToUpdate.getPost());
            editedComment.setUser(commentToUpdate.getUser());
            editedComment.setCreatedAt(commentToUpdate.getCreatedAt());
            editedComment.setId(id);

            return save(editedComment);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment save(CommentDto commentDto, Long postId, String authorName) {
        Post currentPost = postService.findPostById(postId);
        User author = userService.findUserByUsername(authorName);
        Comment newComment = commentDto.toComment(author, currentPost);
        return commentRepository.save(newComment);
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
