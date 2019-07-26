package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.dao.CommentRepository;
import com.bn1knb.newsblog.exception.CommentNotFoundException;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

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
    public void comment(CommentDto newCommentDto, User author, Post post) {
        Comment newComment = newCommentDto.toComment(author, post);
        save(newComment);
    }

    @Override
    public void delete(Long id) {
        assert commentRepository.findById(id).isPresent();
        commentRepository.deleteById(id);
    }

    @Override
    public void update(Long id, CommentDto editedCommentDto) {
        Comment commentToUpdate = findCommentById(id);
        Comment editedComment = editedCommentDto.toComment(commentToUpdate.getUser(), commentToUpdate.getPost());

        editedComment.setCreatedAt(commentToUpdate.getCreatedAt());
        editedComment.setId(id);

        save(editedComment);
    }

    @Override
    public void patch(Map<String, String> fields, Long id) {
        Comment comment = findCommentById(id);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Comment.class, k);
            assert field != null;
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, comment, v);
        });

        save(comment);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
