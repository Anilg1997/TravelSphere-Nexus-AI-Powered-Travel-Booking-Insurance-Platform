package com.travelsphere.transport.service;

import com.travelsphere.transport.dto.*;
import com.travelsphere.transport.model.TransportBooking;
import com.travelsphere.transport.model.TransportRoute;
import com.travelsphere.transport.repository.TransportBookingRepository;
import com.travelsphere.transport.repository.TransportRouteRepository;
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
class TransportServiceImplTest {

    @Mock private TransportRouteRepository routeRepository;
    @Mock private TransportBookingRepository bookingRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private TransportServiceImpl transportService;

    private UUID routeId;
    private TransportRoute route;

    @BeforeEach
    void setUp() {
        routeId = UUID.randomUUID();
        route = TransportRoute.builder()
                .id(routeId).routeName("Mumbai-Pune Express").operator("MSRTC")
                .transportType(TransportRoute.TransportType.BUS)
                .originCity("Mumbai").destinationCity("Pune")
                .originStation("Dadar").destinationStation("Pune Station")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(4))
                .durationMinutes(240).basePrice(new BigDecimal("800.00"))
                .availableSeats(20).build();
    }

    @Test
    void searchRoutesReturnsResults() {
        when(routeRepository.searchRoutes("Mumbai", "Pune", "BUS", 1)).thenReturn(List.of(route));

        TransportSearchRequest request = TransportSearchRequest.builder()
                .originCity("Mumbai").destinationCity("Pune")
                .transportType("BUS").passengers(1).build();

        List<TransportSearchResponse> results = transportService.searchRoutes(request);

        assertEquals(1, results.size());
        assertEquals("Mumbai-Pune Express", results.get(0).getRouteName());
    }

    @Test
    void bookTransportSuccess() {
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(routeRepository.save(any())).thenReturn(route);

        TransportBookingRequest request = TransportBookingRequest.builder()
                .routeId(routeId).passengerName("John")
                .passengerEmail("john@email.com").seatNumber("A1").build();

        TransportBookingResponse response = transportService.bookTransport(request);

        assertNotNull(response);
        assertTrue(response.getBookingRef().startsWith("TS-TR-"));
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals(19, route.getAvailableSeats());
        verify(kafkaTemplate).send(eq("ts.transport.booked"), anyString(), any());
    }

    @Test
    void bookTransportNoSeatsThrows() {
        route.setAvailableSeats(0);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));

        TransportBookingRequest request = TransportBookingRequest.builder()
                .routeId(routeId).passengerName("John")
                .passengerEmail("john@email.com").seatNumber("A1").build();

        assertThrows(IllegalStateException.class, () -> transportService.bookTransport(request));
    }

    @Test
    void cancelBookingRestoresSeatAndSetsCancelled() {
        TransportBooking booking = TransportBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-TR-123").routeId(routeId)
                .status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-TR-123")).thenReturn(Optional.of(booking));
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(routeRepository.save(any())).thenReturn(route);

        TransportBookingResponse response = transportService.cancelBooking("TS-TR-123");

        assertEquals("CANCELLED", response.getStatus());
        assertEquals(21, route.getAvailableSeats()); // restored +1
    }

    @Test
    void getBookingByRefNotFoundThrows() {
        when(bookingRepository.findByBookingRef("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> transportService.getBookingByRef("INVALID"));
    }
}
