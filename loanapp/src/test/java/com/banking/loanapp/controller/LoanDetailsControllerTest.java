package com.banking.loanapp.controller;

import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.service.LoanDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanDetailsControllerTest {
    @Mock
    private LoanDetailsService loanDetailsService;
    @InjectMocks
    private LoanDetailsController loanDetailsController;

    private GenericResponse<Map<String, Object>> mockResponse;
    @BeforeEach
    void setUp() {
        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("customerName", "Ravi Kumar");
        loanDetails.put("loanAmount", 4000000);
        mockResponse = new GenericResponse<>(
                "Loan details fetched successfully",
                "200",
                loanDetails,
                "SUCCESS"
        );
    }


    @Test
    @DisplayName("Should return loan details successfully for valid customer ID")
    void testGetLoanDetailsSuccess() {
        // Arrange
        Long customerId = 1L;
        when(loanDetailsService.getLoanDetails(customerId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<GenericResponse<?>> response = loanDetailsController.getLoanDetails(customerId);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody()); // ✅ Ensure body is not null before accessing fields
        assertEquals("200", response.getBody().getCode()); // ✅ Expecting String, not int
        assertEquals("SUCCESS", response.getBody().getStatus());
        assertEquals("Loan details fetched successfully", response.getBody().getMessage());
        assertEquals("Ravi Kumar", ((Map<?, ?>) response.getBody().getData()).get("customerName"));

        verify(loanDetailsService, times(1)).getLoanDetails(customerId);
    }
    @Test
    @DisplayName("Should handle null or invalid customer ID gracefully")
    void testGetLoanDetailsWithInvalidId() {
        // Arrange
        Long invalidCustomerId = null;
        when(loanDetailsService.getLoanDetails(invalidCustomerId)).thenThrow(new IllegalArgumentException("Customer ID cannot be null"));
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> loanDetailsController.getLoanDetails(invalidCustomerId));
        assertEquals("Customer ID cannot be null", exception.getMessage());
        verify(loanDetailsService, times(1)).getLoanDetails(invalidCustomerId);
    }
}
