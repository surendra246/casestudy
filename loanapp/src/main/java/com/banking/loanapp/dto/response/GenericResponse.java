package com.banking.loanapp.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class GenericResponse<T> {
    private String message;
    private String code;
    private T data;
    private String status;

    public GenericResponse(String message, String code, T data, String status) {
        this.message = message;
        this.code = code;
        this.data = data;
        this.status = status;
    }

    public GenericResponse(String loanDetailsFetchedSuccessfully, int i, Map<String, Object> loanDetails, String success) {
        this.message = message;
        this.code = code;
        this.data = data;
        this.status = status;
    }
}

