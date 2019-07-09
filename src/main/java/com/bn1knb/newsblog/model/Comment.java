package com.bn1knb.newsblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private String content;
    @Lob
    private String attachedFiles;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
