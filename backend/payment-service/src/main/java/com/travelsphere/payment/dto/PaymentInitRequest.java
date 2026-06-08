package com.travelsphere.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitRequest {
    @NotBlank
    private String bookingRef;

    @NotBlank
    private String serviceType;

    @NotNull
    private BigDecimal amount;

    private String currency;

    @NotBlank
    private String paymentMethod;

    private String promoCode;
}
