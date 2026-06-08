package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.AiAgentClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ai-agent-service", path = "/api/v1/ai", fallbackFactory = AiAgentClientFallbackFactory.class)
public interface AiAgentClient {

    @PostMapping("/chat")
    ApiResponse<Map<String, Object>> chat(@RequestBody Map<String, Object> request);

    @PostMapping("/plan-trip")
    ApiResponse<Map<String, Object>> planTrip(@RequestBody Map<String, Object> request);

    @GetMapping("/recommendations")
    ApiResponse<Object> getRecommendations(@RequestHeader(value = "X-User-Id", required = false) String userId);
}
