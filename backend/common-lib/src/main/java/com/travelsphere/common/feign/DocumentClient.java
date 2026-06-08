package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.DocumentClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "document-service", path = "/api/v1/documents", fallbackFactory = DocumentClientFallbackFactory.class)
public interface DocumentClient {

    @PostMapping("/generate")
    ApiResponse<Map<String, Object>> generateDocument(@RequestBody Map<String, Object> request,
                                                       @RequestHeader(value = "X-User-Id", required = false) String userId);

    @GetMapping("/{id}")
    ApiResponse<Map<String, Object>> getDocument(@PathVariable("id") String id);
}
