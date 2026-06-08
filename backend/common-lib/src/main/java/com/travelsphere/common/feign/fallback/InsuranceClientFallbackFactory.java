package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.InsuranceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InsuranceClientFallbackFactory implements FallbackFactory<InsuranceClient> {

    @Override
    public InsuranceClient create(Throwable cause) {
        log.error("Insurance service is unavailable. Cause: {}", cause.getMessage());
        return new InsuranceClient() {
            @Override
            public ApiResponse<Object> getPolicies() {
                log.warn("Fallback: getPolicies - insurance service unavailable");
                return ApiResponse.error("Insurance service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> calculatePremium(Map<String, Object> request) {
                log.warn("Fallback: calculatePremium - insurance service unavailable");
                return ApiResponse.error("Premium calculation is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> purchasePolicy(Map<String, Object> request, String userId) {
                log.warn("Fallback: purchasePolicy - insurance service unavailable");
                return ApiResponse.error("Insurance purchase is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getClaim(String id) {
                log.warn("Fallback: getClaim({}) - insurance service unavailable", id);
                return ApiResponse.error("Insurance claim service is currently unavailable.");
            }
        };
    }
}
