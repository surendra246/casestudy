package com.banking.loanapp.service;

import com.banking.loanapp.dto.request.LoanApplicationRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;

public interface LoanApplicationService {
    GenericResponse<LoanApplicationResponseDTO> applyLoan(LoanApplicationRequestDTO requestDTO);
}

