package com.bartu.onlyhocams.dto;

import com.bartu.onlyhocams.entity.enums.Type;
import lombok.Data;

@Data
public class LikeLogDTO {
    private Long id;
    private UserDTO user;
    private Long postId;
    private Type type;

}
