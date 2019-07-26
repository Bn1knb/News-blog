package com.bn1knb.newsblog.model.dto;

import com.bn1knb.newsblog.model.Post;
import com.bn1knb.newsblog.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
@AllArgsConstructor
public class PostDto implements Serializable {

    @NotBlank
    private String headline;
    @Size(min = 10)
    private String content;
    private Byte[] attachedFile;

    public void toPostDto(final Post post) {
        this.headline = post.getHeadline();
        this.content = post.getContent();
        this.attachedFile = post.getAttachedFile();
    }

    public Post toPost(User author) {
        return Post
                .builder()
                .headline(headline)
                .content(content)
                .user(author)
                .attachedFile(attachedFile)
                .build();
    }
}
