package com.travelsphere.insurance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PremiumRequest {
    @NotNull
    private UUID policyTypeId;

    @NotBlank
    private String destination;

    @Min(1)
    private int durationDays;

    @Min(1)
    private int travellerAge;

    private String coverageType;
}
