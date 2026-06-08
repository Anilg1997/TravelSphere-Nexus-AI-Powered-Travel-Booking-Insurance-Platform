package com.travelsphere.insurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "policy_types", schema = "insurance_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PolicyType {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "coverage_type", columnDefinition = "TEXT[]")
    private String[] coverageType;

    @Column(name = "base_premium")
    private BigDecimal basePremium;

    @Column(name = "max_coverage")
    private BigDecimal maxCoverage;

    @Column(name = "is_active")
    private boolean isActive;
}
