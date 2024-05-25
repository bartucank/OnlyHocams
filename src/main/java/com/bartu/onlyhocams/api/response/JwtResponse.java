package com.bartu.onlyhocams.api.response;

import com.bartu.onlyhocams.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String jwt;
    private Role role;
}
