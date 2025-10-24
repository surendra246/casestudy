package com.banking.loanapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.loanapp.entity.PropertyDetails;

@Repository
public interface PropertyDetailsRepository extends JpaRepository<PropertyDetails, Long> {
}

