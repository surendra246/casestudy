package com.banking.loanapp.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
   private Long customerId;
   @NotBlank(message = "Name is required")
   private String fullName;

   @NotBlank(message = "Password is required")
   private String password;

   @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
   private String email;

   @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    @NotBlank(message = "Phone number is required")
   private String phoneNumber;

   @Past(message = "Date of birth must be in the past")
   private LocalDate dob;

   @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
   private String panNumber;

   @Pattern(regexp = "\\d{12}", message = "Aadhar must be 12 digits")
   private String aadharNumber;
}
