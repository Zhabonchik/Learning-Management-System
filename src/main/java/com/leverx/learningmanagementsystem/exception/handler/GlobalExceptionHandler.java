package com.leverx.learningmanagementsystem.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.InvalidCourseDatesException;
import com.leverx.learningmanagementsystem.exception.response.ErrorResponse;
import com.leverx.learningmanagementsystem.exception.MismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(MismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleMismatchException(MismatchException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidCourseDatesException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleInvalidCourseDatesException(InvalidCourseDatesException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation error");
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.util.UUID.class) {
            return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Invalid UUID format. UUID must be in standard 36-character representation.");
        } else if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.time.LocalDateTime.class) {
            return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Invalid DateTime format. Expected format: yyyy-MM-dd HH:mm:ss.");
        } else if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.time.LocalDate.class) {
            return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Invalid Date format. Expected format: yyyy-MM-dd.");
        }
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Malformed JSON request.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == java.util.UUID.class) {
            return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid UUID format: " + ex.getValue());
        }
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid parameter type.");
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleRuntimeException(RuntimeException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
