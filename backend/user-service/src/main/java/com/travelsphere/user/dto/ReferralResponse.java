package com.travelsphere.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralResponse {
    private UUID id;
    private String referredEmail;
    private String referralCode;
    private String status;
    private int bonusPointsAwarded;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
