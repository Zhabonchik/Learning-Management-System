package com.leverx.learningmanagementsystem.core.security.model;

public enum AuthRoles {
    USER,
    MANAGER,
    LMS_ADMIN;

    public String asRole() {
        return "ROLE_" + this.name();
    }
}
