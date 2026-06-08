package com.travelsphere.hotel.repository;

import com.travelsphere.hotel.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, UUID> {
    Optional<HotelBooking> findByBookingRef(String bookingRef);
    List<HotelBooking> findByUserId(UUID userId);
    List<HotelBooking> findByHotelId(UUID hotelId);
}
