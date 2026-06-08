package com.travelsphere.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportSearchResponse {
    private UUID id;
    private String routeName;
    private String operator;
    private String transportType;
    private String originCity;
    private String destinationCity;
    private String originStation;
    private String destinationStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int durationMinutes;
    private BigDecimal price;
    private int availableSeats;
}
