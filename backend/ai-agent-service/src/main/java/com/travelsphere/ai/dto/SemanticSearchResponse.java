package com.travelsphere.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticSearchResponse {
    private String query;
    private List<SearchResult> results;
    private int totalResults;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SearchResult {
        private String chunkId;
        private String content;
        private String source;
        private String metadata;
        private double score;
    }
}
