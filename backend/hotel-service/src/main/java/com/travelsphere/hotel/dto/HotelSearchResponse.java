package com.travelsphere.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HotelSearchResponse {
    private UUID id;
    private String name;
    private String description;
    private int starRating;
    private String address;
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String[] amenities;
    private String[] imageS3Keys;
    private BigDecimal minPricePerNight;
    private int availableRooms;
    private double averageRating;
    private int reviewCount;
}
