package com.travelsphere.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PurchaseResponse {
    private String policyNumber;
    private String policyTypeName;
    private String tripDestination;
    private LocalDate tripStart;
    private LocalDate tripEnd;
    private BigDecimal insuredAmount;
    private BigDecimal premiumPaid;
    private String status;
    private LocalDateTime issuedAt;
}
