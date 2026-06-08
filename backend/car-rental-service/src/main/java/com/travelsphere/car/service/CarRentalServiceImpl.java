package com.travelsphere.car.service;

import com.travelsphere.car.dto.*;
import com.travelsphere.car.model.Vehicle;
import com.travelsphere.car.model.VehicleBooking;
import com.travelsphere.car.repository.VehicleBookingRepository;
import com.travelsphere.car.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarRentalServiceImpl implements CarRentalService {

    private final VehicleRepository vehicleRepository;
    private final VehicleBookingRepository bookingRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<VehicleSearchResponse> searchVehicles(VehicleSearchRequest request) {
        String type = request.getVehicleType() != null ? request.getVehicleType().toUpperCase() : null;
        List<Vehicle> vehicles = vehicleRepository.searchVehicles(request.getCity(), type);
        return vehicles.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    @Override
    public VehicleSearchResponse getVehicleById(String id) {
        Vehicle vehicle = vehicleRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        return toSearchResponse(vehicle);
    }

    @Override
    @Transactional
    public VehicleBookingResponse bookVehicle(VehicleBookingRequest request, String userId) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (!vehicle.isAvailable()) {
            throw new IllegalStateException("Vehicle is not available");
        }

        long days = ChronoUnit.DAYS.between(request.getPickupDate(), request.getReturnDate());
        if (days < 1) {
            throw new IllegalArgumentException("Return date must be after pickup date");
        }

        BigDecimal totalPrice = vehicle.getDailyRate().multiply(BigDecimal.valueOf(days));
        String bookingRef = "TS-CAR-" + System.currentTimeMillis() % 1000000;

        VehicleBooking booking = VehicleBooking.builder()
                .bookingRef(bookingRef)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .vehicleId(vehicle.getId())
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .pickupCity(request.getPickupCity())
                .totalDays((int) days)
                .dailyRate(vehicle.getDailyRate())
                .totalPrice(totalPrice)
                .addons(request.getAddons())
                .status("CONFIRMED")
                .bookedAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        kafkaTemplate.send("ts.cars.booked", bookingRef, booking);

        return toBookingResponse(booking, vehicle);
    }

    @Override
    public VehicleBookingResponse getBookingByRef(String ref) {
        VehicleBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        return toBookingResponse(booking, vehicle);
    }

    @Override
    @Transactional
    public VehicleBookingResponse cancelBooking(String ref) {
        VehicleBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
        if (vehicle != null) {
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
        }

        return toBookingResponse(booking, vehicle);
    }

    private VehicleSearchResponse toSearchResponse(Vehicle vehicle) {
        return VehicleSearchResponse.builder()
                .id(vehicle.getId())
                .vehicleName(vehicle.getVehicleName())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .vehicleType(vehicle.getVehicleType().name())
                .color(vehicle.getColor())
                .fuelType(vehicle.getFuelType())
                .transmission(vehicle.getTransmission())
                .seatingCapacity(vehicle.getSeatingCapacity())
                .dailyRate(vehicle.getDailyRate())
                .city(vehicle.getCity())
                .isAvailable(vehicle.isAvailable())
                .imageS3Key(vehicle.getImageS3Key())
                .build();
    }

    private VehicleBookingResponse toBookingResponse(VehicleBooking booking, Vehicle vehicle) {
        return VehicleBookingResponse.builder()
                .bookingRef(booking.getBookingRef())
                .vehicleName(vehicle != null ? vehicle.getVehicleName() : "Unknown")
                .brand(vehicle != null ? vehicle.getBrand() : "Unknown")
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .pickupCity(booking.getPickupCity())
                .totalDays(booking.getTotalDays())
                .totalPrice(booking.getTotalPrice())
                .addons(booking.getAddons())
                .status(booking.getStatus())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}
