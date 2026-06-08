package com.travelsphere.transport.repository;

import com.travelsphere.transport.model.TransportRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransportRouteRepository extends JpaRepository<TransportRoute, UUID> {

    @Query("SELECT r FROM TransportRoute r WHERE " +
           "(:origin IS NULL OR LOWER(r.originCity) = LOWER(:origin)) AND " +
           "(:destination IS NULL OR LOWER(r.destinationCity) = LOWER(:destination)) AND " +
           "(:type IS NULL OR r.transportType = :type) AND " +
           "r.availableSeats >= :passengers AND r.status = 'ACTIVE' " +
           "ORDER BY r.departureTime ASC")
    List<TransportRoute> searchRoutes(@Param("origin") String origin,
                                       @Param("destination") String destination,
                                       @Param("type") String type,
                                       @Param("passengers") int passengers);

    List<TransportRoute> findByOriginCityIgnoreCaseAndDestinationCityIgnoreCaseAndStatus(
            String originCity, String destinationCity, String status);
}
