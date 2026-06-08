package com.travelsphere.common.feign.fallback;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.SearchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SearchClientFallbackFactory implements FallbackFactory<SearchClient> {

    @Override
    public SearchClient create(Throwable cause) {
        log.error("Search service is unavailable. Cause: {}", cause.getMessage());
        return new SearchClient() {
            @Override
            public ApiResponse<Map<String, Object>> search(String query, String type, String city) {
                log.warn("Fallback: search({}, {}, {}) - search service unavailable", query, type, city);
                return ApiResponse.error("Search service is currently unavailable. Please try again later.");
            }

            @Override
            public ApiResponse<Void> indexDocument(Map<String, Object> request) {
                log.warn("Fallback: indexDocument - search service unavailable");
                return ApiResponse.error("Document indexing is currently unavailable.");
            }
        };
    }
}
