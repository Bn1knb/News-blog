package com.bn1knb.newsblog.service.comment;

import com.bn1knb.newsblog.dto.CommentDto;
import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.User;
import com.bn1knb.newsblog.model.hateoas.CommentResource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    Comment findCommentById(Long id);

    List<CommentResource> findCommentOfTheCurrentPost(Long postId, Pageable pageable);

    void delete(Long id, boolean hasPermission);

    Comment update(Long id, CommentDto editedComment, String username);

    Comment save(Comment comment);

    Comment save(CommentDto commentDto, MultipartFile attachedFile, Long postId, String authorName) throws IOException;

    boolean isAuthor(Long commentId, User author);

    boolean hasPermissionToDelete(Long commentId, String userName);
}
