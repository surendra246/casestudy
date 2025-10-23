package com.banking.loanapp.service;

import com.banking.loanapp.dto.request.LoanApplicationRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;

public interface LoanDecisionService {
    GenericResponse<LoanApplicationResponseDTO> applyLoan(LoanApplicationRequestDTO request);
}

