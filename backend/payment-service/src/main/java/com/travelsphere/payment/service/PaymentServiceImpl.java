package com.travelsphere.payment.service;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.*;
import com.travelsphere.payment.dto.*;
import com.travelsphere.payment.model.*;
import com.travelsphere.payment.repository.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final RefundRepository refundRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Feign clients for booking verification
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final TransportClient transportClient;
    private final CarRentalClient carRentalClient;
    private final PackageClient packageClient;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentInitRequest request, String userId) {
        // Verify booking exists via Feign client
        verifyBookingExists(request.getBookingRef(), request.getServiceType());

        BigDecimal discount = BigDecimal.ZERO;
        String promoCode = null;

        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            PromoCode promo = promoCodeRepository.findByCodeAndIsActiveTrue(request.getPromoCode())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));

            if (promo.getUsageCount() >= promo.getMaxUsage()) {
                throw new IllegalStateException("Promo code usage limit reached");
            }
            if (request.getAmount().compareTo(promo.getMinOrderAmount()) < 0) {
                throw new IllegalArgumentException("Order amount does not meet minimum for this promo code");
            }

            discount = request.getAmount()
                    .multiply(BigDecimal.valueOf(promo.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (promo.getMaxDiscountAmount() != null && discount.compareTo(promo.getMaxDiscountAmount()) > 0) {
                discount = promo.getMaxDiscountAmount();
            }
            promoCode = promo.getCode();
            promo.setUsageCount(promo.getUsageCount() + 1);
            promoCodeRepository.save(promo);
        }

        BigDecimal finalAmount = request.getAmount().subtract(discount).max(BigDecimal.ZERO);
        String paymentRef = "TS-PAY-" + System.currentTimeMillis() % 1000000;
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        Payment payment = Payment.builder()
                .paymentRef(paymentRef)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .bookingRef(request.getBookingRef())
                .serviceType(request.getServiceType())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .paymentMethod(request.getPaymentMethod())
                .status(Payment.PaymentStatus.PENDING)
                .transactionId(transactionId)
                .promoCode(promoCode)
                .discountAmount(discount)
                .finalAmount(finalAmount)
                .build();

        payment = paymentRepository.save(payment);
        return toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse confirmPayment(String paymentRef) {
        Payment payment = paymentRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment is not in PENDING status");
        }

        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment = paymentRepository.save(payment);

        kafkaTemplate.send("ts.payments.processed", paymentRef, payment);

        if ("WALLET".equalsIgnoreCase(payment.getPaymentMethod()) && payment.getUserId() != null) {
            Wallet wallet = walletRepository.findByUserId(payment.getUserId()).orElse(null);
            if (wallet != null) {
                wallet.setBalance(wallet.getBalance().subtract(payment.getFinalAmount()));
                walletRepository.save(wallet);
            }
        }

        return toPaymentResponse(payment);
    }

    @Override
    public WalletResponse getWalletBalance(UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        return toWalletResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponse topUpWallet(UUID userId, TopUpRequest request) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> Wallet.builder()
                        .userId(userId)
                        .balance(BigDecimal.ZERO)
                        .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                        .isActive(true)
                        .build());

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        wallet = walletRepository.save(wallet);
        return toWalletResponse(wallet);
    }

    @Override
    @Transactional
    public RefundResponse processRefund(RefundRequest request) {
        Payment payment = paymentRepository.findByPaymentRef(request.getPaymentRef())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded");
        }

        String refundRef = "TS-REF-" + System.currentTimeMillis() % 1000000;

        Refund refund = Refund.builder()
                .refundRef(refundRef)
                .paymentId(payment.getId())
                .amount(payment.getFinalAmount())
                .reason(request.getReason())
                .status(Refund.RefundStatus.COMPLETED)
                .build();

        refundRepository.save(refund);

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        if (payment.getUserId() != null) {
            Wallet wallet = walletRepository.findByUserId(payment.getUserId())
                    .orElseGet(() -> Wallet.builder()
                            .userId(payment.getUserId())
                            .balance(BigDecimal.ZERO)
                            .currency("INR")
                            .isActive(true)
                            .build());
            wallet.setBalance(wallet.getBalance().add(payment.getFinalAmount()));
            walletRepository.save(wallet);
        }

        kafkaTemplate.send("ts.payments.refunded", refundRef, refund);

        return RefundResponse.builder()
                .refundRef(refundRef)
                .paymentRef(payment.getPaymentRef())
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(refund.getStatus().name())
                .createdAt(refund.getCreatedAt())
                .build();
    }

    @CircuitBreaker(name = "booking-verification", fallbackMethod = "verifyBookingFallback")
    void verifyBookingExists(String bookingRef, String serviceType) {
        ApiResponse<Map<String, Object>> response = switch (serviceType.toUpperCase()) {
            case "FLIGHT" -> flightClient.getBooking(bookingRef);
            case "HOTEL" -> hotelClient.getBooking(bookingRef);
            case "TRANSPORT" -> transportClient.getBooking(bookingRef);
            case "CAR_RENTAL" -> carRentalClient.getBooking(bookingRef);
            case "PACKAGE" -> packageClient.getBooking(bookingRef);
            default -> {
                log.warn("Unknown service type for booking verification: {}", serviceType);
                yield null;
            }
        };

        if (response == null || !response.isSuccess()) {
            log.warn("Could not verify booking {} for service type {} - proceeding anyway", bookingRef, serviceType);
        }
    }

    @SuppressWarnings("unused")
    void verifyBookingFallback(String bookingRef, String serviceType, Throwable t) {
        log.warn("Circuit breaker triggered for booking verification of {} ({}): {} - proceeding with payment",
                bookingRef, serviceType, t.getMessage());
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentRef(payment.getPaymentRef())
                .bookingRef(payment.getBookingRef())
                .serviceType(payment.getServiceType())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus().name())
                .transactionId(payment.getTransactionId())
                .discountAmount(payment.getDiscountAmount())
                .finalAmount(payment.getFinalAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .userId(wallet.getUserId().toString())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .isActive(wallet.isActive())
                .lastUpdated(wallet.getUpdatedAt())
                .build();
    }
}
