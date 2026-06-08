package com.travelsphere.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fraud_alerts", schema = "admin_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "alert_type")
    private String alertType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
    public enum AlertStatus { OPEN, INVESTIGATING, RESOLVED, DISMISSED }
}
