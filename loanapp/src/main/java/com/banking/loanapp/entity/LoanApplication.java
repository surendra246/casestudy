package com.banking.loanapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "application_id")
   private Long applicationId;
   @ManyToOne
   @JoinColumn(name = "customer_id", nullable = false)
   private Customer customer;
   @NotBlank
   @Column(length = 20, nullable = false)
   private String status;  // APPROVED, REJECTED, PENDING, SENT_TO_KAFKA
   @Size(max = 50)
   @Column(name = "kafka_offset")
   private String kafkaOffset;
   @Column(name = "created_at")
   private LocalDateTime createdAt = LocalDateTime.now();
}
