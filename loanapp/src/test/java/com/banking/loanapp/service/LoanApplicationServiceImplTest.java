package com.banking.loanapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;

import com.banking.loanapp.constants.ErrorCodes;
import com.banking.loanapp.constants.ErrorMessages;
import com.banking.loanapp.constants.ResponseMessages;
import com.banking.loanapp.dto.request.CustomerDTO;
import com.banking.loanapp.dto.request.EmploymentDetailsDTO;
import com.banking.loanapp.dto.request.LoanApplicationRequestDTO;
import com.banking.loanapp.dto.request.PropertyDetailsDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.entity.EmploymentDetails;
import com.banking.loanapp.entity.LoanApplication;
import com.banking.loanapp.entity.PropertyDetails;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.repository.EmploymentDetailsRepository;
import com.banking.loanapp.repository.LoanApplicationRepository;
import com.banking.loanapp.repository.PropertyDetailsRepository;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceImplTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private EmploymentDetailsRepository employmentRepository;
    @Mock private PropertyDetailsRepository propertyRepository;
    @Mock private LoanApplicationRepository loanApplicationRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks private LoanApplicationServiceImpl loanService;

    private LoanApplicationRequestDTO buildValidRequest() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFullName("John Doe");
        customerDTO.setEmail("john@example.com");
        customerDTO.setPanNumber("ABCDE1234F");
        customerDTO.setAadharNumber("123456789012");

        EmploymentDetailsDTO employmentDTO = new EmploymentDetailsDTO();
        employmentDTO.setEmployerName("ABC Corp");
        employmentDTO.setJobTitle("Engineer");
        employmentDTO.setAnnualIncome(BigDecimal.valueOf(1000000));

        PropertyDetailsDTO propertyDTO = new PropertyDetailsDTO();
        propertyDTO.setPropertyType("Flat");
        propertyDTO.setMarketValue(BigDecimal.valueOf(5000000));

        LoanApplicationRequestDTO request = new LoanApplicationRequestDTO();
        request.setCustomer(customerDTO);
        request.setEmploymentDetails(employmentDTO);
        request.setPropertyDetails(propertyDTO);
        return request;
    }

    @Test
    void applyLoan_successfulRequest_returnsSuccessResponse() {
        LoanApplicationRequestDTO request = buildValidRequest();

        Customer savedCustomer = new Customer();
        savedCustomer.setCustomerId(1L);
        savedCustomer.setEmail("john@example.com");
        savedCustomer.setPanNumber("ABCDE1234F");
        savedCustomer.setAadharNumber("123456789012");
        savedCustomer.setFullName("John Doe");
        savedCustomer.setDob(LocalDate.parse("1990-01-01"));
        savedCustomer.setCreatedAt(LocalDateTime.now());

        LoanApplication savedApplication = new LoanApplication();
        savedApplication.setApplicationId(100L);
        savedApplication.setStatus("PENDING");
        savedApplication.setCreatedAt(LocalDateTime.now());
        savedApplication.setCustomer(savedCustomer);

        // Mock repository behavior
        when(customerRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(customerRepository.existsByPanNumber("ABCDE1234F")).thenReturn(false);
        when(customerRepository.existsByAadharNumber("123456789012")).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(employmentRepository.save(any())).thenReturn(new EmploymentDetails());
        when(propertyRepository.save(any())).thenReturn(new PropertyDetails());
        when(loanApplicationRepository.save(any())).thenReturn(savedApplication);

        // Mock Kafka send
        ProducerRecord<String, Object> record = new ProducerRecord<>("loan_application", new Object());
        RecordMetadata metadata = new RecordMetadata(null, 0, 0, 0L, 0L, 0, 0);
        SendResult<String, Object> sendResult = new SendResult<>(record, metadata);
        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(eq("loan_application"), any())).thenReturn(future);

        // Execute
        GenericResponse<LoanApplicationResponseDTO> response = loanService.applyLoan(request);

        // Assert
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("2001", response.getCode());
        assertNotNull(response.getData());
        assertEquals(100L, response.getData().getApplicationId());
    }

    @Test
    void applyLoan_duplicateEmail_returnsFailureResponse() {
        LoanApplicationRequestDTO request = buildValidRequest();
        when(customerRepository.existsByEmail("john@example.com")).thenReturn(true);

        GenericResponse<LoanApplicationResponseDTO> response = loanService.applyLoan(request);

        assertEquals("FAILURE", response.getStatus());
        assertEquals(ErrorCodes.DUPLICATE_CUSTOMER, response.getCode());
        assertEquals(ErrorMessages.CUSTOMER_EMAIL_SHOULD_UNIQUE, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void applyLoan_nullCustomer_returnsFailureResponse() {
        LoanApplicationRequestDTO request = new LoanApplicationRequestDTO();
        request.setCustomer(null);

        GenericResponse<LoanApplicationResponseDTO> response = loanService.applyLoan(request);

        assertEquals("FAILURE", response.getStatus());
        assertEquals("5002", response.getCode());
        assertEquals(ResponseMessages.LOAN_APPLICATION_FAILED_DUE_DATA, response.getMessage());
        assertNull(response.getData());
    }
}
