package com.banking.loanapp.service;

import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.entity.LoanDecision;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.repository.LoanDecisionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoanDetailsService {
    private final CustomerRepository customerRepository;
    private final LoanDecisionRepository loanDecisionRepository;
    public LoanDetailsService(CustomerRepository customerRepository,
                              LoanDecisionRepository loanDecisionRepository) {
        this.customerRepository = customerRepository;
        this.loanDecisionRepository = loanDecisionRepository;
    }
    public GenericResponse<Map<String, Object>> getLoanDetails(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            // Use the existing constructor directly
            return new GenericResponse<>(
                    "Customer not found",
                    "INVALID_CUSTOMER_ID",
                    null,
                    HttpStatus.NOT_FOUND.toString()
            );
        }
        Customer customer = customerOpt.get();
        Optional<LoanDecision> loanOpt = loanDecisionRepository.findTopByCustomerCustomerId(customerId);
        Map<String, Object> responseData = new HashMap<>();
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("fullName", customer.getFullName());
        customerMap.put("email", customer.getEmail());
        customerMap.put("phoneNumber", customer.getPhoneNumber());
        responseData.put("customer", customerMap);
        loanOpt.ifPresent(loan -> {
            Map<String, Object> loanMap = new HashMap<>();
            loanMap.put("loanId", loan.getDecisionId());
            loanMap.put("approvedAmount", loan.getApprovedAmount());
            loanMap.put("interestRate", loan.getInterestRate());
            loanMap.put("status", "ACTIVE");
            responseData.put("loan", loanMap);
        });
        return new GenericResponse<>(
                "Loan details fetched successfully",
                HttpStatus.OK.toString(),
                responseData,
                "SUCCESS"
        );
    }
}
