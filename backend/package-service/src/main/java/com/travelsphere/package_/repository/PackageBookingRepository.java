package com.travelsphere.package_.repository;

import com.travelsphere.package_.model.PackageBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageBookingRepository extends JpaRepository<PackageBooking, UUID> {
    Optional<PackageBooking> findByBookingRef(String bookingRef);
}
