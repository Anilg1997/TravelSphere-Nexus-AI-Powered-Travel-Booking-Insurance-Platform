package com.travelsphere.payment.service;

import com.travelsphere.payment.dto.*;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiatePayment(PaymentInitRequest request, String userId);
    PaymentResponse confirmPayment(String paymentRef);
    WalletResponse getWalletBalance(UUID userId);
    WalletResponse topUpWallet(UUID userId, TopUpRequest request);
    RefundResponse processRefund(RefundRequest request);
}
