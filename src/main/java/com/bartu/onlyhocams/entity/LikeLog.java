package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.entity.enums.Type;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "like_log")
public class LikeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_LIKELOG_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_LIKELOG_POST"))
    private Post post;
}
