package com.travelsphere.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQueryRequest {
    private String query;
    private String entityType;
    private String city;
    private String category;
    private int page;
    private int size;
}
