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
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Post> posts;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Comment> comments;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String password, String firstName, String lastName, String email, Date createdAt, State state, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.state = state;
        this.role = role;
    }

    @PrePersist
    private void setCreationDate() {
        this.createdAt = new Date();
    }
}
