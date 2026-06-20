package com.travelsphere.admin.dto;

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
public class UserManagementResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private boolean emailVerified;
    private boolean accountLocked;
    private boolean accountEnabled;
    private String loyaltyTier;
    private Long totalBookings;
    private Double totalSpent;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
