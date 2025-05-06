package com.leverx.learningmanagementsystem.exception;

public class NotEnoughCoinsException extends RuntimeException {
    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
