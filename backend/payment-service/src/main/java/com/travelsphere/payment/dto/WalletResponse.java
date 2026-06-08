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
public class WalletResponse {
    private String userId;
    private BigDecimal balance;
    private String currency;
    private boolean isActive;
    private LocalDateTime lastUpdated;
}
