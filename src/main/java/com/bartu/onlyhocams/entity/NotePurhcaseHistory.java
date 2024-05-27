package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.DocumentDTO;
import com.bartu.onlyhocams.dto.NoteDTO;
import com.bartu.onlyhocams.entity.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Data
@Entity
public class NotePurhcaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_NOTEPURHCASE_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "note_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_NOTEPURHCASE_NOTE"))
    private Note note;



}
