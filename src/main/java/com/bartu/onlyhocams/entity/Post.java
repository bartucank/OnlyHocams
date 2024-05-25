package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.entity.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    private String content;
    private LocalDateTime publishDate;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_POST_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_POST_CATEGORY"))
    private Category category;


}
