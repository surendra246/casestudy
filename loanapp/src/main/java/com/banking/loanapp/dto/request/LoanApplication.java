package com.banking.loanapp.dto.request;

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
public class LoanApplication {
   private Long applicationId;
   @NotNull
   private Long customerId;
   private String status;
   private String kafkaOffset;
   private LocalDateTime createdAt;
}
