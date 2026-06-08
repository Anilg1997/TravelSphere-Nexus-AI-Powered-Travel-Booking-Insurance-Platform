package com.travelsphere.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDocumentRequest {
    @NotBlank
    private String entityType;

    @NotBlank
    private UUID entityId;

    @NotBlank
    private String title;

    private String description;

    private String city;

    private String country;

    private String category;

    private String[] tags;

    private double rating;

    private double price;
}
