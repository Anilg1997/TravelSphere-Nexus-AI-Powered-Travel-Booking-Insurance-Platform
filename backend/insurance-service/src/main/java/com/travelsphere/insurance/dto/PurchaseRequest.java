package com.travelsphere.insurance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PurchaseRequest {
    @NotNull
    private UUID policyTypeId;

    @NotBlank
    private String tripDestination;

    @NotNull
    private LocalDate tripStart;

    @NotNull
    private LocalDate tripEnd;

    @Min(1)
    private int travellerAge;

    private String bookingRef;
}
