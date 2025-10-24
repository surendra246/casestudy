package com.banking.loanapp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LoanDecisionResponseDTO {
    private Long decisionId;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private String remarks;
    private LocalDateTime decidedAt;
}

