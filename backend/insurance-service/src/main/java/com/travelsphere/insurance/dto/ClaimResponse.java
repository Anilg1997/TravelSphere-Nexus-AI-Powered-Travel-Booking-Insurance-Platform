package com.travelsphere.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClaimResponse {
    private UUID id;
    private String claimNumber;
    private String policyNumber;
    private String claimType;
    private LocalDate incidentDate;
    private String description;
    private BigDecimal claimAmount;
    private BigDecimal approvedAmount;
    private String status;
    private LocalDateTime filedAt;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private List<String> documents;
}
