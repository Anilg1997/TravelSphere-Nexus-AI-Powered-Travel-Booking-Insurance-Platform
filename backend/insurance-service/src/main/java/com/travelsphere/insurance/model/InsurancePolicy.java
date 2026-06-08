package com.travelsphere.insurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "insurance_policies", schema = "insurance_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InsurancePolicy {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "policy_number", unique = true)
    private String policyNumber;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "policy_type_id")
    private UUID policyTypeId;

    @Column(name = "booking_ref")
    private String bookingRef;

    @Column(name = "trip_destination")
    private String tripDestination;

    @Column(name = "trip_start")
    private LocalDate tripStart;

    @Column(name = "trip_end")
    private LocalDate tripEnd;

    @Column(name = "insured_amount")
    private BigDecimal insuredAmount;

    @Column(name = "premium_paid")
    private BigDecimal premiumPaid;

    private String status;

    @Column(name = "traveller_age")
    private int travellerAge;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "pdf_s3_key")
    private String pdfS3Key;

    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
    }
}
