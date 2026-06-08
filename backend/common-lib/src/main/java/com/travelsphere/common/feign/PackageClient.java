package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.PackageClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "package-service", path = "/api/v1/packages", fallbackFactory = PackageClientFallbackFactory.class)
public interface PackageClient {

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getPackage(@PathVariable("id") String id);

    @GetMapping("/booking/{ref}")
    ApiResponse<Map<String, Object>> getBooking(@PathVariable("ref") String ref);

    @PostMapping("/book")
    ApiResponse<Map<String, Object>> bookPackage(@RequestBody Map<String, Object> request,
                                                  @RequestHeader(value = "X-User-Id", required = false) String userId);
}
