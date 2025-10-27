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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private AuthController authController;
    private AuthRequestDTO authRequest;
    private Customer customer;
    @BeforeEach
    void setUp() {
        authRequest = new AuthRequestDTO();
        authRequest.setUsername("john@example.com");
        authRequest.setPassword("password123");
        customer = new Customer();
        customer.setEmail("john@example.com");
        customer.setPassword("password123");
    }
    @Test
    void testGenerateToken_Success() {
        // Mock repository and token util
        when(customerRepository.findByEmail(authRequest.getUsername())).thenReturn(Optional.of(customer));
        when(jwtTokenUtil.generateToken(authRequest.getUsername())).thenReturn("mocked-jwt-token");
        ResponseEntity<GenericResponse<Map<String, Object>>> response = authController.generateToken(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GenericResponse<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertEquals("Token generated successfully", body.getMessage());
        assertNotNull(body.getData());
        Map<String, Object> data = body.getData();
        assertEquals("mocked-jwt-token", data.get("token"));
        assertEquals(3600, data.get("expiresIn"));
        assertEquals("Bearer", data.get("tokenType"));
        verify(customerRepository, times(1)).findByEmail(authRequest.getUsername());
        verify(jwtTokenUtil, times(1)).generateToken(authRequest.getUsername());
    }
    @Test
    void testGenerateToken_InvalidCredentials_WrongPassword() {
        // Customer exists but password mismatch
        customer.setPassword("wrongpassword");
        when(customerRepository.findByEmail(authRequest.getUsername())).thenReturn(Optional.of(customer));
        ResponseEntity<GenericResponse<Map<String, Object>>> response = authController.generateToken(authRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        GenericResponse<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid credentials", body.getMessage());
        assertNull(body.getData());
        verify(customerRepository, times(1)).findByEmail(authRequest.getUsername());
        verifyNoInteractions(jwtTokenUtil);
    }
    @Test
    void testGenerateToken_InvalidCredentials_UserNotFound() {
        // User not found
        when(customerRepository.findByEmail(authRequest.getUsername())).thenReturn(Optional.empty());
        ResponseEntity<GenericResponse<Map<String, Object>>> response = authController.generateToken(authRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        GenericResponse<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid credentials", body.getMessage());
        assertNull(body.getData());
        verify(customerRepository, times(1)).findByEmail(authRequest.getUsername());
        verifyNoInteractions(jwtTokenUtil);
    }
}
