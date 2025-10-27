package com.banking.loanapp.controller;

import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.service.LoanDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanDetailsControllerTest {
    @Mock
    private LoanDetailsService loanDetailsService;
    @InjectMocks
    private LoanDetailsController loanDetailsController;

    @Test
    void testGetLoanDetails_Success() {
        Long customerId = 101L;
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("loanId", 555);
        mockData.put("status", "ACTIVE");
        GenericResponse<Map<String, Object>> mockResponse =
                new GenericResponse<>("Loan details fetched successfully", "200", mockData, "SUCCESS");
        when(loanDetailsService.getLoanDetails(customerId)).thenReturn(mockResponse);
        ResponseEntity<GenericResponse<?>> responseEntity = loanDetailsController.getLoanDetails(customerId);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Loan details fetched successfully", responseEntity.getBody().getMessage());
        assertEquals("SUCCESS", responseEntity.getBody().getStatus());
        assertEquals(mockData, responseEntity.getBody().getData());
        verify(loanDetailsService, times(1)).getLoanDetails(customerId);
    }
}
