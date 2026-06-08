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
@Table(name = "transport_bookings", schema = "transport_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_ref", unique = true)
    private String bookingRef;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "route_id")
    private UUID routeId;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "passenger_email")
    private String passengerEmail;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "price_paid")
    private BigDecimal pricePaid;

    private String status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    private String pnr;
}
