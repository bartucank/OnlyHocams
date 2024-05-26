package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.PostDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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


    public PostDTO toDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(this.id);
        postDTO.setContent(this.content);
        postDTO.setPublishDate(this.publishDate);
        postDTO.setUser(this.user.toDTO());
        postDTO.setCategoryId(this.category.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        postDTO.setFormattedDate(this.publishDate.format(formatter));
        postDTO.setCategoryName(this.category.getName());
        postDTO.setComments(new ArrayList<>());
        postDTO.setActions(new ArrayList<>());
        postDTO.setDocuments(new ArrayList<>());
        return postDTO;
    }
}
