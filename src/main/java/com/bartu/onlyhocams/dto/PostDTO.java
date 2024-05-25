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
    private Long categoryName;
    private List<Long> documentIds;
}
