package com.travelsphere.flight.repository;

import com.travelsphere.flight.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {

    @Query("SELECT f FROM Flight f WHERE f.originAirportId = :origin " +
           "AND f.destinationAirportId = :destination " +
           "AND f.departureTime BETWEEN :startDate AND :endDate " +
           "AND f.availableSeats >= :passengers " +
           "ORDER BY f.departureTime ASC")
    List<Flight> searchFlights(@Param("origin") UUID origin,
                               @Param("destination") UUID destination,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate,
                               @Param("passengers") int passengers);
}
