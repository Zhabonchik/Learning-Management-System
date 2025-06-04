package com.leverx.learningmanagementsystem.core.security.model;

public enum AuthRoles {
    USER,
    MANAGER;

    public String asRole(){
        return "ROLE_" + this.name();
    }
}
