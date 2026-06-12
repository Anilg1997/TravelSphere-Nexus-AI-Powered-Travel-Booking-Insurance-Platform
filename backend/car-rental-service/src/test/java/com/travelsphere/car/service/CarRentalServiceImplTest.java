package com.travelsphere.car.service;

import com.travelsphere.car.dto.*;
import com.travelsphere.car.model.Vehicle;
import com.travelsphere.car.model.VehicleBooking;
import com.travelsphere.car.repository.VehicleBookingRepository;
import com.travelsphere.car.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarRentalServiceImplTest {

    @Mock private VehicleRepository vehicleRepository;
    @Mock private VehicleBookingRepository bookingRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private CarRentalServiceImpl carRentalService;

    private UUID vehicleId;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        vehicle = Vehicle.builder()
                .id(vehicleId).vehicleName("Swift Dzire").brand("Maruti")
                .model("Dzire").year(2023).vehicleType(Vehicle.VehicleType.SEDAN)
                .color("White").fuelType("Petrol").transmission("Automatic")
                .seatingCapacity(5).dailyRate(new BigDecimal("2500.00"))
                .city("Mumbai").isAvailable(true).build();
    }

    @Test
    void searchVehiclesReturnsResults() {
        when(vehicleRepository.searchVehicles("Mumbai", "SEDAN")).thenReturn(List.of(vehicle));

        VehicleSearchRequest request = VehicleSearchRequest.builder()
                .city("Mumbai").vehicleType("SEDAN").build();

        List<VehicleSearchResponse> results = carRentalService.searchVehicles(request);

        assertEquals(1, results.size());
        assertEquals("Swift Dzire", results.get(0).getVehicleName());
    }

    @Test
    void bookVehicleSuccess() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(vehicleRepository.save(any())).thenReturn(vehicle);

        VehicleBookingRequest request = VehicleBookingRequest.builder()
                .vehicleId(vehicleId).pickupDate(LocalDate.now().plusDays(1))
                .returnDate(LocalDate.now().plusDays(4))
                .pickupCity("Mumbai").addons(new String[]{"GPS", "Child Seat"}).build();

        VehicleBookingResponse response = carRentalService.bookVehicle(request, UUID.randomUUID().toString());

        assertNotNull(response);
        assertTrue(response.getBookingRef().startsWith("TS-CAR-"));
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals(new BigDecimal("7500.00"), response.getTotalPrice()); // 3 days * 2500
        assertFalse(vehicle.isAvailable());
        verify(kafkaTemplate).send(eq("ts.cars.booked"), anyString(), any());
    }

    @Test
    void bookVehicleNotAvailableThrows() {
        vehicle.setAvailable(false);
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleBookingRequest request = VehicleBookingRequest.builder()
                .vehicleId(vehicleId).pickupDate(LocalDate.now().plusDays(1))
                .returnDate(LocalDate.now().plusDays(2)).pickupCity("Mumbai").build();

        assertThrows(IllegalStateException.class,
                () -> carRentalService.bookVehicle(request, null));
    }

    @Test
    void bookVehicleReturnBeforePickupThrows() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleBookingRequest request = VehicleBookingRequest.builder()
                .vehicleId(vehicleId).pickupDate(LocalDate.now().plusDays(3))
                .returnDate(LocalDate.now().plusDays(1)).pickupCity("Mumbai").build();

        assertThrows(IllegalArgumentException.class,
                () -> carRentalService.bookVehicle(request, null));
    }

    @Test
    void cancelBookingRestoresVehicle() {
        VehicleBooking booking = VehicleBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-CAR-123").vehicleId(vehicleId)
                .status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-CAR-123")).thenReturn(Optional.of(booking));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(vehicleRepository.save(any())).thenReturn(vehicle);

        VehicleBookingResponse response = carRentalService.cancelBooking("TS-CAR-123");

        assertEquals("CANCELLED", response.getStatus());
        assertTrue(vehicle.isAvailable());
    }

    @Test
    void getVehicleByIdNotFoundThrows() {
        when(vehicleRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> carRentalService.getVehicleById(UUID.randomUUID().toString()));
    }
}
