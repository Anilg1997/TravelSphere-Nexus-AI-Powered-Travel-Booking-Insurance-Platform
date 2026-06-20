package com.travelsphere.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpToolResponse {
    private boolean success;
    private String toolName;
    private Map<String, Object> result;
    private String error;
    private long executionTimeMs;
}
