package com.banking.loanapp.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDecisionDTO {
   @NotNull
   private Long customerId;
   private BigDecimal approvedAmount;
   private BigDecimal interestRate;
   private String remarks;
}
