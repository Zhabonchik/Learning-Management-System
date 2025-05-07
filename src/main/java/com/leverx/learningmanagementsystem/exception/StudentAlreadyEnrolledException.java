package com.leverx.learningmanagementsystem.exception;

public class StudentAlreadyEnrolledException extends RuntimeException {
    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }
}
