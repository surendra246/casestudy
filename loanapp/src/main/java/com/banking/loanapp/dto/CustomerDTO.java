package com.banking.loanapp.dto;

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
   @NotBlank
   private String fullName;
   @NotBlank
   private String password;
   @Email
   private String email;
   @Pattern(regexp = "^[0-9]{10}$")
   private String phoneNumber;
   @Past
   private LocalDate dob;
   @Pattern(regexp = "^[A-Z0-9]{10}$")
   private String panNumber;
   @Pattern(regexp = "^[0-9]{12}$")
   private String aadharNumber;
}
