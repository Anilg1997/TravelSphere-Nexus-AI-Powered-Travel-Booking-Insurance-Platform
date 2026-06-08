package com.travelsphere.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hotel_bookings", schema = "hotel_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HotelBooking {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_ref", unique = true)
    private String bookingRef;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "hotel_id")
    private UUID hotelId;

    @Column(name = "room_type_id")
    private UUID roomTypeId;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    private int guests;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
}
