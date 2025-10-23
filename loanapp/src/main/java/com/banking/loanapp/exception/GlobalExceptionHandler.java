package com.banking.loanapp.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.banking.loanapp.constants.ResponseMessages;
import com.banking.loanapp.dto.GenericResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<GenericResponse<Void>> handleDuplicateCustomer(DuplicateCustomerException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("customer", ResponseMessages.DUPLICATE_CUSTOMER);
        GenericResponse<Void> response = new GenericResponse<>(
            ex.getMessage(),
            "DUPLICATE_CUSTOMER",
            null,
            errorMap,
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        GenericResponse<Void> response = new GenericResponse<>(
            ResponseMessages.VALIDATION_FAILED,
            "VALIDATION_ERROR",
            null,
            fieldErrors,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGenericException(Exception ex) {
        GenericResponse<Void> response = new GenericResponse<>(
            ResponseMessages.UNEXPECTED_ERROR,
            "INTERNAL_ERROR",
            null,
            null,
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
