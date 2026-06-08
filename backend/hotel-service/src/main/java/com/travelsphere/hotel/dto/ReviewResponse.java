package com.travelsphere.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private UUID hotelId;
    private UUID userId;
    private int rating;
    private String title;
    private String reviewText;
    private LocalDateTime createdAt;
}
