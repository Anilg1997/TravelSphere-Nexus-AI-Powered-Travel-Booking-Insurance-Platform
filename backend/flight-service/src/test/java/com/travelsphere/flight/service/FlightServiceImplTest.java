package com.travelsphere.flight.service;

import com.travelsphere.flight.dto.*;
import com.travelsphere.flight.model.Flight;
import com.travelsphere.flight.model.FlightBooking;
import com.travelsphere.flight.model.Seat;
import com.travelsphere.flight.repository.FlightBookingRepository;
import com.travelsphere.flight.repository.FlightRepository;
import com.travelsphere.flight.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock private FlightRepository flightRepository;
    @Mock private FlightBookingRepository bookingRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private FlightServiceImpl flightService;

    private UUID flightId;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flightId = UUID.randomUUID();
        flight = Flight.builder()
                .id(flightId).flightNumber("TS-101").airline("TravelSphere Air")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(3))
                .durationMinutes(180).aircraftType("Boeing 737")
                .basePrice(new BigDecimal("4500.00")).availableSeats(10).status("ACTIVE").build();
    }

    @Test
    void searchFlightsReturnsFilteredResults() {
        Flight flight2 = Flight.builder().id(UUID.randomUUID()).availableSeats(0).build();
        when(flightRepository.findAll()).thenReturn(List.of(flight, flight2));

        FlightSearchRequest request = FlightSearchRequest.builder()
                .origin("DEL").destination("BOM").passengers(2).build();

        List<FlightSearchResponse> results = flightService.searchFlights(request);

        assertEquals(1, results.size());
        assertEquals("TS-101", results.get(0).getFlightNumber());
    }

    @Test
    void searchFlightsReturnsAllWhenSufficientSeats() {
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        FlightSearchRequest request = FlightSearchRequest.builder()
                .origin("DEL").destination("BOM").passengers(5).build();

        List<FlightSearchResponse> results = flightService.searchFlights(request);
        assertEquals(1, results.size());
    }

    @Test
    void getFlightByIdReturnsFlight() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        FlightSearchResponse response = flightService.getFlightById(flightId.toString());

        assertEquals("TS-101", response.getFlightNumber());
        assertEquals("TravelSphere Air", response.getAirline());
    }

    @Test
    void getFlightByIdNotFoundThrows() {
        when(flightRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> flightService.getFlightById(UUID.randomUUID().toString()));
    }

    @Test
    void bookFlightSuccess() {
        Seat seat = Seat.builder().id(UUID.randomUUID()).flightId(flightId).seatNumber("12A").isAvailable(true).build();
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(seatRepository.findByFlightIdAndSeatNumber(flightId, "12A")).thenReturn(Optional.of(seat));
        when(flightRepository.save(any())).thenReturn(flight);
        when(seatRepository.save(any())).thenReturn(seat);
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        BookingRequest request = BookingRequest.builder()
                .flightId(flightId).passengerName("John Doe")
                .passengerEmail("john@email.com").seatNumber("12A").cabinClass("economy").build();

        BookingResponse response = flightService.bookFlight(request);

        assertNotNull(response);
        assertTrue(response.getBookingRef().startsWith("TS-FL-"));
        assertEquals("CONFIRMED", response.getStatus());
        assertNotNull(response.getPnr());
        assertEquals(6, response.getPnr().length());
        assertFalse(seat.isAvailable());
        assertEquals(9, flight.getAvailableSeats());
        verify(kafkaTemplate).send(eq("ts.flights.booked"), anyString(), any());
    }

    @Test
    void bookFlightNoSeatsAvailableThrows() {
        flight.setAvailableSeats(0);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        BookingRequest request = BookingRequest.builder()
                .flightId(flightId).passengerName("John").passengerEmail("j@e.com")
                .seatNumber("1A").cabinClass("economy").build();

        assertThrows(IllegalStateException.class, () -> flightService.bookFlight(request));
    }

    @Test
    void bookFlightSeatNotAvailableThrows() {
        Seat seat = Seat.builder().id(UUID.randomUUID()).flightId(flightId).seatNumber("12A").isAvailable(false).build();
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(seatRepository.findByFlightIdAndSeatNumber(flightId, "12A")).thenReturn(Optional.of(seat));

        BookingRequest request = BookingRequest.builder()
                .flightId(flightId).passengerName("John").passengerEmail("j@e.com")
                .seatNumber("12A").cabinClass("economy").build();

        assertThrows(IllegalStateException.class, () -> flightService.bookFlight(request));
    }

    @Test
    void cancelBookingSetsStatusCancelled() {
        FlightBooking booking = FlightBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-FL-12345").status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-FL-12345")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponse response = flightService.cancelBooking("TS-FL-12345");

        assertEquals("CANCELLED", response.getStatus());
        verify(kafkaTemplate).send(eq("ts.flights.cancelled"), eq("TS-FL-12345"), any());
    }

    @Test
    void checkInSetsCheckedInStatus() {
        FlightBooking booking = FlightBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-FL-12345").status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-FL-12345")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponse response = flightService.checkIn("TS-FL-12345");

        assertEquals("CHECKED_IN", response.getStatus());
        verify(kafkaTemplate).send(eq("ts.flights.checked-in"), eq("TS-FL-12345"), any());
    }

    @Test
    void getBookingByRefNotFoundThrows() {
        when(bookingRepository.findByBookingRef("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> flightService.getBookingByRef("INVALID"));
    }
}
