package com.travelsphere.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClaimRequest {
    @NotBlank
    private String policyNumber;

    @NotBlank
    private String claimType;

    @NotNull
    private LocalDate incidentDate;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal claimAmount;
}
