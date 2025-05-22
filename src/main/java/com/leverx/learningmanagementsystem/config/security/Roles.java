package com.leverx.learningmanagementsystem.config.security;

public enum Roles {
    USER,
    MANAGER;

    public String asRole(){
        return "ROLE_" + this.name();
    }
}
