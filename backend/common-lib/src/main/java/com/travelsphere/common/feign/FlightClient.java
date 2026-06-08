package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.FlightClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "flight-service", path = "/api/v1/flights", fallbackFactory = FlightClientFallbackFactory.class)
public interface FlightClient {

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getFlight(@PathVariable("id") String id);

    @GetMapping("/booking/{ref}")
    ApiResponse<Map<String, Object>> getBooking(@PathVariable("ref") String ref);

    @PostMapping("/book")
    ApiResponse<Map<String, Object>> bookFlight(@RequestBody Map<String, Object> request);

    @PutMapping("/cancel/{ref}")
    ApiResponse<Map<String, Object>> cancelBooking(@PathVariable("ref") String ref);
}
