package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.HotelClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "hotel-service", path = "/api/v1/hotels", fallbackFactory = HotelClientFallbackFactory.class)
public interface HotelClient {

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getHotel(@PathVariable("id") String id);

    @GetMapping("/booking/{ref}")
    ApiResponse<Map<String, Object>> getBooking(@PathVariable("ref") String ref);

    @PostMapping("/book")
    ApiResponse<Map<String, Object>> bookHotel(@RequestBody Map<String, Object> request,
                                                @RequestHeader(value = "X-User-Id", required = false) String userId);

    @PutMapping("/cancel/{ref}")
    ApiResponse<Map<String, Object>> cancelBooking(@PathVariable("ref") String ref);
}
