package com.travelsphere.package_.model;

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
@Table(name = "package_bookings", schema = "package_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_ref", unique = true)
    private String bookingRef;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "travel_date")
    private LocalDate travelDate;

    @Column(name = "group_size")
    private int groupSize;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String status;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
}
