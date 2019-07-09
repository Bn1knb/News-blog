package com.bn1knb.newsblog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Post {

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
    private User user;
}
