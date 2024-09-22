package com.hotel.auth_service.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User Not Found");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");

        Throwable rootCause = NestedExceptionUtils.getRootCause(ex);
        if (rootCause != null) {
            // Log or return the root cause message
            errorResponse.put("message", rootCause.getMessage());
        } else {
            errorResponse.put("message", ex.getMessage());
        }

        errorResponse.put("exception", ex.getClass().getSimpleName());
        errorResponse.put("stackTrace", ex.getStackTrace());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionSystemException(TransactionSystemException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Transaction Error");

        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException validationException = (ConstraintViolationException) cause;

            Map<String, String> validationErrors = new HashMap<>();
            validationException.getConstraintViolations().forEach(violation -> {
                validationErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
            });

            errorResponse.put("validationErrors", validationErrors);
            errorResponse.put("message", "Validation failed for the transaction.");
        } else {
            errorResponse.put("message", ex.getMessage());
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
