package com.travelsphere.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentManagementRequest {
    @NotBlank
    private String entityType;

    @NotBlank
    private String action;

    private Map<String, Object> data;

    private boolean published;
    private boolean featured;
    private List<String> tags;
}
