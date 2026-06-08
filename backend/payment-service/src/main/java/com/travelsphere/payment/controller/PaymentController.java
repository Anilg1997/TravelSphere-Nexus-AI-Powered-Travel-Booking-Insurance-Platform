package com.travelsphere.payment.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.payment.dto.*;
import com.travelsphere.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing, wallet, and refund APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate a payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(
            @Valid @RequestBody PaymentInitRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        PaymentResponse response = paymentService.initiatePayment(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment initiated successfully"));
    }

    @PostMapping("/confirm/{ref}")
    @Operation(summary = "Confirm a payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(@PathVariable String ref) {
        PaymentResponse response = paymentService.confirmPayment(ref);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment confirmed successfully"));
    }

    @GetMapping("/wallet/{userId}")
    @Operation(summary = "Get wallet balance")
    public ResponseEntity<ApiResponse<WalletResponse>> getWalletBalance(@PathVariable UUID userId) {
        WalletResponse response = paymentService.getWalletBalance(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/wallet/{userId}/topup")
    @Operation(summary = "Top up wallet")
    public ResponseEntity<ApiResponse<WalletResponse>> topUpWallet(
            @PathVariable UUID userId,
            @Valid @RequestBody TopUpRequest request) {
        WalletResponse response = paymentService.topUpWallet(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Wallet topped up successfully"));
    }

    @PostMapping("/refund")
    @Operation(summary = "Process a refund")
    public ResponseEntity<ApiResponse<RefundResponse>> processRefund(@Valid @RequestBody RefundRequest request) {
        RefundResponse response = paymentService.processRefund(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Refund processed successfully"));
    }
}
