package com.travelsphere.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refunds", schema = "payment_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "refund_ref", unique = true)
    private String refundRef;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum RefundStatus {
        PENDING, COMPLETED, REJECTED
    }
}
