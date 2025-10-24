package com.banking.loanapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@Entity
@Table(name = "loan_decisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDecision {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "decision_id")
   private Long decisionId;

   @OneToOne
   @JoinColumn(name = "customer_id", nullable = false)
   private Customer customer;

   @DecimalMin(value = "0.0")
   //@Digits(integer = 5, fraction = 2)
   @Column(name = "approved_amount", precision = 12, scale = 2)
   private BigDecimal approvedAmount;

   @DecimalMin(value = "0.0")
   @Digits(integer = 5, fraction = 2)
   @Column(name = "interest_rate", precision = 5, scale = 2)
   private BigDecimal interestRate;

   @Column(columnDefinition = "TEXT")
   private String remarks;
   @Column(name = "decided_at")
   private LocalDateTime decidedAt = LocalDateTime.now();
}
