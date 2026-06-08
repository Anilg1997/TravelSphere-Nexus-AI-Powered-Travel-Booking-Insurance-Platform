package com.travelsphere.transport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transport_routes", schema = "transport_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "route_name")
    private String routeName;

    private String operator;

    @Column(name = "transport_type")
    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @Column(name = "origin_city")
    private String originCity;

    @Column(name = "destination_city")
    private String destinationCity;

    @Column(name = "origin_station")
    private String originStation;

    @Column(name = "destination_station")
    private String destinationStation;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "available_seats")
    private int availableSeats;

    private String status;

    public enum TransportType {
        BUS, TRAIN
    }
}
