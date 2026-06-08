package com.travelsphere.flight.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.flight.dto.*;
import com.travelsphere.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Flight search, booking, check-in, and status APIs")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/search")
    @Operation(summary = "Search flights by origin, destination, and date")
    public ResponseEntity<ApiResponse<List<FlightSearchResponse>>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "economy") String cabin,
            @RequestParam(defaultValue = "1") int passengers) {
        FlightSearchRequest request = FlightSearchRequest.builder()
                .origin(origin).destination(destination)
                .date(date).cabinClass(cabin).passengers(passengers)
                .build();
        List<FlightSearchResponse> results = flightService.searchFlights(request);
        return ResponseEntity.ok(ApiResponse.success(results, "Flights found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight details by ID")
    public ResponseEntity<ApiResponse<FlightSearchResponse>> getFlight(@PathVariable String id) {
        FlightSearchResponse flight = flightService.getFlightById(id);
        return ResponseEntity.ok(ApiResponse.success(flight));
    }

    @PostMapping("/book")
    @Operation(summary = "Book a flight")
    public ResponseEntity<ApiResponse<BookingResponse>> bookFlight(@Valid @RequestBody BookingRequest request) {
        BookingResponse booking = flightService.bookFlight(request);
        return ResponseEntity.ok(ApiResponse.success(booking, "Flight booked successfully"));
    }

    @GetMapping("/booking/{ref}")
    @Operation(summary = "Get booking details by reference")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(@PathVariable String ref) {
        BookingResponse booking = flightService.getBookingByRef(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/cancel/{ref}")
    @Operation(summary = "Cancel a flight booking")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable String ref) {
        BookingResponse booking = flightService.cancelBooking(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
    }

    @PostMapping("/check-in/{ref}")
    @Operation(summary = "Check in for a flight")
    public ResponseEntity<ApiResponse<BookingResponse>> checkIn(@PathVariable String ref) {
        BookingResponse booking = flightService.checkIn(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Check-in successful"));
    }
}
