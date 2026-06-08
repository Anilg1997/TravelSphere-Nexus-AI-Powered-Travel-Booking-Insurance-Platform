package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.AdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AdminClientFallbackFactory implements FallbackFactory<AdminClient> {

    @Override
    public AdminClient create(Throwable cause) {
        log.error("Admin service is unavailable. Cause: {}", cause.getMessage());
        return new AdminClient() {
            @Override
            public ApiResponse<Map<String, Object>> getDashboard() {
                log.warn("Fallback: getDashboard - admin service unavailable");
                return ApiResponse.error("Admin dashboard is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Object> getFraudAlerts(String status) {
                log.warn("Fallback: getFraudAlerts({}) - admin service unavailable", status);
                return ApiResponse.error("Fraud alerts are currently unavailable.");
            }
        };
    }
}
