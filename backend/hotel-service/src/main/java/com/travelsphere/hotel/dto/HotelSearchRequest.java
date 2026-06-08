package com.travelsphere.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HotelSearchRequest {
    private String city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;
    private Integer minStars;
    private Double maxPricePerNight;
    private String sortBy;
}
