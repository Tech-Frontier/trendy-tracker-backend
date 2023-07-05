package com.trendyTracker.common.Exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.trendyTracker.common.Exception.ExceptionDetail.NoResultException;

@ControllerAdvice
public class ApiExceptionHandler {
    /*
     * Custom Exception 에 대한 Handler
     */

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<?> handleNoResult(NoResultException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "no result were found", errors);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
