package com.travelsphere.package_.repository;

import com.travelsphere.package_.model.HolidayPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolidayPackageRepository extends JpaRepository<HolidayPackage, UUID> {

    @Query("SELECT p FROM HolidayPackage p WHERE " +
           "(:destination IS NULL OR LOWER(p.destination) = LOWER(:destination)) AND " +
           "p.isActive = true")
    List<HolidayPackage> searchPackages(@Param("destination") String destination);

    List<HolidayPackage> findByIsActiveTrue();
}
