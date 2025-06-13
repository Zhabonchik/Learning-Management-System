package com.leverx.learningmanagementsystem.core.db.dto;

public record DataSourceDto (
        String url,
        String username,
        String password,
        String driverClassName
) {
}
