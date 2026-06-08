package com.travelsphere.car.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchResponse {
    private UUID id;
    private String vehicleName;
    private String brand;
    private String model;
    private int year;
    private String vehicleType;
    private String color;
    private String fuelType;
    private String transmission;
    private int seatingCapacity;
    private BigDecimal dailyRate;
    private String city;
    private boolean isAvailable;
    private String imageS3Key;
}
