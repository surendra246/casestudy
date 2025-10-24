package com.banking.loanapp.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentDetailsDTO {
   private Long employmentId;
   private Long customerId;
   @NotBlank
   private String employerName;
   @NotBlank
   private String jobTitle;
   @DecimalMin(value = "0.0", inclusive = false)
   private BigDecimal annualIncome;
}
