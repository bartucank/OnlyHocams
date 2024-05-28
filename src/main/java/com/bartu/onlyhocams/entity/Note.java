package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.DocumentDTO;
import com.bartu.onlyhocams.dto.NoteDTO;
import com.bartu.onlyhocams.entity.enums.Role;
import com.bartu.onlyhocams.entity.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
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


    public NoteDTO toDTO(){
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(this.id);
        noteDTO.setTitle(this.title);
        noteDTO.setUser(this.user.toDTO());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        noteDTO.setFormattedDate(this.publishDate.format(formatter));
        noteDTO.setReviews(new ArrayList<>());
        noteDTO.setDocument(new DocumentDTO());
        noteDTO.setStatus(getStatus());
        return noteDTO;
    }


}
