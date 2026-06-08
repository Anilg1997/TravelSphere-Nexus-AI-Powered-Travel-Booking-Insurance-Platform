package com.travelsphere.package_.repository;

import com.travelsphere.package_.model.PackageItinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PackageItineraryRepository extends JpaRepository<PackageItinerary, UUID> {
    List<PackageItinerary> findByPackageIdOrderByDayNumber(UUID packageId);
}
