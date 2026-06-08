package com.travelsphere.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FlightSearchRequest {
    private String origin;
    private String destination;
    private LocalDate date;
    private LocalDate returnDate;
    private String tripType;
    private String cabinClass;
    private int passengers;
}
