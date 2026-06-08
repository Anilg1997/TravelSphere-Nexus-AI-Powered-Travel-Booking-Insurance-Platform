package com.travelsphere.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles", schema = "user_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    private String address;

    private String city;

    private String country;

    @Column(name = "profile_image_s3_key")
    private String profileImageS3Key;

    @Column(name = "loyalty_points")
    @Builder.Default
    private int loyaltyPoints = 0;

    @Column(name = "loyalty_tier")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LoyaltyTier loyaltyTier = LoyaltyTier.SILVER;

    @Column(name = "total_trips")
    @Builder.Default
    private int totalTrips = 0;

    @Column(name = "total_spent")
    @Builder.Default
    private double totalSpent = 0.0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LoyaltyTier {
        SILVER, GOLD, PLATINUM, DIAMOND
    }
}
