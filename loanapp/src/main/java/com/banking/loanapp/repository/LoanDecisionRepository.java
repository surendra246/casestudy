package com.banking.loanapp.repository;

import com.banking.loanapp.entity.LoanDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanDecisionRepository extends JpaRepository<LoanDecision, Long> {
    Optional<LoanDecision> findTopByCustomerCustomerId(Long customerId);
}
