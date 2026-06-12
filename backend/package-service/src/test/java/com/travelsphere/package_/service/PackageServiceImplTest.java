package com.travelsphere.package_.service;

import com.travelsphere.package_.dto.*;
import com.travelsphere.package_.model.HolidayPackage;
import com.travelsphere.package_.model.PackageBooking;
import com.travelsphere.package_.model.PackageItinerary;
import com.travelsphere.package_.repository.HolidayPackageRepository;
import com.travelsphere.package_.repository.PackageBookingRepository;
import com.travelsphere.package_.repository.PackageItineraryRepository;
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
class PackageServiceImplTest {

    @Mock private HolidayPackageRepository packageRepository;
    @Mock private PackageBookingRepository bookingRepository;
    @Mock private PackageItineraryRepository itineraryRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private PackageServiceImpl packageService;

    private UUID packageId;
    private HolidayPackage holidayPackage;

    @BeforeEach
    void setUp() {
        packageId = UUID.randomUUID();
        holidayPackage = HolidayPackage.builder()
                .id(packageId).packageName("Goa Beach Paradise")
                .description("5-day Goa beach vacation")
                .destination("Goa").durationDays(5).durationNights(4)
                .pricePerPerson(new BigDecimal("15000.00"))
                .maxGroupSize(10).rating(4.5).isActive(true).build();
    }

    @Test
    void searchPackagesReturnsResults() {
        when(packageRepository.searchPackages("Goa")).thenReturn(List.of(holidayPackage));
        when(itineraryRepository.findByPackageIdOrderByDayNumber(packageId)).thenReturn(List.of());

        List<PackageSearchResponse> results = packageService.searchPackages("Goa");

        assertEquals(1, results.size());
        assertEquals("Goa Beach Paradise", results.get(0).getPackageName());
    }

    @Test
    void bookPackageSuccess() {
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(holidayPackage));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(null);

        PackageBookingRequest request = PackageBookingRequest.builder()
                .packageId(packageId).travelDate(LocalDate.now().plusDays(30))
                .groupSize(4).specialRequests("Vegetarian food").build();

        PackageBookingResponse response = packageService.bookPackage(request, UUID.randomUUID().toString());

        assertNotNull(response);
        assertTrue(response.getBookingRef().startsWith("TS-PKG-"));
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals(new BigDecimal("60000.00"), response.getTotalPrice()); // 15000 * 4
        verify(kafkaTemplate).send(eq("ts.packages.booked"), anyString(), any());
    }

    @Test
    void bookPackageNotActiveThrows() {
        holidayPackage.setActive(false);
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(holidayPackage));

        PackageBookingRequest request = PackageBookingRequest.builder()
                .packageId(packageId).travelDate(LocalDate.now().plusDays(30))
                .groupSize(2).build();

        assertThrows(IllegalStateException.class,
                () -> packageService.bookPackage(request, null));
    }

    @Test
    void cancelBookingSetsCancelled() {
        PackageBooking booking = PackageBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-PKG-123").packageId(packageId)
                .status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-PKG-123")).thenReturn(Optional.of(booking));
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(holidayPackage));
        when(bookingRepository.save(any())).thenReturn(booking);

        PackageBookingResponse response = packageService.cancelBooking("TS-PKG-123");

        assertEquals("CANCELLED", response.getStatus());
    }

    @Test
    void getBookingByRefNotFoundThrows() {
        when(bookingRepository.findByBookingRef("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> packageService.getBookingByRef("INVALID"));
    }
}
