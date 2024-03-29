package com.trendytracker.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.trendytracker.common.exception.detail.*;

import jakarta.validation.ValidationException;

@ControllerAdvice
public class ApiExceptionHandler {
    /*
     * Exception 에 대한 전역 Handler 처리
     */

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        List<String> errors = new ArrayList<>();
        ApiError apiError;
        errors.add(ex.getMessage());

        if (ex.getMessage().contains("TechType"))
            apiError = new ApiError(HttpStatus.BAD_REQUEST, "invalid type error", errors);
        else if (ex.getClass().getName().contains("NotFoundException"))
            apiError = new ApiError(HttpStatus.NOT_FOUND, "no result were found", errors);
        else 
            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "runtime error", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<?> handleNoResult(NoResultException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "no result were found", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> handleAlreadyExistResult(AlreadyExistException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "already exist", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "validation error", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(NotAllowedValueException.class)
    public ResponseEntity<?> handleNotAllow(NotAllowedValueException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE, "not allowed value error", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
