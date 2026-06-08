package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.AiAgentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AiAgentClientFallbackFactory implements FallbackFactory<AiAgentClient> {

    @Override
    public AiAgentClient create(Throwable cause) {
        log.error("AI agent service is unavailable. Cause: {}", cause.getMessage());
        return new AiAgentClient() {
            @Override
            public ApiResponse<Map<String, Object>> chat(Map<String, Object> request) {
                log.warn("Fallback: chat - AI agent service unavailable");
                return ApiResponse.error("AI assistant is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> planTrip(Map<String, Object> request) {
                log.warn("Fallback: planTrip - AI agent service unavailable");
                return ApiResponse.error("AI trip planning is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Object> getRecommendations(String userId) {
                log.warn("Fallback: getRecommendations({}) - AI agent service unavailable", userId);
                return ApiResponse.error("Recommendations are currently unavailable.");
            }
        };
    }
}
