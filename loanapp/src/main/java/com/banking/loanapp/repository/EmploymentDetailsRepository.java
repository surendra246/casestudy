package com.banking.loanapp.repository;

import com.banking.loanapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.loanapp.entity.EmploymentDetails;

import java.util.Optional;

@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Long> {
    Optional<EmploymentDetails> findByCustomer(Customer customerId);
}



