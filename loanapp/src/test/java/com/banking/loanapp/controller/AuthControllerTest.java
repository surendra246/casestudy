package com.banking.loanapp.controller;

import com.banking.loanapp.dto.AuthRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthController authController;

    private AuthRequestDTO request;
    private Customer customer;

    @BeforeEach
    void setUp() {
        request = new AuthRequestDTO();
        request.setUsername("test@example.com");
        request.setPassword("password123");
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("password123");
    }

    @Test
    void testGenerateToken_Success() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(jwtTokenUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");
        ResponseEntity<GenericResponse<Map<String, Object>>> responseEntity = authController.generateToken(request);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Token generated successfully", responseEntity.getBody().getMessage());
        assertEquals("success", responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().getData().containsKey("token"));
        assertEquals("mock-jwt-token", responseEntity.getBody().getData().get("token"));
        verify(customerRepository, times(1)).findByEmail("test@example.com");
        verify(jwtTokenUtil, times(1)).generateToken("test@example.com");

    }

    @Test
    void testGenerateToken_UserNotFound() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        ResponseEntity<GenericResponse<Map<String, Object>>> responseEntity = authController.generateToken(request);
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Invalid username or password", responseEntity.getBody().getMessage());
        assertEquals("failure", responseEntity.getBody().getStatus());
        verify(customerRepository, times(1)).findByEmail("test@example.com");
        verify(jwtTokenUtil, never()).generateToken(anyString());

    }

    @Test
    void testGenerateToken_WrongPassword() {
        customer.setPassword("differentPassword");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        ResponseEntity<GenericResponse<Map<String, Object>>> responseEntity = authController.generateToken(request);
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Invalid username or password", responseEntity.getBody().getMessage());
        assertEquals("failure", responseEntity.getBody().getStatus());
        verify(customerRepository, times(1)).findByEmail("test@example.com");
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }
}
