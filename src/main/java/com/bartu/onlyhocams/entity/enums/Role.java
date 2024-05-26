package com.bartu.onlyhocams.entity.enums;

public enum Role {
    ADMIN("Admin"),
    USER("User");

    String description;
    Role(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
