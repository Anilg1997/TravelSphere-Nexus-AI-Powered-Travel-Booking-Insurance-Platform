package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.NotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class NotificationClientFallbackFactory implements FallbackFactory<NotificationClient> {

    @Override
    public NotificationClient create(Throwable cause) {
        log.error("Notification service is unavailable. Cause: {}", cause.getMessage());
        return new NotificationClient() {
            @Override
            public ApiResponse<Void> sendNotification(Map<String, Object> request) {
                log.warn("Fallback: sendNotification - notification service unavailable");
                return ApiResponse.error("Notification service is currently unavailable.");
            }

            @Override
            public ApiResponse<Object> getNotifications(String userId) {
                log.warn("Fallback: getNotifications({}) - notification service unavailable", userId);
                return ApiResponse.error("Notifications are currently unavailable.");
            }
        };
    }
}
