package com.travelsphere.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hotel_reviews", schema = "hotel_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HotelReview {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hotel_id")
    private UUID hotelId;

    @Column(name = "user_id")
    private UUID userId;

    private int rating;

    private String title;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
