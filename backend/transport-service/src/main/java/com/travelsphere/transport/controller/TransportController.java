package com.travelsphere.transport.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.transport.dto.*;
import com.travelsphere.transport.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transport")
@RequiredArgsConstructor
@Tag(name = "Transport", description = "Bus/train search, booking, and PNR APIs")
public class TransportController {

    private final TransportService transportService;

    @GetMapping("/search")
    @Operation(summary = "Search transport routes")
    public ResponseEntity<ApiResponse<List<TransportSearchResponse>>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int passengers) {
        TransportSearchRequest request = TransportSearchRequest.builder()
                .originCity(origin).destinationCity(destination)
                .transportType(type).passengers(passengers).build();
        List<TransportSearchResponse> results = transportService.searchRoutes(request);
        return ResponseEntity.ok(ApiResponse.success(results, "Routes found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get route details by ID")
    public ResponseEntity<ApiResponse<TransportSearchResponse>> getRoute(@PathVariable String id) {
        TransportSearchResponse route = transportService.getRouteById(id);
        return ResponseEntity.ok(ApiResponse.success(route));
    }

    @PostMapping("/book")
    @Operation(summary = "Book a transport ticket")
    public ResponseEntity<ApiResponse<TransportBookingResponse>> bookTransport(
            @Valid @RequestBody TransportBookingRequest request) {
        TransportBookingResponse booking = transportService.bookTransport(request);
        return ResponseEntity.ok(ApiResponse.success(booking, "Transport booked successfully"));
    }

    @GetMapping("/booking/{ref}")
    @Operation(summary = "Get booking by reference")
    public ResponseEntity<ApiResponse<TransportBookingResponse>> getBooking(@PathVariable String ref) {
        TransportBookingResponse booking = transportService.getBookingByRef(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/cancel/{ref}")
    @Operation(summary = "Cancel a transport booking")
    public ResponseEntity<ApiResponse<TransportBookingResponse>> cancelBooking(@PathVariable String ref) {
        TransportBookingResponse booking = transportService.cancelBooking(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
    }

    @GetMapping("/pnr/{pnr}")
    @Operation(summary = "Get booking by PNR")
    public ResponseEntity<ApiResponse<TransportBookingResponse>> getBookingByPnr(@PathVariable String pnr) {
        TransportBookingResponse booking = transportService.getBookingByPnr(pnr);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }
}
