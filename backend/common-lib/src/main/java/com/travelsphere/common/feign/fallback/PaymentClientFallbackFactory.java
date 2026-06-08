package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.PaymentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class PaymentClientFallbackFactory implements FallbackFactory<PaymentClient> {

    @Override
    public PaymentClient create(Throwable cause) {
        log.error("Payment service is unavailable. Cause: {}", cause.getMessage());
        return new PaymentClient() {
            @Override
            public ApiResponse<Map<String, Object>> initiatePayment(Map<String, Object> request, String userId) {
                log.warn("Fallback: initiatePayment - payment service unavailable");
                return ApiResponse.error("Payment service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> confirmPayment(String ref) {
                log.warn("Fallback: confirmPayment({}) - payment service unavailable", ref);
                return ApiResponse.error("Payment confirmation service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> processRefund(Map<String, Object> request) {
                log.warn("Fallback: processRefund - payment service unavailable");
                return ApiResponse.error("Refund service is currently unavailable. Please try again later.");
            }
        };
    }
}
