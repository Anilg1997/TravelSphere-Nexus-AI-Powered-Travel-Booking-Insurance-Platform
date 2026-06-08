package com.travelsphere.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripPlanRequest {
    @NotBlank
    private String destination;

    private int durationDays;

    private double budget;

    private int travelers;

    private String preferences;
}
