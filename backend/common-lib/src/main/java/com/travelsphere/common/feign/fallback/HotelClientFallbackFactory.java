package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.HotelClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HotelClientFallbackFactory implements FallbackFactory<HotelClient> {

    @Override
    public HotelClient create(Throwable cause) {
        log.error("Hotel service is unavailable. Cause: {}", cause.getMessage());
        return new HotelClient() {
            @Override
            public ApiResponse<Map<String, Object>> getHotel(String id) {
                log.warn("Fallback: getHotel({}) - service unavailable", id);
                return ApiResponse.error("Hotel service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getBooking(String ref) {
                log.warn("Fallback: getBooking({}) - service unavailable", ref);
                return ApiResponse.error("Hotel booking service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> bookHotel(Map<String, Object> request, String userId) {
                log.warn("Fallback: bookHotel - hotel service unavailable");
                return ApiResponse.error("Hotel booking service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> cancelBooking(String ref) {
                log.warn("Fallback: cancelBooking({}) - hotel service unavailable", ref);
                return ApiResponse.error("Hotel cancellation service is currently unavailable.");
            }
        };
    }
}
