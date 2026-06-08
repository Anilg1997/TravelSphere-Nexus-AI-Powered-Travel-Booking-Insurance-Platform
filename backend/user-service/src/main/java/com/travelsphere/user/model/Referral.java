package com.travelsphere.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "referrals", schema = "user_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "referrer_user_id", nullable = false)
    private UUID referrerUserId;

    @Column(name = "referred_email", nullable = false)
    private String referredEmail;

    @Column(name = "referral_code", nullable = false)
    private String referralCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReferralStatus status = ReferralStatus.PENDING;

    @Column(name = "referred_user_id")
    private UUID referredUserId;

    @Column(name = "bonus_points_awarded")
    @Builder.Default
    private int bonusPointsAwarded = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum ReferralStatus {
        PENDING, REGISTERED, COMPLETED
    }
}
