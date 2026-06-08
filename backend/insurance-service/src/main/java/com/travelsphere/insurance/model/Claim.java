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
@Table(name = "claims", schema = "insurance_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Claim {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "claim_number", unique = true)
    private String claimNumber;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "claim_type")
    private String claimType;

    @Column(name = "incident_date")
    private LocalDate incidentDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "claim_amount")
    private BigDecimal claimAmount;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    private String status;

    @Column(name = "filed_at")
    private LocalDateTime filedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "assigned_agent_id")
    private UUID assignedAgentId;

    @PrePersist
    protected void onCreate() {
        filedAt = LocalDateTime.now();
    }
}
