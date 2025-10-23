package com.banking.loanapp.dto;

import java.util.Map;

import lombok.Data;

@Data
public class GenericResponse<T> {
    private String message;
    private String code;
    private T data;
    private Map<String, String> errors;
    private int status;

    public GenericResponse(String message, String code, T data, Map<String, String> errors, int status) {
        this.message = message;
        this.code = code;
        this.data = data;
        this.errors = errors;
        this.status = status;
    }
}

