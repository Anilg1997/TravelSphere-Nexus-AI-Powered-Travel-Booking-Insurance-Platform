package com.travelsphere.car.repository;

import com.travelsphere.car.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:city IS NULL OR LOWER(v.city) = LOWER(:city)) AND " +
           "(:type IS NULL OR v.vehicleType = :type) AND " +
           "v.isAvailable = true")
    List<Vehicle> searchVehicles(@Param("city") String city, @Param("type") String type);

    List<Vehicle> findByCityIgnoreCaseAndIsAvailableTrue(String city);
}
