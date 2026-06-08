package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.NotificationClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "notification-service", path = "/api/v1/notifications", fallbackFactory = NotificationClientFallbackFactory.class)
public interface NotificationClient {

    @PostMapping("/send")
    ApiResponse<Void> sendNotification(@RequestBody Map<String, Object> request);

    @GetMapping
    ApiResponse<Object> getNotifications(@RequestHeader("X-User-Id") String userId);
}
