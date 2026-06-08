package com.travelsphere.package_.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.package_.dto.*;
import com.travelsphere.package_.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
@Tag(name = "Packages", description = "Holiday packages, itineraries, and group booking APIs")
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/search")
    @Operation(summary = "Search holiday packages")
    public ResponseEntity<ApiResponse<List<PackageSearchResponse>>> searchPackages(
            @RequestParam(required = false) String destination) {
        List<PackageSearchResponse> results = packageService.searchPackages(destination);
        return ResponseEntity.ok(ApiResponse.success(results, "Packages found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get package details by ID")
    public ResponseEntity<ApiResponse<PackageSearchResponse>> getPackage(@PathVariable String id) {
        PackageSearchResponse pkg = packageService.getPackageById(id);
        return ResponseEntity.ok(ApiResponse.success(pkg));
    }

    @PostMapping("/book")
    @Operation(summary = "Book a holiday package")
    public ResponseEntity<ApiResponse<PackageBookingResponse>> bookPackage(
            @Valid @RequestBody PackageBookingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        PackageBookingResponse booking = packageService.bookPackage(request, userId);
        return ResponseEntity.ok(ApiResponse.success(booking, "Package booked successfully"));
    }

    @GetMapping("/booking/{ref}")
    @Operation(summary = "Get booking by reference")
    public ResponseEntity<ApiResponse<PackageBookingResponse>> getBooking(@PathVariable String ref) {
        PackageBookingResponse booking = packageService.getBookingByRef(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/cancel/{ref}")
    @Operation(summary = "Cancel a package booking")
    public ResponseEntity<ApiResponse<PackageBookingResponse>> cancelBooking(@PathVariable String ref) {
        PackageBookingResponse booking = packageService.cancelBooking(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
    }
}
