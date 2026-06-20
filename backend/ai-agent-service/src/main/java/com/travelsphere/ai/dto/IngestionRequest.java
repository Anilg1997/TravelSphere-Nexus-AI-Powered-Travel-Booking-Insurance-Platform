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
public class IngestionRequest {
    @NotBlank
    private String content;

    private String source;

    private String contentType;

    private String metadata;
}
