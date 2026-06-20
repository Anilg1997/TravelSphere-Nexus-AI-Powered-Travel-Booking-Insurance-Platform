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
public class BookingManagementResponse {
    private UUID id;
    private String bookingRef;
    private String userId;
    private String userName;
    private String userEmail;
    private String serviceType;
    private String serviceName;
    private Double amount;
    private String status;
    private String paymentStatus;
    private LocalDateTime travelDate;
    private LocalDateTime createdAt;
}
