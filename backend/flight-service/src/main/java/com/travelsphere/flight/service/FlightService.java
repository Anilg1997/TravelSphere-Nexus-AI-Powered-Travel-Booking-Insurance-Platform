package com.travelsphere.flight.service;

import com.travelsphere.flight.dto.*;

import java.util.List;

public interface FlightService {
    List<FlightSearchResponse> searchFlights(FlightSearchRequest request);
    FlightSearchResponse getFlightById(String id);
    BookingResponse bookFlight(BookingRequest request);
    BookingResponse getBookingByRef(String ref);
    BookingResponse cancelBooking(String ref);
    BookingResponse checkIn(String ref);
}
