package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        log.error("User service is unavailable. Cause: {}", cause.getMessage());
        return new UserClient() {
            @Override
            public ApiResponse<Map<String, Object>> getProfile(String userId) {
                log.warn("Fallback: getProfile({}) - user service unavailable", userId);
                return ApiResponse.error("User service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> updateProfile(String userId, Map<String, Object> request) {
                log.warn("Fallback: updateProfile - user service unavailable");
                return ApiResponse.error("Profile update is currently unavailable. Please try again later.");
            }
        };
    }
}
