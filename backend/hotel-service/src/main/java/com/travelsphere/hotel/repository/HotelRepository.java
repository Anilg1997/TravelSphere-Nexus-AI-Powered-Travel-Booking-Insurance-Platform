package com.travelsphere.hotel.repository;

import com.travelsphere.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    @Query("SELECT h FROM Hotel h WHERE h.isActive = true " +
           "AND (:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:minStars IS NULL OR h.starRating >= :minStars) " +
           "ORDER BY h.starRating DESC, h.name ASC")
    List<Hotel> searchHotels(@Param("city") String city,
                             @Param("minStars") Integer minStars);

    List<Hotel> findByCityIgnoreCaseAndIsActiveTrue(String city);

    List<Hotel> findByStarRatingGreaterThanEqualAndIsActiveTrue(int starRating);
}
