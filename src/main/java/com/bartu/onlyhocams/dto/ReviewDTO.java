package com.bartu.onlyhocams.dto;

import com.bartu.onlyhocams.entity.enums.Type;
import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String content;
    private UserDTO user;
    private Long noteId;
    private Type type;
}
