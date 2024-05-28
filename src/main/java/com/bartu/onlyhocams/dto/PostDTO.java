package com.bartu.onlyhocams.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;

    private String content;
    private LocalDateTime publishDate;
    private String formattedDate;
    private UserDTO user;
    private Long categoryId;
    private String categoryName;
    private List<DocumentDTO> documents;
    private List<CommentDTO> comments;
    private List<LikeLogDTO> actions;
    private Boolean isLiked;
    private Boolean isDisliked;
}
