package com.travelsphere.flight.controller;

import com.travelsphere.common.dto.ApiResponse;
import com.travelsphere.flight.dto.*;
import com.travelsphere.flight.service.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    @Mock private FlightService flightService;
    @InjectMocks private FlightController flightController;

    @Test
    void searchFlightsReturnsResults() {
        FlightSearchResponse flight = FlightSearchResponse.builder()
                .id(UUID.randomUUID()).flightNumber("TS-101").airline("TravelSphere")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(3))
                .durationMinutes(180).aircraftType("B737")
                .price(new BigDecimal("4500")).availableSeats(10).build();

        when(flightService.searchFlights(any())).thenReturn(List.of(flight));

        ResponseEntity<ApiResponse<List<FlightSearchResponse>>> response =
                flightController.searchFlights("DEL", "BOM",
                        java.time.LocalDate.now().plusDays(1), "economy", 1);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getFlightReturnsFlight() {
        FlightSearchResponse flight = FlightSearchResponse.builder()
                .id(UUID.randomUUID()).flightNumber("TS-101").build();
        when(flightService.getFlightById("id-123")).thenReturn(flight);

        ResponseEntity<ApiResponse<FlightSearchResponse>> response =
                flightController.getFlight("id-123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("TS-101", response.getBody().getData().getFlightNumber());
    }

    @Test
    void bookFlightReturnsBooking() {
        BookingResponse booking = BookingResponse.builder()
                .bookingRef("TS-FL-123").status("CONFIRMED")
                .passengerName("John").seatNumber("12A").cabinClass("economy")
                .pricePaid(new BigDecimal("4500")).build();
        when(flightService.bookFlight(any())).thenReturn(booking);

        BookingRequest request = BookingRequest.builder()
                .flightId(UUID.randomUUID()).passengerName("John")
                .passengerEmail("j@e.com").seatNumber("12A").cabinClass("economy").build();

        ResponseEntity<ApiResponse<BookingResponse>> response = flightController.bookFlight(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CONFIRMED", response.getBody().getData().getStatus());
        assertEquals("Flight booked successfully", response.getBody().getMessage());
    }

    @Test
    void cancelBookingReturnsCancelled() {
        BookingResponse booking = BookingResponse.builder()
                .bookingRef("TS-FL-123").status("CANCELLED").build();
        when(flightService.cancelBooking("TS-FL-123")).thenReturn(booking);

        ResponseEntity<ApiResponse<BookingResponse>> response =
                flightController.cancelBooking("TS-FL-123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CANCELLED", response.getBody().getData().getStatus());
    }

    @Test
    void checkInReturnsCheckedIn() {
        BookingResponse booking = BookingResponse.builder()
                .bookingRef("TS-FL-123").status("CHECKED_IN").build();
        when(flightService.checkIn("TS-FL-123")).thenReturn(booking);

        ResponseEntity<ApiResponse<BookingResponse>> response =
                flightController.checkIn("TS-FL-123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("CHECKED_IN", response.getBody().getData().getStatus());
    }
}
