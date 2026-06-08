package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.CarRentalClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class CarRentalClientFallbackFactory implements FallbackFactory<CarRentalClient> {

    @Override
    public CarRentalClient create(Throwable cause) {
        log.error("Car rental service is unavailable. Cause: {}", cause.getMessage());
        return new CarRentalClient() {
            @Override
            public ApiResponse<Map<String, Object>> getVehicle(String id) {
                log.warn("Fallback: getVehicle({}) - car rental service unavailable", id);
                return ApiResponse.error("Car rental service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getBooking(String ref) {
                log.warn("Fallback: getBooking({}) - car rental service unavailable", ref);
                return ApiResponse.error("Car rental booking service is currently unavailable.");
            }

            @Override
            public ApiResponse<Map<String, Object>> bookVehicle(Map<String, Object> request, String userId) {
                log.warn("Fallback: bookVehicle - car rental service unavailable");
                return ApiResponse.error("Car rental booking service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> cancelBooking(String ref) {
                log.warn("Fallback: cancelBooking({}) - car rental service unavailable", ref);
                return ApiResponse.error("Car rental cancellation service is currently unavailable.");
            }
        };
    }
}
