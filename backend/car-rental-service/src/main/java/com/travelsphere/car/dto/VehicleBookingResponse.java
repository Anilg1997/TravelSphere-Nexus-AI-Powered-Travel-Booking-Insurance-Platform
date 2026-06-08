package com.travelsphere.car.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBookingResponse {
    private String bookingRef;
    private String vehicleName;
    private String brand;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String pickupCity;
    private int totalDays;
    private BigDecimal totalPrice;
    private String[] addons;
    private String status;
    private LocalDateTime bookedAt;
}
