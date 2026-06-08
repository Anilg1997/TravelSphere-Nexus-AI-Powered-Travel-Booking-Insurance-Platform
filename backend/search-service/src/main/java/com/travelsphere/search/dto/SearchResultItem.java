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
public class SearchResultItem {
    private UUID id;
    private String entityType;
    private UUID entityId;
    private String title;
    private String description;
    private String city;
    private String country;
    private String category;
    private double rating;
    private double price;
    private double score;
}
