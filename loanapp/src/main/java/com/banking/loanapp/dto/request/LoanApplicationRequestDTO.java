package com.banking.loanapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanApplicationRequestDTO {
    
    @Valid
    @NotNull(message = "Customer details are required")
    private CustomerDTO customer;

    
    @Valid
    private EmploymentDetailsDTO employmentDetails;

    
    @Valid
    private PropertyDetailsDTO propertyDetails;
}
