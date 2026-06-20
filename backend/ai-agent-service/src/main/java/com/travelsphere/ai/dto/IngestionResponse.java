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
public class IngestionResponse {
    private String source;
    private int chunksCreated;
    private boolean success;
    private String message;
    private List<String> chunkIds;
}
