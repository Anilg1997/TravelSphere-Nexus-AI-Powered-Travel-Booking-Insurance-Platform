package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.AdminClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "admin-service", path = "/api/v1/admin", fallbackFactory = AdminClientFallbackFactory.class)
public interface AdminClient {

    @GetMapping("/dashboard")
    ApiResponse<Map<String, Object>> getDashboard();

    @GetMapping("/fraud-alerts")
    ApiResponse<Object> getFraudAlerts(@RequestParam(value = "status", required = false) String status);
}
