package com.leverx.learningmanagementsystem.exception;

public abstract class EntityValidationException extends RuntimeException {

    public EntityValidationException (String message) {
        super(message);
    }

    public static class EntityNotFoundException extends EntityValidationException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class IncorrectResultSizeException extends EntityValidationException {
        public IncorrectResultSizeException(String message) {
            super(message);
        }
    }

    public static class InvalidCourseDatesException extends EntityValidationException {
        public InvalidCourseDatesException(String message) {
            super(message);
        }
    }

}
