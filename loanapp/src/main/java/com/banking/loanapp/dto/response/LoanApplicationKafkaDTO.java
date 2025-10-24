package com.banking.loanapp.dto.response;

import lombok.Data;

@Data
public class LoanApplicationKafkaDTO {
    private Long applicationId;
    private Long customerId;
    private String status;
    private String meessage;
}
