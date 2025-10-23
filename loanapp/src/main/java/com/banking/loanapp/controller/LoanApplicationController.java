package com.banking.loanapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.loanapp.dto.request.LoanApplicationRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;
import com.banking.loanapp.service.LoanApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loan")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping("/apply")
    public ResponseEntity<GenericResponse<LoanApplicationResponseDTO>> applyLoan(@Valid @RequestBody LoanApplicationRequestDTO requestDTO
    ) {
        GenericResponse<LoanApplicationResponseDTO> response = loanApplicationService.applyLoan(requestDTO);
        return ResponseEntity.ok(response);
    }
}

