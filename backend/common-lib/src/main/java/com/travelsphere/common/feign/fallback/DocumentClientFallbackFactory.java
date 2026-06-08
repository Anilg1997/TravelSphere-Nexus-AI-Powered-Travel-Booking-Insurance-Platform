package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.DocumentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class DocumentClientFallbackFactory implements FallbackFactory<DocumentClient> {

    @Override
    public DocumentClient create(Throwable cause) {
        log.error("Document service is unavailable. Cause: {}", cause.getMessage());
        return new DocumentClient() {
            @Override
            public ApiResponse<Map<String, Object>> generateDocument(Map<String, Object> request, String userId) {
                log.warn("Fallback: generateDocument - document service unavailable");
                return ApiResponse.error("Document generation service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Map<String, Object>> getDocument(String id) {
                log.warn("Fallback: getDocument({}) - document service unavailable", id);
                return ApiResponse.error("Document retrieval is currently unavailable.");
            }
        };
    }
}
