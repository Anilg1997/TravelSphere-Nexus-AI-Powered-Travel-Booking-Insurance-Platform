package com.travelsphere.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentRef;
    private String bookingRef;
    private String serviceType;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private LocalDateTime createdAt;
}
