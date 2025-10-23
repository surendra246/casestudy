package com.banking.loanapp.dto;

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
public class LoanApplicationDTO {
   private Long applicationId;
   @NotNull
   private Long customerId;
   private String status; // APPROVED / REJECTED / PENDING / SENT_TO_KAFKA
   private String kafkaOffset;
   private LocalDateTime createdAt;
}
