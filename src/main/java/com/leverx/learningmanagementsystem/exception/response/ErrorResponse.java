package com.leverx.learningmanagementsystem.exception.response;

public record ErrorResponse(int statusCode, String message) {
    public ErrorResponse(String message) {
        this(0, message); // Можно установить дефолтный statusCode
    }
}