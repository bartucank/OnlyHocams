package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.entity.enums.Role;
import com.bartu.onlyhocams.entity.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Note {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String title;

    private LocalDateTime publishDate;

    private String content;

    @Column(name = "\"like\"")
    private Long like;

    @Column(name = "\"dislike\"")
    private Long dislike;


    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_NOTE_USER"))
    private User user;



}
