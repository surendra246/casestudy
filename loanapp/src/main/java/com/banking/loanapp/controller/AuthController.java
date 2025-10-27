package com.banking.loanapp.controller;

import com.banking.loanapp.dto.AuthRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.security.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerRepository customerRepository;
    public AuthController(JwtTokenUtil jwtTokenUtil, CustomerRepository customerRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerRepository = customerRepository;
    }
    @PostMapping("/token")
    public ResponseEntity<GenericResponse<Map<String, Object>>> generateToken(@RequestBody AuthRequestDTO request) {
        Optional<Customer> user = customerRepository.findByEmail(request.getUsername());
        if (user.isEmpty() || !user.get().getPassword().equals(request.getPassword())) {
            GenericResponse<Map<String, Object>> response =
                    new GenericResponse<>("Invalid username or password", "401", null, "failure");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = jwtTokenUtil.generateToken(request.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("expiresIn", 3600);
        data.put("tokenType", "Bearer");
        GenericResponse<Map<String, Object>> response =
                new GenericResponse<>("Token generated successfully", "200", data, "success");
        return ResponseEntity.ok(response);
    }
}
