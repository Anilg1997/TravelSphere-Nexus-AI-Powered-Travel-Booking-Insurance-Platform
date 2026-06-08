package com.travelsphere.transport.repository;

import com.travelsphere.transport.model.TransportBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportBookingRepository extends JpaRepository<TransportBooking, UUID> {
    Optional<TransportBooking> findByBookingRef(String bookingRef);
    Optional<TransportBooking> findByPnr(String pnr);
}
