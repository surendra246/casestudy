package com.banking.loanapp.service;

import java.time.LocalDateTime;

import org.apache.kafka.common.KafkaException;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.banking.loanapp.constants.ErrorCodes;
import com.banking.loanapp.constants.ErrorMessages;
import com.banking.loanapp.constants.ResponseMessages;
import com.banking.loanapp.dto.request.LoanApplicationRequestDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationKafkaDTO;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.entity.EmploymentDetails;
import com.banking.loanapp.entity.LoanApplication;
import com.banking.loanapp.entity.PropertyDetails;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.repository.EmploymentDetailsRepository;
import com.banking.loanapp.repository.LoanApplicationRepository;
import com.banking.loanapp.repository.PropertyDetailsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final CustomerRepository customerRepository;
    private final EmploymentDetailsRepository employmentRepository;
    private final PropertyDetailsRepository propertyRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final KafkaTemplate<String, LoanApplicationKafkaDTO> kafkaTemplate;

    public LoanApplicationServiceImpl(
        CustomerRepository customerRepository,
        EmploymentDetailsRepository employmentRepository,
        PropertyDetailsRepository propertyRepository,
        LoanApplicationRepository loanApplicationRepository,
        KafkaTemplate<String, LoanApplicationKafkaDTO> kafkaTemplate
    ) {
        this.customerRepository = customerRepository;
        this.employmentRepository = employmentRepository;
        this.propertyRepository = propertyRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public GenericResponse<LoanApplicationResponseDTO> applyLoan(LoanApplicationRequestDTO requestDTO) {
        try {
            // Validate customer input
            if (requestDTO.getCustomer() == null) {
                return new GenericResponse<>(
                    ResponseMessages.LOAN_APPLICATION_FAILED_DUE_DATA,
                    "5002",
                    null,
                    "FAILURE"
                );
            }

            String email = requestDTO.getCustomer().getEmail();
            String pan = requestDTO.getCustomer().getPanNumber();
            String aadhar = requestDTO.getCustomer().getAadharNumber();

            // Duplicate checks
            if (email != null && customerRepository.existsByEmail(email)) {
                return new GenericResponse<>(
                    ErrorMessages.CUSTOMER_EMAIL_SHOULD_UNIQUE,
                    ErrorCodes.DUPLICATE_CUSTOMER,
                    null,
                    "FAILURE"
                );
            }

            if (pan != null && customerRepository.existsByPanNumber(pan)) {
                return new GenericResponse<>(
                    ErrorMessages.CUSTOMER_PANCARD_NUMBER_SHOULD_UNIQUE,
                    ErrorCodes.DUPLICATE_CUSTOMER,
                    null,
                    "FAILURE"
                );
            }

            if (aadhar != null && customerRepository.existsByAadharNumber(aadhar)) {
                return new GenericResponse<>(
                    ErrorMessages.CUSTOMER_AADHAR_CARD_SHOULD_UNIQUE,
                    ErrorCodes.DUPLICATE_CUSTOMER,
                    null,
                    "FAILURE"
                );
            }

            // ✅ Save Customer
            Customer customer = new Customer();
            BeanUtils.copyProperties(requestDTO.getCustomer(), customer);
            customer.setCreatedAt(LocalDateTime.now());
            customer = customerRepository.save(customer);

            // Save Employment Details
            if (requestDTO.getEmploymentDetails() != null) {
                EmploymentDetails employment = new EmploymentDetails();
                BeanUtils.copyProperties(requestDTO.getEmploymentDetails(), employment);
                employment.setCustomer(customer);
                employmentRepository.save(employment);
            }

            // Save Property Details
            if (requestDTO.getPropertyDetails() != null) {
                PropertyDetails property = new PropertyDetails();
                BeanUtils.copyProperties(requestDTO.getPropertyDetails(), property);
                property.setCustomer(customer);
                propertyRepository.save(property);
            }

            // Save Loan Application
            LoanApplication application = new LoanApplication();
            application.setCustomer(customer);
            application.setStatus("PENDING");
            application.setCreatedAt(LocalDateTime.now());
            application = loanApplicationRepository.save(application);

            // Send to Kafka
            LoanApplicationKafkaDTO kafkaDTO = new LoanApplicationKafkaDTO();
            kafkaDTO.setApplicationId(application.getApplicationId());
            kafkaDTO.setCustomerId(customer.getCustomerId());
            kafkaDTO.setStatus(application.getStatus());
            kafkaDTO.setMessage(ResponseMessages.APPLICATION_SEND_KAFKA);
            kafkaTemplate.send("loan_application", kafkaDTO);

            // Prepare response
            LoanApplicationResponseDTO responseDTO = new LoanApplicationResponseDTO();
            responseDTO.setApplicationId(application.getApplicationId());
            responseDTO.setStatus(application.getStatus());

            return new GenericResponse<>(
                ResponseMessages.APPLICATION_SUBMITTED,
                "2001",
                responseDTO,
                "SUCCESS"
            );

        } catch (DataIntegrityViolationException | ConstraintViolationException | NullPointerException e) {
            return new GenericResponse<>(
                ResponseMessages.LOAN_APPLICATION_FAILED_DUE_DATA,
                "5002",
                null,
                "FAILURE"
            );
        } catch (KafkaException e) {
            return new GenericResponse<>(
                ResponseMessages.LOAN_APPLICATION_FAILED_DUE_KAFKA_DISPATCH,
                "5003",
                null,
                "FAILURE"
            );
        }

    }
}

