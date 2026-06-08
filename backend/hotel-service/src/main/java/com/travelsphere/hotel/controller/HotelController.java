package com.travelsphere.hotel.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.hotel.dto.*;
import com.travelsphere.hotel.service.HotelService;
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
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel search, booking, reviews, and management APIs")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/search")
    @Operation(summary = "Search hotels by city, dates, and filters")
    public ResponseEntity<ApiResponse<List<HotelSearchResponse>>> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(defaultValue = "1") int guests,
            @RequestParam(required = false) Integer stars) {
        HotelSearchRequest request = HotelSearchRequest.builder()
                .city(city).checkIn(checkIn).checkOut(checkOut)
                .guests(guests).minStars(stars).build();
        List<HotelSearchResponse> results = hotelService.searchHotels(request);
        return ResponseEntity.ok(ApiResponse.success(results, "Hotels found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hotel details by ID")
    public ResponseEntity<ApiResponse<HotelSearchResponse>> getHotel(@PathVariable String id) {
        HotelSearchResponse hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(ApiResponse.success(hotel));
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get hotels by city name")
    public ResponseEntity<ApiResponse<List<HotelSearchResponse>>> getHotelsByCity(@PathVariable String city) {
        List<HotelSearchResponse> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(ApiResponse.success(hotels, "Hotels found in " + city));
    }

    @PostMapping("/book")
    @Operation(summary = "Book a hotel room")
    public ResponseEntity<ApiResponse<BookingResponse>> bookHotel(
            @Valid @RequestBody BookingRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        BookingResponse booking = hotelService.bookHotel(request, userId);
        return ResponseEntity.ok(ApiResponse.success(booking, "Hotel booked successfully"));
    }

    @GetMapping("/booking/{ref}")
    @Operation(summary = "Get booking details by reference")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(@PathVariable String ref) {
        BookingResponse booking = hotelService.getBookingByRef(ref);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/cancel/{ref}")
    @Operation(summary = "Cancel a hotel booking")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable String ref) {
        BookingResponse booking = hotelService.cancelBooking(ref);
        return ResponseEntity.ok(ApiResponse.success(booking, "Booking cancelled successfully"));
    }

    @PostMapping("/{id}/reviews")
    @Operation(summary = "Add a review for a hotel")
    public ResponseEntity<ApiResponse<ReviewResponse>> addReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest request) {
        request.setHotelId(java.util.UUID.fromString(id));
        ReviewResponse review = hotelService.addReview(id, request);
        return ResponseEntity.ok(ApiResponse.success(review, "Review added successfully"));
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get all reviews for a hotel")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getHotelReviews(@PathVariable String id) {
        List<ReviewResponse> reviews = hotelService.getHotelReviews(id);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }
}
