package com.travelsphere.hotel.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewRequest {
    private UUID hotelId;

    @Min(1) @Max(5)
    private int rating;

    @NotBlank
    private String title;

    @NotBlank
    private String reviewText;
}
