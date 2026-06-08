package com.travelsphere.car.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBookingRequest {
    @NotNull
    private UUID vehicleId;

    @NotNull
    private LocalDate pickupDate;

    @NotNull
    private LocalDate returnDate;

    @NotNull
    private String pickupCity;

    private String[] addons;
}
