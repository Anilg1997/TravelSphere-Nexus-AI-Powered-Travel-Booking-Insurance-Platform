package com.travelsphere.hotel.service;

import com.travelsphere.hotel.dto.*;
import com.travelsphere.hotel.model.Hotel;
import com.travelsphere.hotel.model.HotelBooking;
import com.travelsphere.hotel.model.HotelReview;
import com.travelsphere.hotel.model.RoomType;
import com.travelsphere.hotel.repository.*;
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
class HotelServiceImplTest {

    @Mock private HotelRepository hotelRepository;
    @Mock private RoomTypeRepository roomTypeRepository;
    @Mock private HotelBookingRepository bookingRepository;
    @Mock private HotelReviewRepository reviewRepository;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private HotelServiceImpl hotelService;

    private UUID hotelId;
    private Hotel hotel;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        hotelId = UUID.randomUUID();
        hotel = Hotel.builder()
                .id(hotelId).name("Grand Hotel").description("Luxury hotel")
                .starRating(5).address("123 Main St").city("Mumbai").country("India")
                .latitude(BigDecimal.valueOf(19.0760)).longitude(BigDecimal.valueOf(72.8777))
                .amenities(new String[]{"pool", "spa"}).isActive(true).build();
        roomType = RoomType.builder()
                .id(UUID.randomUUID()).hotelId(hotelId).typeName("Deluxe")
                .basePricePerNight(new BigDecimal("5000.00")).availableRooms(5).build();
    }

    @Test
    void searchHotelsReturnsResults() {
        when(hotelRepository.searchHotels("Mumbai", null)).thenReturn(List.of(hotel));
        when(roomTypeRepository.findByHotelIdAndAvailableRoomsGreaterThan(hotelId, 0)).thenReturn(List.of(roomType));
        when(reviewRepository.averageRatingByHotelId(hotelId)).thenReturn(4.5);
        when(reviewRepository.countByHotelId(hotelId)).thenReturn(10);

        HotelSearchRequest request = HotelSearchRequest.builder().city("Mumbai").build();
        List<HotelSearchResponse> results = hotelService.searchHotels(request);

        assertEquals(1, results.size());
        assertEquals("Grand Hotel", results.get(0).getName());
        assertEquals(new BigDecimal("5000.00"), results.get(0).getMinPricePerNight());
    }

    @Test
    void bookHotelSuccess() {
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));
        when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(roomTypeRepository.save(any())).thenReturn(roomType);

        BookingRequest request = BookingRequest.builder()
                .hotelId(hotelId).roomTypeId(roomType.getId())
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .guests(2).build();

        BookingResponse response = hotelService.bookHotel(request, UUID.randomUUID().toString());

        assertNotNull(response);
        assertTrue(response.getBookingRef().startsWith("TS-HTL-"));
        assertEquals("CONFIRMED", response.getStatus());
        assertEquals(new BigDecimal("10000.00"), response.getTotalPrice()); // 2 nights * 5000
        assertEquals(4, roomType.getAvailableRooms()); // 5 - 1 (1 room booked for 2 nights)
        verify(kafkaTemplate).send(eq("ts.hotels.booked"), anyString(), any());
    }

    @Test
    void bookHotelNoRoomsAvailableThrows() {
        roomType.setAvailableRooms(0);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));

        BookingRequest request = BookingRequest.builder()
                .hotelId(hotelId).roomTypeId(roomType.getId())
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(2))
                .guests(1).build();

        assertThrows(IllegalStateException.class, () -> hotelService.bookHotel(request, null));
    }

    @Test
    void bookHotelCheckoutBeforeCheckinThrows() {
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));

        BookingRequest request = BookingRequest.builder()
                .hotelId(hotelId).roomTypeId(roomType.getId())
                .checkInDate(LocalDate.now().plusDays(3))
                .checkOutDate(LocalDate.now().plusDays(1))
                .guests(1).build();

        assertThrows(IllegalArgumentException.class, () -> hotelService.bookHotel(request, null));
    }

    @Test
    void cancelBookingRestoresRoom() {
        HotelBooking booking = HotelBooking.builder()
                .id(UUID.randomUUID()).bookingRef("TS-HTL-123").hotelId(hotelId)
                .roomTypeId(roomType.getId()).status("CONFIRMED").build();
        when(bookingRepository.findByBookingRef("TS-HTL-123")).thenReturn(Optional.of(booking));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(roomTypeRepository.save(any())).thenReturn(roomType);

        BookingResponse response = hotelService.cancelBooking("TS-HTL-123");

        assertEquals("CANCELLED", response.getStatus());
        assertEquals(6, roomType.getAvailableRooms()); // restored + 1
        verify(kafkaTemplate).send(eq("ts.hotels.cancelled"), anyString(), any());
    }

    @Test
    void addReviewSuccess() {
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(reviewRepository.save(any())).thenAnswer(inv -> {
            HotelReview r = inv.getArgument(0);
            r.setId(UUID.randomUUID());
            r.setCreatedAt(LocalDateTime.now());
            return r;
        });

        ReviewRequest request = ReviewRequest.builder()
                .rating(5).title("Great").reviewText("Amazing stay").build();

        ReviewResponse response = hotelService.addReview(hotelId.toString(), request);

        assertNotNull(response);
        assertEquals(5, response.getRating());
        assertEquals("Great", response.getTitle());
    }
}
