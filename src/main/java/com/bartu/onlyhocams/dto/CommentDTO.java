package com.bartu.onlyhocams.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private Long postId;
}
