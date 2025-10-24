package com.banking.loanapp.service;

import com.banking.loanapp.constants.ErrorCodes;
import com.banking.loanapp.constants.ErrorMessages;
import com.banking.loanapp.constants.ResponseMessages;
import com.banking.loanapp.dto.request.LoanDecisionDTO;
import com.banking.loanapp.dto.response.GenericResponse;
import com.banking.loanapp.dto.response.LoanApplicationKafkaDTO;
import com.banking.loanapp.dto.response.LoanApplicationResponseDTO;
import com.banking.loanapp.entity.Customer;
import com.banking.loanapp.entity.EmploymentDetails;
import com.banking.loanapp.entity.PropertyDetails;
import com.banking.loanapp.entity.LoanApplication;
import com.banking.loanapp.entity.LoanDecision;
import com.banking.loanapp.repository.CustomerRepository;
import com.banking.loanapp.repository.EmploymentDetailsRepository;
import com.banking.loanapp.repository.PropertyDetailsRepository;
import com.banking.loanapp.repository.LoanApplicationRepository;
import com.banking.loanapp.repository.LoanDecisionRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoanApplicationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LoanApplicationConsumer.class);

    private final LoanDecisionRepository loanDecisionRepository;
    private final CustomerRepository customerRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final EmploymentDetailsRepository employmentRepository;
    private final PropertyDetailsRepository propertyRepository;

    public LoanApplicationConsumer(LoanDecisionRepository loanDecisionRepository,
                                   CustomerRepository customerRepository,
                                   LoanApplicationRepository loanApplicationRepository,
                                   EmploymentDetailsRepository employmentRepository,
                                   PropertyDetailsRepository propertyRepository) {
        this.loanDecisionRepository = loanDecisionRepository;
        this.customerRepository = customerRepository;
        this.loanApplicationRepository = loanApplicationRepository;
        this.employmentRepository = employmentRepository;
        this.propertyRepository = propertyRepository;
    }

    /**
     * Kafka listener to consume loan applications.
     */
    @KafkaListener(
            topics = "loan_application",
            groupId = "loanapp-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consumeLoanApplication(LoanApplicationKafkaDTO message) {
        logger.info("Received Kafka Loan Application | AppID: {}, CustID: {}, Status: {}, Message: {}",
                message.getApplicationId(), message.getCustomerId(), message.getStatus(), message.getMessage());

        Optional<LoanApplication> optionalApplication = loanApplicationRepository.findById(message.getApplicationId());
        if (optionalApplication.isEmpty()) {
            logger.warn(ResponseMessages.APPLICATION_NOT_FOUND.replace("{}", message.getApplicationId().toString()));
            return;
        }
        LoanApplication application = optionalApplication.get();

        Optional<Customer> optionalCustomer = customerRepository.findById(message.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            logger.warn(ResponseMessages.CUSTOMER_NOT_FOUND);
            return;
        }
        Customer customer = optionalCustomer.get();

        // Make decision using common logic
        makeDecisionInternal(application, customer);
        logger.info(ResponseMessages.APPLICATION_CONSUMES_KAFKA);
    }

    /**
     * Manual loan decision API using LoanDecisionDTO.
     */
    @Transactional
    public GenericResponse<LoanApplicationResponseDTO> makeDecision( LoanDecisionDTO requestDTO) {

        Optional<LoanApplication> optionalApplication = loanApplicationRepository.findById(requestDTO.getCustomerId());
        if (optionalApplication.isEmpty()) {
            return new GenericResponse<>(
                    ResponseMessages.APPLICATION_NOT_FOUND.replace("{}", requestDTO.getCustomerId().toString()),
                    ErrorCodes.DATA_VALIDATION_FAILED,
                    null,
                    "FAILURE"
            );
        }
        LoanApplication application = optionalApplication.get();

        Optional<Customer> optionalCustomer = customerRepository.findById(requestDTO.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return new GenericResponse<>(
                    ResponseMessages.CUSTOMER_NOT_FOUND,
                    ErrorCodes.DATA_VALIDATION_FAILED,
                    null,
                    "FAILURE"
            );
        }
        Customer customer = optionalCustomer.get();

        // Perform the loan decision
        makeDecisionInternal(application, customer);

        LoanApplicationResponseDTO responseDTO = LoanApplicationResponseDTO.builder()
                .applicationId(application.getApplicationId())
                .status(application.getStatus())
                .build();

        return new GenericResponse<>(
                ResponseMessages.APPLICATION_UPDATED.replace("{}", application.getStatus()) + " for ID: " + application.getApplicationId(),
                null,
                responseDTO,
                "SUCCESS"
        );
    }

    /**
     * Internal method for loan decision logic (salary & property eligibility).
     */
    private void makeDecisionInternal(LoanApplication application, Customer customer) {

        // Fetch Employment Details
        Optional<EmploymentDetails> optionalEmployment = employmentRepository.findByCustomer(customer);
        if (optionalEmployment.isEmpty()) {
            logger.warn("Employment details not found for customer ID: {}", customer.getCustomerId());
            rejectApplication(application);
            return;
        }
        EmploymentDetails employment = optionalEmployment.get();
        BigDecimal annualIncome = employment.getAnnualIncome() != null ? employment.getAnnualIncome() : BigDecimal.ZERO;
        BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);


        // Fetch Property Details
        Optional<PropertyDetails> optionalProperty = propertyRepository.findByCustomer(customer);
        if (optionalProperty.isEmpty()) {
            logger.warn("Property details not found for customer ID: {}", customer.getCustomerId());
            rejectApplication(application);
            return;
        }
        PropertyDetails property = optionalProperty.get();
        BigDecimal marketValue = property.getMarketValue() != null ? property.getMarketValue() : BigDecimal.ZERO;

        // Eligibility check: salary > 25k AND property > 1M
        boolean salaryEligible = monthlyIncome.compareTo(BigDecimal.valueOf(25000)) > 0;
        boolean propertyEligible = marketValue.compareTo(BigDecimal.valueOf(1000000)) > 0;

        if (salaryEligible && propertyEligible) {
            approveLoan(application, customer, monthlyIncome, marketValue);
            System.out.println("Yes customer has eligible property");
        } else {
            rejectApplication(application);
            logger.info("Loan Rejected | Customer ID: {} | Monthly Salary: {} | Property Value: {}",
                    customer.getCustomerId(), monthlyIncome, marketValue);
        }
    }

    /**
     * Approve loan and persist decision.
     */
    private void approveLoan(LoanApplication application, Customer customer,
                             BigDecimal monthlyIncome, BigDecimal propertyValue) {

        BigDecimal approvedAmount = propertyValue.multiply(BigDecimal.valueOf(0.8));
        BigDecimal interestRate = BigDecimal.valueOf(7.5);

        LoanDecision decision = new LoanDecision();
        decision.setCustomer(customer);
        decision.setApprovedAmount(approvedAmount);
        decision.setInterestRate(interestRate);
        decision.setRemarks("Auto-approved | Monthly Income: " + monthlyIncome + ", Property Value: " + propertyValue);
        decision.setDecidedAt(LocalDateTime.now());


         loanDecisionRepository.save(decision);

        application.setStatus("APPROVED");
        loanApplicationRepository.save(application);

        logger.info("Loan Approved | Customer ID: {} | Approved Amount: {} | Interest: {}%",
                customer.getCustomerId(), approvedAmount, interestRate);
    }

    /**
     * Reject loan and update status.
     */
    private void rejectApplication(LoanApplication application) {
        application.setStatus("REJECTED");
        loanApplicationRepository.save(application);
    }
}
