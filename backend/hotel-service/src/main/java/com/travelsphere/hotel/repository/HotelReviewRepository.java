package com.travelsphere.hotel.repository;

import com.travelsphere.hotel.model.HotelReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelReviewRepository extends JpaRepository<HotelReview, UUID> {
    List<HotelReview> findByHotelId(UUID hotelId);
    List<HotelReview> findByUserId(UUID userId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM HotelReview r WHERE r.hotelId = :hotelId")
    double averageRatingByHotelId(@Param("hotelId") UUID hotelId);

    @Query("SELECT COUNT(r) FROM HotelReview r WHERE r.hotelId = :hotelId")
    int countByHotelId(@Param("hotelId") UUID hotelId);
}
