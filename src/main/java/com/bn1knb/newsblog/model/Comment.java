package com.bn1knb.newsblog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    @Lob
    private byte[] attachedFiles;
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"comments", "approved", "content", "attachedFile", "user"})
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"posts", "comments", "state", "role", "password", "email", "createdAt", "id", "firstName", "lastName"})
    private User user;

    @PrePersist
    private void setCreationDate() {
        this.createdAt = new Date();
    }
}
