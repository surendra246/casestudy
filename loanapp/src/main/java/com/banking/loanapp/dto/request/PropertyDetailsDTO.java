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
public class PropertyDetailsDTO {
   private Long propertyId;
   private Long customerId;
   @NotBlank
   private String propertyType;
   @NotBlank
   private String address;
   @DecimalMin(value = "0.0", inclusive = false)
   private BigDecimal marketValue;
}
