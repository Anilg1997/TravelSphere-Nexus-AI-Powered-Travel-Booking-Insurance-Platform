package com.travelsphere.transport.service;

import com.travelsphere.transport.dto.*;
import com.travelsphere.transport.model.TransportBooking;
import com.travelsphere.transport.model.TransportRoute;
import com.travelsphere.transport.repository.TransportBookingRepository;
import com.travelsphere.transport.repository.TransportRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {

    private final TransportRouteRepository routeRepository;
    private final TransportBookingRepository bookingRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<TransportSearchResponse> searchRoutes(TransportSearchRequest request) {
        String type = request.getTransportType() != null ? request.getTransportType().toUpperCase() : null;
        List<TransportRoute> routes = routeRepository.searchRoutes(
                request.getOriginCity(),
                request.getDestinationCity(),
                type,
                Math.max(request.getPassengers(), 1)
        );
        return routes.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    @Override
    public TransportSearchResponse getRouteById(String id) {
        TransportRoute route = routeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        return toSearchResponse(route);
    }

    @Override
    @Transactional
    public TransportBookingResponse bookTransport(TransportBookingRequest request) {
        TransportRoute route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        if (route.getAvailableSeats() < 1) {
            throw new IllegalStateException("No seats available");
        }

        String bookingRef = "TS-TR-" + System.currentTimeMillis() % 1000000;
        String pnr = generatePNR();

        TransportBooking booking = TransportBooking.builder()
                .bookingRef(bookingRef)
                .routeId(route.getId())
                .passengerName(request.getPassengerName())
                .passengerEmail(request.getPassengerEmail())
                .seatNumber(request.getSeatNumber())
                .pricePaid(route.getBasePrice())
                .status("CONFIRMED")
                .bookedAt(LocalDateTime.now())
                .pnr(pnr)
                .build();

        bookingRepository.save(booking);

        route.setAvailableSeats(route.getAvailableSeats() - 1);
        routeRepository.save(route);

        kafkaTemplate.send("ts.transport.booked", bookingRef, booking);

        return toBookingResponse(booking, route);
    }

    @Override
    public TransportBookingResponse getBookingByRef(String ref) {
        TransportBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        TransportRoute route = routeRepository.findById(booking.getRouteId()).orElse(null);
        return toBookingResponse(booking, route);
    }

    @Override
    @Transactional
    public TransportBookingResponse cancelBooking(String ref) {
        TransportBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        TransportRoute route = routeRepository.findById(booking.getRouteId()).orElse(null);
        if (route != null) {
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);
        }

        return toBookingResponse(booking, route);
    }

    @Override
    public TransportBookingResponse getBookingByPnr(String pnr) {
        TransportBooking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        TransportRoute route = routeRepository.findById(booking.getRouteId()).orElse(null);
        return toBookingResponse(booking, route);
    }

    private TransportSearchResponse toSearchResponse(TransportRoute route) {
        return TransportSearchResponse.builder()
                .id(route.getId())
                .routeName(route.getRouteName())
                .operator(route.getOperator())
                .transportType(route.getTransportType().name())
                .originCity(route.getOriginCity())
                .destinationCity(route.getDestinationCity())
                .originStation(route.getOriginStation())
                .destinationStation(route.getDestinationStation())
                .departureTime(route.getDepartureTime())
                .arrivalTime(route.getArrivalTime())
                .durationMinutes(route.getDurationMinutes())
                .price(route.getBasePrice())
                .availableSeats(route.getAvailableSeats())
                .build();
    }

    private TransportBookingResponse toBookingResponse(TransportBooking booking, TransportRoute route) {
        return TransportBookingResponse.builder()
                .bookingRef(booking.getBookingRef())
                .pnr(booking.getPnr())
                .routeName(route != null ? route.getRouteName() : "Unknown")
                .operator(route != null ? route.getOperator() : "Unknown")
                .passengerName(booking.getPassengerName())
                .seatNumber(booking.getSeatNumber())
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
