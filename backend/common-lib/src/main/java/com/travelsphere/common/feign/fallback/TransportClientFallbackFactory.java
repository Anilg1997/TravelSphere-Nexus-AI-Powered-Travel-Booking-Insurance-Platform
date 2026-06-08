package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class TransportClientFallbackFactory implements FallbackFactory<TransportClient> {

    @Override
    public TransportClient create(Throwable cause) {
        log.error("Transport service is unavailable. Cause: {}", cause.getMessage());
        return new TransportClient() {
            @Override
            public ApiResponse<Map<String, Object>> getRoute(String id) {
                log.warn("Fallback: getRoute({}) - transport service unavailable", id);
                return ApiResponse.error("Transport service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getBooking(String ref) {
                log.warn("Fallback: getBooking({}) - transport service unavailable", ref);
                return ApiResponse.error("Transport booking service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> bookTransport(Map<String, Object> request) {
                log.warn("Fallback: bookTransport - transport service unavailable");
                return ApiResponse.error("Transport booking service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> cancelBooking(String ref) {
                log.warn("Fallback: cancelBooking({}) - transport service unavailable", ref);
                return ApiResponse.error("Transport cancellation service is currently unavailable.");
            }
        };
    }
}
