package com.leverx.learningmanagementsystem.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.exception.InvalidCourseDatesException;
import com.leverx.learningmanagementsystem.exception.response.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_FORMAT;
import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_TIME_FORMAT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(IncorrectResultSizeException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleMismatchException(IncorrectResultSizeException ex) {
        return new ErrorResponse(CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidCourseDatesException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleInvalidCourseDatesException(InvalidCourseDatesException ex) {
        return new ErrorResponse(CONFLICT.value(), ex.getMessage());
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
        return new ErrorResponse(BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.util.UUID.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid UUID format. UUID must be in standard 36-character representation.");
        } else if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.time.LocalDateTime.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid DateTime format. Expected format: " + DATE_TIME_FORMAT);
        } else if (cause instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType() == java.time.LocalDate.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid Date format. Expected format: " + DATE_FORMAT);
        }
        return new ErrorResponse(BAD_REQUEST.value(),"Malformed JSON request.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return (ex.getRequiredType() == java.util.UUID.class)
                ? new ErrorResponse(BAD_REQUEST.value(), "Invalid UUID format: " + ex.getValue())
                : new ErrorResponse(BAD_REQUEST.value(), "Invalid parameter type.");
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }
}
