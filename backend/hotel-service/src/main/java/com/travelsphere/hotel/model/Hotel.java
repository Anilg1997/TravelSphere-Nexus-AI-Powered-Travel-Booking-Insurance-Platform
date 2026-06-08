package com.travelsphere.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "hotels", schema = "hotel_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Hotel {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "star_rating")
    private int starRating;

    private String address;
    private String city;
    private String country;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "image_s3_keys", columnDefinition = "TEXT[]")
    private String[] imageS3Keys;
}
