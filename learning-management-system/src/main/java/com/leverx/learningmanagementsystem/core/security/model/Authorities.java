package com.leverx.learningmanagementsystem.core.security.model;

public enum Authorities {
    INFO("Info"),
    BASIC("Basic");

    private final String name;

    private Authorities(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
