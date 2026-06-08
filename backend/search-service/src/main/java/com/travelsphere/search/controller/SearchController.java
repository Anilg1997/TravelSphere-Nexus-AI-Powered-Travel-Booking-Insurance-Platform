package com.travelsphere.search.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.search.dto.*;
import com.travelsphere.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Full-text search across all travel domains")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "Search across all domains")
    public ResponseEntity<ApiResponse<SearchResponse>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchQueryRequest request = SearchQueryRequest.builder()
                .query(q).entityType(type).city(city).category(category)
                .page(page).size(size).build();
        SearchResponse response = searchService.search(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Search completed"));
    }

    @PostMapping("/index")
    @Operation(summary = "Index a document for search")
    public ResponseEntity<ApiResponse<Void>> indexDocument(@Valid @RequestBody IndexDocumentRequest request) {
        searchService.indexDocument(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Document indexed successfully"));
    }

    @DeleteMapping("/{entityType}/{entityId}")
    @Operation(summary = "Remove a document from search index")
    public ResponseEntity<ApiResponse<Void>> removeDocument(
            @PathVariable String entityType, @PathVariable String entityId) {
        searchService.removeDocument(entityType, entityId);
        return ResponseEntity.ok(ApiResponse.success(null, "Document removed from index"));
    }
}
