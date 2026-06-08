package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.TransportClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "transport-service", path = "/api/v1/transport", fallbackFactory = TransportClientFallbackFactory.class)
public interface TransportClient {

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getRoute(@PathVariable("id") String id);

    @GetMapping("/booking/{ref}")
    ApiResponse<Map<String, Object>> getBooking(@PathVariable("ref") String ref);

    @PostMapping("/book")
    ApiResponse<Map<String, Object>> bookTransport(@RequestBody Map<String, Object> request);

    @PutMapping("/cancel/{ref}")
    ApiResponse<Map<String, Object>> cancelBooking(@PathVariable("ref") String ref);
}
