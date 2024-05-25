package com.bartu.onlyhocams.api.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String username;
    private String password;
    private String email;
}
