package com.travelsphere.flight.repository;

import com.travelsphere.flight.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByFlightId(UUID flightId);
    Optional<Seat> findByFlightIdAndSeatNumber(UUID flightId, String seatNumber);
}
