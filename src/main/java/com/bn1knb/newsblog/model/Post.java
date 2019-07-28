package com.bn1knb.newsblog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Post implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String headline;
    @Lob
    private Byte[] attachedFile;
    @Lob
    private String content;
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"posts", "comments", "state", "role", "password", "email", "createdAt", "firstName", "lastName"})
    private User user;
    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties(value = "post")
    private List<Comment> comments;
    private boolean isApproved;

    @PrePersist
    private void setCreationDate() {
        this.createdAt = new Date();
    }
}
