package com.travelsphere.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "room_types", schema = "hotel_schema")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoomType {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hotel_id")
    private UUID hotelId;

    @Column(name = "type_name")
    private String typeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_occupancy")
    private int maxOccupancy;

    @Column(name = "base_price_per_night")
    private BigDecimal basePricePerNight;

    @Column(name = "total_rooms")
    private int totalRooms;

    @Column(name = "available_rooms")
    private int availableRooms;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;
}
