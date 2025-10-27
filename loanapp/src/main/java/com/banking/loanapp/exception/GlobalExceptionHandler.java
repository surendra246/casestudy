package com.banking.loanapp.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.banking.loanapp.constants.ErrorCodes;
import com.banking.loanapp.constants.ErrorMessages;
import com.banking.loanapp.constants.ResponseMessages;
import com.banking.loanapp.dto.response.GenericResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<GenericResponse<List<String>>> handleDuplicateCustomer(DuplicateCustomerException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("customer: " + ResponseMessages.DUPLICATE_CUSTOMER);

        GenericResponse<List<String>> response = new GenericResponse<>(
                ex.getMessage(),
                ErrorCodes.DUPLICATE_CUSTOMER,
                errors,
                ErrorMessages.DUPLICATE_CUSTOMER
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        GenericResponse<List<String>> response = new GenericResponse<>(
                ErrorMessages.VALIDATION_FAILED,
                ErrorCodes.VALIDATION_FAILED,
                errors,
                "FAILED"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericResponse<List<String>>> handleDataError(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(new GenericResponse<>(
                "Data integrity violation",
                "5002",
                List.of(ex.getMessage()),
                "FAILED"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<List<String>>> handleGenericException(Exception ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage() != null ? ex.getMessage() : "Unknown error");

        GenericResponse<List<String>> response = new GenericResponse<>(
                "An unexpected error occurred",
                "40004",
                errors,
                "FAILED"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

