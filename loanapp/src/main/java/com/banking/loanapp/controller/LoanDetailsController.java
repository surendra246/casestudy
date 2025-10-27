package com.banking.loanapp.controller;


import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.service.LoanDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanDetailsController {
    private final LoanDetailsService loanDetailsService;
    public LoanDetailsController(LoanDetailsService loanDetailsService) {
        this.loanDetailsService = loanDetailsService;
    }
    @GetMapping("/details")
    public ResponseEntity<GenericResponse<?>> getLoanDetails(@RequestParam Long customerId) {
        return ResponseEntity.ok(loanDetailsService.getLoanDetails(customerId));
    }
}
