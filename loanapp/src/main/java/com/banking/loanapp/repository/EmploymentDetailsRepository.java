package com.banking.loanapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.loanapp.entity.EmploymentDetails;

@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Long> {
}

