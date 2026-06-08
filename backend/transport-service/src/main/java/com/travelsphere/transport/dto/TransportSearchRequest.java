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
public class TransportSearchRequest {
    private String originCity;
    private String destinationCity;
    private String transportType;
    private LocalDateTime departureDate;
    private int passengers;
}
