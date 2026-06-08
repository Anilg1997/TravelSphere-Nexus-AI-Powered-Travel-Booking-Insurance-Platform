package com.travelsphere.car.controller;

import com.travelsphere.car.dto.*;
import com.travelsphere.car.service.CarRentalService;
import com.travelsphere.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@Tag(name = "Car Rental", description = "Vehicle search and booking APIs")
public class CarRentalController {

    private final CarRentalService carRentalService;

    @GetMapping("/search")
    @Operation(summary = "Search available vehicles")
    public ResponseEntity<ApiResponse<List<VehicleSearchResponse>>> searchVehicles(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type) {
        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .city(city).vehicleType(type).build();
        List<VehicleSearchResponse> results = carRentalService.searchVehicles(request);
        return ResponseEntity.ok(ApiResponse.success(results, "Vehicles found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle details by ID")
    public ResponseEntity<ApiResponse<VehicleSearchResponse>> getVehicle(@PathVariable String id) {
        VehicleSearchResponse vehicle = carRentalService.getVehicleById(id);
        return ResponseEntity.ok(ApiResponse.success(vehicle));
    }

    @PostMapping("/book")
    @Operation(summary = "Book a vehicle")
    public ResponseEntity<ApiResponse<VehicleBookingResponse>> bookVehicle(
            @Valid @RequestBody VehicleBookingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        VehicleBookingResponse booking = carRentalService.bookVehicle(request, userId);
        return ResponseEntity.ok(ApiResponse.success(booking, "Vehicle booked successfully"));
    }

    @GetMapping("/booking/{ref}")
    @Operation(summary = "Get booking by reference")
    public ResponseEntity<ApiResponse<VehicleBookingResponse>> getBooking(@PathVariable String ref) {
        VehicleBookingResponse booking = carRentalService.getBookingByRef(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/cancel/{ref}")
    @Operation(summary = "Cancel a vehicle booking")
    public ResponseEntity<ApiResponse<VehicleBookingResponse>> cancelBooking(@PathVariable String ref) {
        VehicleBookingResponse booking = carRentalService.cancelBooking(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
    }
}
