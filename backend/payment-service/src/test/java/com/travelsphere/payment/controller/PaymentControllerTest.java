package com.travelsphere.payment.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.payment.dto.*;
import com.travelsphere.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock private PaymentService paymentService;
    @InjectMocks private PaymentController paymentController;

    @Test
    void initiatePaymentReturnsPaymentResponse() {
        PaymentResponse response = PaymentResponse.builder()
                .paymentRef("TS-PAY-123").status("PENDING")
                .amount(new BigDecimal("4500")).finalAmount(new BigDecimal("4500")).build();
        when(paymentService.initiatePayment(any(), any())).thenReturn(response);

        PaymentInitRequest request = PaymentInitRequest.builder()
                .bookingRef("TS-FL-123").serviceType("FLIGHT")
                .amount(new BigDecimal("4500")).paymentMethod("CARD").build();

        ResponseEntity<ApiResponse<PaymentResponse>> result =
                paymentController.initiatePayment(request, UUID.randomUUID().toString());

        assertEquals(200, result.getStatusCode().value());
        assertEquals("PENDING", result.getBody().getData().getStatus());
    }

    @Test
    void confirmPaymentReturnsConfirmed() {
        PaymentResponse response = PaymentResponse.builder()
                .paymentRef("TS-PAY-123").status("COMPLETED").build();
        when(paymentService.confirmPayment("TS-PAY-123")).thenReturn(response);

        ResponseEntity<ApiResponse<PaymentResponse>> result =
                paymentController.confirmPayment("TS-PAY-123");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("COMPLETED", result.getBody().getData().getStatus());
    }

    @Test
    void getWalletBalanceReturnsBalance() {
        WalletResponse wallet = WalletResponse.builder()
                .userId(UUID.randomUUID().toString())
                .balance(new BigDecimal("5000")).currency("INR").isActive(true).build();
        when(paymentService.getWalletBalance(any())).thenReturn(wallet);

        ResponseEntity<ApiResponse<WalletResponse>> result =
                paymentController.getWalletBalance(UUID.randomUUID());

        assertEquals(200, result.getStatusCode().value());
        assertEquals(new BigDecimal("5000"), result.getBody().getData().getBalance());
    }

    @Test
    void topUpWalletReturnsUpdatedBalance() {
        WalletResponse wallet = WalletResponse.builder()
                .userId(UUID.randomUUID().toString())
                .balance(new BigDecimal("7000")).currency("INR").isActive(true).build();
        when(paymentService.topUpWallet(any(), any())).thenReturn(wallet);

        TopUpRequest request = TopUpRequest.builder().amount(new BigDecimal("2000")).build();

        ResponseEntity<ApiResponse<WalletResponse>> result =
                paymentController.topUpWallet(UUID.randomUUID(), request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Wallet topped up successfully", result.getBody().getMessage());
    }

    @Test
    void processRefundReturnsRefund() {
        RefundResponse refund = RefundResponse.builder()
                .refundRef("TS-REF-123").status("COMPLETED")
                .amount(new BigDecimal("4500")).build();
        when(paymentService.processRefund(any())).thenReturn(refund);

        RefundRequest request = RefundRequest.builder()
                .paymentRef("TS-PAY-123").reason("Change of plans").build();

        ResponseEntity<ApiResponse<RefundResponse>> result =
                paymentController.processRefund(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("COMPLETED", result.getBody().getData().getStatus());
    }
}
