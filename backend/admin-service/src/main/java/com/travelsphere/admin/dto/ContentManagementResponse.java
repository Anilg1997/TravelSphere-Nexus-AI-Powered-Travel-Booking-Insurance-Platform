package com.travelsphere.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentManagementResponse {
    private String entityType;
    private String entityId;
    private String action;
    private boolean success;
    private Map<String, Object> data;
    private LocalDateTime processedAt;
}
