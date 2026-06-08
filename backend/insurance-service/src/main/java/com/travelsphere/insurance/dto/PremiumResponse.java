package com.travelsphere.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PremiumResponse {
    private String policyTypeName;
    private String destination;
    private int durationDays;
    private int travellerAge;
    private BigDecimal basePremium;
    private BigDecimal calculatedPremium;
    private double destinationMultiplier;
    private double ageMultiplier;
    private double durationMultiplier;
    private BigDecimal maxCoverage;
}
