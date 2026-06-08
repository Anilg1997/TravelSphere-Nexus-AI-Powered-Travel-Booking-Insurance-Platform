package com.travelsphere.flight.service;

import com.travelsphere.flight.dto.*;
import com.travelsphere.flight.model.Flight;
import com.travelsphere.flight.model.FlightBooking;
import com.travelsphere.flight.model.Seat;
import com.travelsphere.flight.repository.FlightBookingRepository;
import com.travelsphere.flight.repository.FlightRepository;
import com.travelsphere.flight.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightBookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
                .filter(f -> f.getAvailableSeats() >= request.getPassengers())
                .map(this::toSearchResponse)
                .toList();
    }

    @Override
    public FlightSearchResponse getFlightById(String id) {
        Flight flight = flightRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));
        return toSearchResponse(flight);
    }

    @Override
    @Transactional
    public BookingResponse bookFlight(BookingRequest request) {
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        if (flight.getAvailableSeats() < 1) {
            throw new IllegalStateException("No seats available");
        }

        Seat seat = seatRepository.findByFlightIdAndSeatNumber(flight.getId(), request.getSeatNumber())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        if (!seat.isAvailable()) {
            throw new IllegalStateException("Seat not available");
        }

        String bookingRef = "TS-FL-" + System.currentTimeMillis() % 1000000;
        String pnr = generatePNR();

        FlightBooking booking = FlightBooking.builder()
                .bookingRef(bookingRef)
                .userId(null)
                .flightId(flight.getId())
                .passengerName(request.getPassengerName())
                .passengerEmail(request.getPassengerEmail())
                .seatNumber(request.getSeatNumber())
                .cabinClass(request.getCabinClass())
                .pricePaid(flight.getBasePrice())
                .status("CONFIRMED")
                .bookedAt(LocalDateTime.now())
                .pnr(pnr)
                .build();

        bookingRepository.save(booking);

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        seat.setAvailable(false);
        seatRepository.save(seat);

        kafkaTemplate.send("ts.flights.booked", bookingRef, booking);

        return toBookingResponse(booking);
    }

    @Override
    public BookingResponse getBookingByRef(String ref) {
        FlightBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(String ref) {
        FlightBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        kafkaTemplate.send("ts.flights.cancelled", ref, booking);

        return toBookingResponse(booking);
    }

    @Override
    public BookingResponse checkIn(String ref) {
        FlightBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus("CHECKED_IN");
        bookingRepository.save(booking);

        kafkaTemplate.send("ts.flights.checked-in", ref, booking);

        return toBookingResponse(booking);
    }

    private FlightSearchResponse toSearchResponse(Flight flight) {
        return FlightSearchResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airline(flight.getAirline())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .durationMinutes(flight.getDurationMinutes())
                .aircraftType(flight.getAircraftType())
                .price(flight.getBasePrice())
                .availableSeats(flight.getAvailableSeats())
                .build();
    }

    private BookingResponse toBookingResponse(FlightBooking booking) {
        return BookingResponse.builder()
                .bookingRef(booking.getBookingRef())
                .pnr(booking.getPnr())
                .passengerName(booking.getPassengerName())
                .seatNumber(booking.getSeatNumber())
                .cabinClass(booking.getCabinClass())
                .pricePaid(booking.getPricePaid())
                .status(booking.getStatus())
                .bookedAt(booking.getBookedAt())
                .build();
    }

    private String generatePNR() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder pnr = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            pnr.append(chars.charAt(random.nextInt(chars.length())));
        }
        return pnr.toString();
    }
}
