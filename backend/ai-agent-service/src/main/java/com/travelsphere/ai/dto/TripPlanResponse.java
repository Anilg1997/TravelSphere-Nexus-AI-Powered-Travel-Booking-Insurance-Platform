package com.travelsphere.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripPlanResponse {
    private String destination;
    private int durationDays;
    private double budget;
    private List<String> itinerary;
    private List<String> recommendations;
    private String summary;
}
