package com.banking.loanapp.controller;

import com.banking.loanapp.dto.request.LoanDecisionDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;
import com.banking.loanapp.service.LoanApplicationConsumer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/loan")
public class LoanDecisionController {

    @Autowired
    private LoanApplicationConsumer loanApplicationConsumer;

    @PostMapping("/decision")
    public ResponseEntity<GenericResponse<LoanApplicationResponseDTO>> applyLoan(@Valid @RequestBody LoanDecisionDTO requestDTO
    ) {
        GenericResponse<LoanApplicationResponseDTO> response = loanApplicationConsumer.makeDecision(requestDTO);
        return ResponseEntity.ok(response);
    }

}
