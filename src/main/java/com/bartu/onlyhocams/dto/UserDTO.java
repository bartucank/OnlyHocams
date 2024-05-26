package com.bartu.onlyhocams.dto;

import com.bartu.onlyhocams.entity.enums.Role;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private Role role;
    private String roleStr;

    private BigDecimal credit;
    private String email;
    private String username;
}
