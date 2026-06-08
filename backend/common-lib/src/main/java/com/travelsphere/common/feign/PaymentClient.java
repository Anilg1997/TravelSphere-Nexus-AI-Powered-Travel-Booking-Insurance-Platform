package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.PaymentClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "payment-service", path = "/api/v1/payments", fallbackFactory = PaymentClientFallbackFactory.class)
public interface PaymentClient {

    @PostMapping("/initiate")
    ApiResponse<Map<String, Object>> initiatePayment(@RequestBody Map<String, Object> request,
                                                      @RequestHeader(value = "X-User-Id", required = false) String userId);

    @PostMapping("/confirm/{ref}")
    ApiResponse<Map<String, Object>> confirmPayment(@PathVariable("ref") String ref);

    @PostMapping("/refund")
    ApiResponse<Map<String, Object>> processRefund(@RequestBody Map<String, Object> request);
}
