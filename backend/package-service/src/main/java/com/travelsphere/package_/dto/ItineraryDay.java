package com.travelsphere.package_.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDay {
    private int dayNumber;
    private String dayTitle;
    private String description;
    private String[] activities;
}
