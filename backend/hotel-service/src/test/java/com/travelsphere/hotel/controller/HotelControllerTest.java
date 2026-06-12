package com.travelsphere.hotel.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.hotel.dto.*;
import com.travelsphere.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Mock private HotelService hotelService;
    @InjectMocks private HotelController hotelController;

    @Test
    void searchHotelsReturnsResults() {
        HotelSearchResponse hotel = HotelSearchResponse.builder()
                .id(UUID.randomUUID()).name("Grand Hotel").starRating(5)
                .city("Mumbai").country("India")
                .minPricePerNight(new BigDecimal("5000"))
                .availableRooms(10).averageRating(4.5).build();

        when(hotelService.searchHotels(any())).thenReturn(List.of(hotel));

        ResponseEntity<ApiResponse<List<HotelSearchResponse>>> response =
                hotelController.searchHotels("Mumbai", LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(3), 2, null);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getHotelReturnsHotel() {
        HotelSearchResponse hotel = HotelSearchResponse.builder()
                .id(UUID.randomUUID()).name("Grand Hotel").build();
        when(hotelService.getHotelById("id-123")).thenReturn(hotel);

        ResponseEntity<ApiResponse<HotelSearchResponse>> response =
                hotelController.getHotel("id-123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Grand Hotel", response.getBody().getData().getName());
    }

    @Test
    void bookHotelReturnsBooking() {
        BookingResponse booking = BookingResponse.builder()
                .bookingRef("TS-HTL-123").hotelName("Grand Hotel")
                .status("CONFIRMED").totalPrice(new BigDecimal("10000"))
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3)).build();
        when(hotelService.bookHotel(any(), any())).thenReturn(booking);

        BookingRequest request = BookingRequest.builder()
                .hotelId(UUID.randomUUID()).roomTypeId(UUID.randomUUID())
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3)).guests(2).build();

        ResponseEntity<ApiResponse<BookingResponse>> response =
                hotelController.bookHotel(request, UUID.randomUUID().toString());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CONFIRMED", response.getBody().getData().getStatus());
    }

    @Test
    void cancelBookingReturnsCancelled() {
        BookingResponse booking = BookingResponse.builder()
                .bookingRef("TS-HTL-123").status("CANCELLED").build();
        when(hotelService.cancelBooking("TS-HTL-123")).thenReturn(booking);

        ResponseEntity<ApiResponse<BookingResponse>> response =
                hotelController.cancelBooking("TS-HTL-123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CANCELLED", response.getBody().getData().getStatus());
    }

    @Test
    void addReviewReturnsReview() {
        UUID hotelUuid = UUID.randomUUID();
        ReviewResponse review = ReviewResponse.builder()
                .id(UUID.randomUUID()).rating(5).title("Great").build();
        when(hotelService.addReview(eq(hotelUuid.toString()), any())).thenReturn(review);

        ReviewRequest request = ReviewRequest.builder()
                .rating(5).title("Great").reviewText("Amazing").build();

        ResponseEntity<ApiResponse<ReviewResponse>> response =
                hotelController.addReview(hotelUuid.toString(), request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(5, response.getBody().getData().getRating());
    }
}
