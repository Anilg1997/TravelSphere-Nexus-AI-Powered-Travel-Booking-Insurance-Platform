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
public class RefundResponse {
    private String refundRef;
    private String paymentRef;
    private BigDecimal amount;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
}
