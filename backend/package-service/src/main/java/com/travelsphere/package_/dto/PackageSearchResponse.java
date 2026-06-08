package com.travelsphere.package_.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageSearchResponse {
    private UUID id;
    private String packageName;
    private String description;
    private String destination;
    private int durationDays;
    private int durationNights;
    private BigDecimal pricePerPerson;
    private int maxGroupSize;
    private String[] includedServices;
    private double rating;
    private List<ItineraryDay> itinerary;
}
