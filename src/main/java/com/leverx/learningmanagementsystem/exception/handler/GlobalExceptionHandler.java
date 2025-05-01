package com.leverx.learningmanagementsystem.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.InvalidCourseDatesException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.response.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_FORMAT;
import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_TIME_FORMAT;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public @ResponseBody ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(IncorrectResultSizeException.class)
    @ResponseStatus(CONFLICT)
    public @ResponseBody ErrorResponse handleMismatchException(IncorrectResultSizeException ex) {
        return new ErrorResponse(CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidCourseDatesException.class)
    @ResponseStatus(CONFLICT)
    public @ResponseBody ErrorResponse handleInvalidCourseDatesException(InvalidCourseDatesException ex) {
        return new ErrorResponse(CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
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
    public @ResponseBody ErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.util.UUID.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid UUID format. UUID must be in standard 36-character representation.");
        } else if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.time.LocalDateTime.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid DateTime format. Expected format: " + DATE_TIME_FORMAT);
        } else if (cause instanceof InvalidFormatException &&
                ((InvalidFormatException) cause).getTargetType() == java.time.LocalDate.class) {
            return new ErrorResponse(BAD_REQUEST.value(),
                    "Invalid Date format. Expected format: " + DATE_FORMAT);
        }
        return new ErrorResponse(BAD_REQUEST.value(),"Malformed JSON request.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public @ResponseBody ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == java.util.UUID.class) {
            return new ErrorResponse(BAD_REQUEST.value(), "Invalid UUID format: " + ex.getValue());
        }
        return new ErrorResponse(BAD_REQUEST.value(), "Invalid parameter type.");
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(NOT_FOUND)
    public @ResponseBody ErrorResponse handleRuntimeException(RuntimeException ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }
}
