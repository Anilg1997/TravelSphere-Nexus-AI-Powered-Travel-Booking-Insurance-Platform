package com.travelsphere.car.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vehicles", schema = "car_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "vehicle_name")
    private String vehicleName;

    private String brand;

    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "vehicle_type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String color;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "seating_capacity")
    private int seatingCapacity;

    @Column(name = "daily_rate")
    private BigDecimal dailyRate;

    @Column(name = "city")
    private String city;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "image_s3_key")
    private String imageS3Key;

    public enum VehicleType {
        SEDAN, SUV, HATCHBACK, LUXURY, VAN
    }
}
