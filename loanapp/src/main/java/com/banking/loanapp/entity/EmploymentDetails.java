package com.banking.loanapp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employment_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "employment_id")
   private Long employmentId;
   @ManyToOne
   @JoinColumn(name = "customer_id", nullable = false)
   private Customer customer;
   @NotBlank
   @Size(max = 100)
   @Column(name = "employer_name", nullable = false)
   private String employerName;
   @NotBlank
   @Size(max = 50)
   @Column(name = "job_title", nullable = false)
   private String jobTitle;
   @DecimalMin(value = "0.0", inclusive = false)
   @Digits(integer = 10, fraction = 2)
   @Column(name = "annual_income", precision = 12, scale = 2, nullable = false)
   private BigDecimal annualIncome;
}
