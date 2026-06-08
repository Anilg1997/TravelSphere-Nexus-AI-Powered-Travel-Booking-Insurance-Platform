package com.travelsphere.car.model;

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
@Table(name = "vehicle_bookings", schema = "car_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_ref", unique = true)
    private String bookingRef;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "vehicle_id")
    private UUID vehicleId;

    @Column(name = "pickup_date")
    private LocalDate pickupDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "pickup_city")
    private String pickupCity;

    @Column(name = "total_days")
    private int totalDays;

    @Column(name = "daily_rate")
    private BigDecimal dailyRate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "addons", columnDefinition = "TEXT[]")
    private String[] addons;

    private String status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;
}
