package com.banking.loanapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.loanapp.entity.LoanDecision;

public interface LoanDecisionRepository extends JpaRepository<LoanDecision, Long> {
}