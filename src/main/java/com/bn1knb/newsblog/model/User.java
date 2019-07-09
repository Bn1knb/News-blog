package com.bn1knb.newsblog.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    @OneToMany(mappedBy = "user")
    List<Post> posts;
    @OneToMany(mappedBy = "user")
    List<Comment> comments;
    private State state;
    private Role role;

    @PrePersist
    void setCreationDate() {
        this.createdAt = new Date();
    }
}
