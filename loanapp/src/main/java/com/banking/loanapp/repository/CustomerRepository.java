package com.banking.loanapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.loanapp.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    boolean existsByPanNumber(String panNumber);
    boolean existsByAadharNumber(String aadharNumber);
}
