package com.banking.loanapp.constants;

import java.math.BigDecimal;

public final class ResponseMessages {


    // Success messages
    public static final String APPLICATION_SUBMITTED = "Loan application submitted successfully";
    public static final String CUSTOMER_CREATED = "Customer created successfully";

    // Duplicate messages
    public static final String DUPLICATE_EMAIL = "Email already exists";
    public static final String DUPLICATE_PHONE = "Phone number already exists";
    

     // Logging
    public static final String CHECKING_EMAIL = "Checking for existing customer with email: {}";
    public static final String CHECKING_PHONE = "Checking for existing customer with phone: {}";
    public static final String DUPLICATE_EMAIL_LOG = "Duplicate email detected: {}";
    public static final String DUPLICATE_PHONE_LOG = "Duplicate phone detected: {}";

    // Validation messages
    public static final String INVALID_INPUT = "Invalid input data";
    public static final String VALIDATION_FAILED = "Validation failed";

    // Error messages
    public static final String UNEXPECTED_ERROR = "Unexpected error occurred";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";

    // Validation errors
    public static final String INVALID_PAN_FORMAT = "Invalid PAN format";
    public static final String INVALID_AADHAR_FORMAT = "Aadhar must be 12 digits";
    public static final String MISSING_REQUIRED_FIELD = "Required field is missing";

    // Conflict errors
    public static final String DUPLICATE_CUSTOMER = "Customer already exists with the provided details";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already registered";

    // Auth errors
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to access this resource";
    public static final String TOKEN_EXPIRED = "Authentication token has expired";
    public static final String TOKEN_INVALID = "Authentication token is invalid";

    // Generic errors
    public static final String INTERNAL_SERVER_ERROR = "Unexpected error occurred";
    public static final String RESOURCE_NOT_FOUND = "Requested resource was not found";
    public static final String SERVICE_UNAVAILABLE = "Service is temporarily unavailable";

    // Kafka
    public static final String KAFKA_RECEIVED = "Received Kafka message: Application ID = {}, Status = {}, Remarks = {}";
    public static final String APPLICATION_NOT_FOUND = "Application not found for ID: {}";
    public static final String APPLICATION_SEND_KAFKA = "application has been submitted successfully";
    public static final String APPLICATION_CONSUMES_KAFKA = "data has been Consumed successfully";

    // Application
    public static final String APPLICATION_UPDATED = "Application status updated to {} for ID: {}";
    public static final String CARD_ISSUED = "Credit card issued for customer: {}";
    public static final String CARD_NOT_ISSUED = "Credit card not issued for {}: Salary below threshold.";

    public static final BigDecimal FIXED_CREDIT_LIMIT = BigDecimal.valueOf(250000);
    

    public static final String LOAN_APPLICATION_FAILED_DUE_DATA = "Loan application failed due to data issue";
    public static final String LOAN_APPLICATION_FAILED_DUE_KAFKA_DISPATCH  = "Loan application failed during Kafka dispatch";
}
