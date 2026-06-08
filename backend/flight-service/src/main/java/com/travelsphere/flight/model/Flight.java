package com.travelsphere.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flights", schema = "flight_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Flight {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_number")
    private String flightNumber;

    private String airline;

    @Column(name = "origin_airport_id")
    private UUID originAirportId;

    @Column(name = "destination_airport_id")
    private UUID destinationAirportId;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "aircraft_type")
    private String aircraftType;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "available_seats")
    private int availableSeats;

    private String status;
}
