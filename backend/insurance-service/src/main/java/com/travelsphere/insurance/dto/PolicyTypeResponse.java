package com.travelsphere.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PolicyTypeResponse {
    private UUID id;
    private String name;
    private String description;
    private String[] coverageType;
    private BigDecimal basePremium;
    private BigDecimal maxCoverage;
}
