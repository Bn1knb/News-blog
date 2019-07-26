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
    @GeneratedValue
    private Long id;
    @Lob
    private String content;
    @Lob
    private Byte[] attachedFiles;
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"comments","isApproved","content","attachedFile"})
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"posts","comments","state","role","password","email","createdAt"})
    private User user;

    @PrePersist
    private void setCreationDate() {
        this.createdAt = new Date();
    }
}
