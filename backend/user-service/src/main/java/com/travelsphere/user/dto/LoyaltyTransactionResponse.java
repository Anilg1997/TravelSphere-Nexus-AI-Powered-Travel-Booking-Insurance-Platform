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
public class LoyaltyTransactionResponse {
    private UUID id;
    private int points;
    private String type;
    private String description;
    private String referenceId;
    private LocalDateTime createdAt;
}
