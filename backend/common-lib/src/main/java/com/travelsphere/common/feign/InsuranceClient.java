package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.InsuranceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "insurance-service", path = "/api/v1/insurance", fallbackFactory = InsuranceClientFallbackFactory.class)
public interface InsuranceClient {

    @GetMapping("/policies")
    ApiResponse<Object> getPolicies();

    @PostMapping("/calculate")
    ApiResponse<Map<String, Object>> calculatePremium(@RequestBody Map<String, Object> request);

    @PostMapping("/purchase")
    ApiResponse<Map<String, Object>> purchasePolicy(@RequestBody Map<String, Object> request,
                                                     @RequestHeader(value = "X-User-Id", required = false) String userId);

    @GetMapping("/claims/{id}")
    ApiResponse<Map<String, Object>> getClaim(@PathVariable("id") String id);
}
