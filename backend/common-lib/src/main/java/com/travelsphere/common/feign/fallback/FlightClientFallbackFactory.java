package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.FlightClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class FlightClientFallbackFactory implements FallbackFactory<FlightClient> {

    @Override
    public FlightClient create(Throwable cause) {
        log.error("Flight service is unavailable. Cause: {}", cause.getMessage());
        return new FlightClient() {
            @Override
            public ApiResponse<Map<String, Object>> getFlight(String id) {
                log.warn("Fallback: getFlight({}) - service unavailable", id);
                return ApiResponse.error("Flight service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getBooking(String ref) {
                log.warn("Fallback: getBooking({}) - service unavailable", ref);
                return ApiResponse.error("Flight booking service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> bookFlight(Map<String, Object> request) {
                log.warn("Fallback: bookFlight - flight service unavailable");
                return ApiResponse.error("Flight booking service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> cancelBooking(String ref) {
                log.warn("Fallback: cancelBooking({}) - flight service unavailable", ref);
                return ApiResponse.error("Flight cancellation service is currently unavailable.");
            }
        };
    }
}
