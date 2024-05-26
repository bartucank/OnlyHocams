package com.bartu.onlyhocams.entity;

import com.bartu.onlyhocams.dto.CommentDTO;
import com.bartu.onlyhocams.dto.LikeLogDTO;
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

    public CommentDTO toCommentDTO(){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setContent(content);
        commentDTO.setPostId(post.getId());
        commentDTO.setUser(user.toDTO());
        return commentDTO;
    }
    public LikeLogDTO toActionDTO(){
        LikeLogDTO likeLogDTO = new LikeLogDTO();
        likeLogDTO.setId(id);
        likeLogDTO.setType(type);
        likeLogDTO.setUser(user.toDTO());
        likeLogDTO.setPostId(post.getId());
        return likeLogDTO;
    }
}
