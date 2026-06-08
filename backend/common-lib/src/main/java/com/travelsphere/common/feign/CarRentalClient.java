package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.CarRentalClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "car-rental-service", path = "/api/v1/cars", fallbackFactory = CarRentalClientFallbackFactory.class)
public interface CarRentalClient {

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getVehicle(@PathVariable("id") String id);

    @GetMapping("/booking/{ref}")
    ApiResponse<Map<String, Object>> getBooking(@PathVariable("ref") String ref);

    @PostMapping("/book")
    ApiResponse<Map<String, Object>> bookVehicle(@RequestBody Map<String, Object> request,
                                                  @RequestHeader(value = "X-User-Id", required = false) String userId);

    @PutMapping("/cancel/{ref}")
    ApiResponse<Map<String, Object>> cancelBooking(@PathVariable("ref") String ref);
}
