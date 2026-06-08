package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.UserClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "user-service", path = "/api/v1/users", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping("/me")
    ApiResponse<Map<String, Object>> getProfile(@RequestHeader("X-User-Id") String userId);

    @PutMapping("/me")
    ApiResponse<Map<String, Object>> updateProfile(@RequestHeader("X-User-Id") String userId,
                                                    @RequestBody Map<String, Object> request);
}
