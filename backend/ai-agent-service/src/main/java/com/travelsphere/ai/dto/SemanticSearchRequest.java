package com.travelsphere.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemanticSearchRequest {
    @NotBlank
    private String query;

    @Builder.Default
    private int topK = 5;

    private Double minScore;

    private String filter;
}
