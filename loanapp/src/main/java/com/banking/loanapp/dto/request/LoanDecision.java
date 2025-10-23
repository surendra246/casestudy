package com.banking.loanapp.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDecision {
   private Long decisionId;
   @NotNull
   private Long customerId;
   private BigDecimal approvedAmount;
   private BigDecimal interestRate;
   private String remarks;
   private LocalDateTime decidedAt;
}
