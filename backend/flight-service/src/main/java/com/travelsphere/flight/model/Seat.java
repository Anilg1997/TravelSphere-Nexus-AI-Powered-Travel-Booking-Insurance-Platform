package com.travelsphere.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "seats", schema = "flight_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_id")
    private UUID flightId;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "cabin_class")
    private String cabinClass;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "is_window")
    private boolean isWindow;

    @Column(name = "is_aisle")
    private boolean isAisle;
}
