package com.travelsphere.common.feign;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.common.feign.fallback.SearchClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "search-service", path = "/api/v1/search", fallbackFactory = SearchClientFallbackFactory.class)
public interface SearchClient {

    @GetMapping
    ApiResponse<Map<String, Object>> search(@RequestParam(value = "q", required = false) String query,
                                             @RequestParam(value = "type", required = false) String type,
                                             @RequestParam(value = "city", required = false) String city);

    @PostMapping("/index")
    ApiResponse<Void> indexDocument(@RequestBody Map<String, Object> request);
}
