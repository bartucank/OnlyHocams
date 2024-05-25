package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.entity.enums.Role;
import com.bartu.onlyhocams.entity.enums.Type;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "review")
public class Review {
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
            foreignKey = @ForeignKey(name = "FK_REVIEW_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "note_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_REVIEW_NOTE"))
    private Note note;
}
