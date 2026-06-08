package com.travelsphere.car.repository;

import com.travelsphere.car.model.VehicleBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleBookingRepository extends JpaRepository<VehicleBooking, UUID> {
    Optional<VehicleBooking> findByBookingRef(String bookingRef);
}
