package com.bn1knb.newsblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private byte[] attachedFile;
    @Lob
    private String content;
    private Date createdAt;
    private boolean isApproved;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"posts", "comments", "state", "role", "password", "email", "createdAt", "firstName", "lastName"})
    private User user;
    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments;


    @PrePersist
    private void setCreationDate() {
        this.createdAt = new Date();
    }
}
