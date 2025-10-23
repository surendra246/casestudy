package com.banking.loanapp.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "property_id")
   private Long propertyId;
   @ManyToOne
   @JoinColumn(name = "customer_id", nullable = false)
   private Customer customer;
   @NotBlank
   @Size(max = 50)
   @Column(name = "property_type", nullable = false)
   private String propertyType;
   @NotBlank
   @Column(nullable = false)
   private String address;
   @DecimalMin(value = "0.0", inclusive = false)
   @Digits(integer = 12, fraction = 2)
   @Column(name = "market_value", precision = 12, scale = 2, nullable = false)
   private BigDecimal marketValue;
}
