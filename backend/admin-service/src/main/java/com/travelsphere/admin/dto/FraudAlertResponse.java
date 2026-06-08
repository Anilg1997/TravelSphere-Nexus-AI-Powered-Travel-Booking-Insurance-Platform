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
public class FraudAlertResponse {
    private UUID id;
    private UUID userId;
    private String alertType;
    private String description;
    private String severity;
    private String status;
    private String referenceId;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
