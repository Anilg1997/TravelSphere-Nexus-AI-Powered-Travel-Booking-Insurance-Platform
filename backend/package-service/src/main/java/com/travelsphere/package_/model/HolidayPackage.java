package com.travelsphere.package_.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "holiday_packages", schema = "package_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "package_name")
    private String packageName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String destination;

    @Column(name = "duration_days")
    private int durationDays;

    @Column(name = "duration_nights")
    private int durationNights;

    @Column(name = "price_per_person")
    private BigDecimal pricePerPerson;

    @Column(name = "max_group_size")
    private int maxGroupSize;

    @Column(name = "included_services", columnDefinition = "TEXT[]")
    private String[] includedServices;

    @Column(name = "image_s3_key")
    private String imageS3Key;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "rating")
    private double rating;
}
