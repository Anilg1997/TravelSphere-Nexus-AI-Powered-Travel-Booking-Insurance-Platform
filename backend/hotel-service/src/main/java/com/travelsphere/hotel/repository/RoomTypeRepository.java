package com.travelsphere.hotel.repository;

import com.travelsphere.hotel.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {
    List<RoomType> findByHotelId(UUID hotelId);
    List<RoomType> findByHotelIdAndAvailableRoomsGreaterThan(UUID hotelId, int minAvailable);
}
