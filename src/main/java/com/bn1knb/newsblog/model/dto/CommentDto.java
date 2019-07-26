package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.Comment;
import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommentDto implements Serializable {

    @NotBlank
    private String content;
    private Byte[] attachedFiles;

    public Comment toComment(User author, Post post) {
        return Comment
                .builder()
                .content(content)
                .attachedFiles(attachedFiles)
                .user(author)
                .post(post)
                .build();
    }

}
