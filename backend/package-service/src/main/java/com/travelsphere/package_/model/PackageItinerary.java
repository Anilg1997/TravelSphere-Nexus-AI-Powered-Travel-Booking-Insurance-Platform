package com.travelsphere.package_.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "package_itineraries", schema = "package_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageItinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "day_number")
    private int dayNumber;

    @Column(name = "day_title")
    private String dayTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "activities", columnDefinition = "TEXT[]")
    private String[] activities;
}
