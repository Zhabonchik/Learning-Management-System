package com.leverx.learningmanagementsystem.core.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leverx.learningmanagementsystem.core.exception.model.EntityNotFoundException;
import com.leverx.learningmanagementsystem.core.exception.model.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.core.exception.model.InvalidCourseDatesException;
import com.leverx.learningmanagementsystem.core.exception.response.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.NonNull;

import static com.leverx.learningmanagementsystem.core.utils.DataFormatUtils.DATE_FORMAT;
import static com.leverx.learningmanagementsystem.core.utils.DataFormatUtils.DATE_TIME_FORMAT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return createNotFoundResponseWithMessage(ex.getMessage());
    }

    @ExceptionHandler(IncorrectResultSizeException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleMismatchException(IncorrectResultSizeException ex) {
        return createConflictResponseWithMessage(ex.getMessage());
    }

    @ExceptionHandler(InvalidCourseDatesException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleInvalidCourseDatesException(InvalidCourseDatesException ex) {
        return createConflictResponseWithMessage(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation error");
        return createBadRequestResponseWithMessage(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.util.UUID.class) {
            createBadRequestResponseWithMessage("Invalid UUID format. UUID must be in standard 36-character representation.");
        } else if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.time.LocalDateTime.class) {
            return createBadRequestResponseWithMessage("Invalid DateTime format. Expected format: " + DATE_TIME_FORMAT);
        } else if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.time.LocalDate.class) {
            createBadRequestResponseWithMessage("Invalid Date format. Expected format: " + DATE_FORMAT);
        }
        return createBadRequestResponseWithMessage("Malformed JSON request.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return (ex.getRequiredType() == java.util.UUID.class)
                ? createBadRequestResponseWithMessage("Invalid UUID format: %s".formatted(ex.getValue()))
                : createBadRequestResponseWithMessage("Invalid parameter type.");
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return createInternalErrorResponseWithMessage(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return createInternalErrorResponseWithMessage(ex.getMessage());
    }

    private static @NonNull ErrorResponse createBadRequestResponseWithMessage(@NonNull String message) {
        return new ErrorResponse(BAD_REQUEST.value(), message);
    }

    private static @NonNull ErrorResponse createNotFoundResponseWithMessage(@NonNull String message) {
        return new ErrorResponse(NOT_FOUND.value(), message);
    }

    private static @NonNull ErrorResponse createConflictResponseWithMessage(@NonNull String message) {
        return new ErrorResponse(CONFLICT.value(), message);
    }

    private static @NonNull ErrorResponse createInternalErrorResponseWithMessage(@NonNull String message) {
        return new ErrorResponse(INTERNAL_SERVER_ERROR.value(), message);
    }
}
