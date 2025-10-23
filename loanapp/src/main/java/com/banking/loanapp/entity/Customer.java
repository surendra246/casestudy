package com.banking.loanapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "customer_id")
   private Long customerId;
   @NotBlank
   @Size(max = 100)
   @Column(name = "full_name", nullable = false, length = 100)
   private String fullName;
   @NotBlank
   @Size(max = 15)
   @Column(nullable = false, length = 15)
   private String password;
   @Email
   @Size(max = 100)
   @Column(nullable = false, unique = true, length = 100)
   private String email;
   @NotBlank
   @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
   @Column(name = "phone_number", nullable = false, length = 10)
   private String phoneNumber;
   @Past
   @Column(nullable = false)
   private LocalDate dob;
   @NotBlank
   @Pattern(regexp = "^[A-Z0-9]{10}$", message = "PAN must be 10 alphanumeric characters")
   @Column(name = "pan_number", nullable = false, unique = true, length = 10)
   private String panNumber;
   @NotBlank
   @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar must be 12 digits")
   @Column(name = "aadhar_number", nullable = false, unique = true, length = 12)
   private String aadharNumber;
   @Column(name = "created_at")
   private LocalDateTime createdAt = LocalDateTime.now();
}
