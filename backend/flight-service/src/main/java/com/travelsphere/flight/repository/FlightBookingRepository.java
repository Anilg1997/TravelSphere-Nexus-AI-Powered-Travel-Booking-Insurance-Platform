package com.travelsphere.flight.repository;

import com.travelsphere.flight.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, UUID> {
    Optional<FlightBooking> findByBookingRef(String bookingRef);
}
