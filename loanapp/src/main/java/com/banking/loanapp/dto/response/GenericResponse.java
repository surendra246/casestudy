package com.banking.loanapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
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
}

