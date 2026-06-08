package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.PackageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class PackageClientFallbackFactory implements FallbackFactory<PackageClient> {

    @Override
    public PackageClient create(Throwable cause) {
        log.error("Package service is unavailable. Cause: {}", cause.getMessage());
        return new PackageClient() {
            @Override
            public ApiResponse<Map<String, Object>> getPackage(String id) {
                log.warn("Fallback: getPackage({}) - package service unavailable", id);
                return ApiResponse.error("Package service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getBooking(String ref) {
                log.warn("Fallback: getBooking({}) - package service unavailable", ref);
                return ApiResponse.error("Package booking service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> bookPackage(Map<String, Object> request, String userId) {
                log.warn("Fallback: bookPackage - package service unavailable");
                return ApiResponse.error("Package booking service is currently unavailable. Please try again later.");
            }
        };
    }
}
