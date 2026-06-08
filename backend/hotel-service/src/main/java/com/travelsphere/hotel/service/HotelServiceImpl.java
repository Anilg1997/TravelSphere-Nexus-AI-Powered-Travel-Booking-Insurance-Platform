package com.travelsphere.hotel.service;

import com.travelsphere.hotel.dto.*;
import com.travelsphere.hotel.model.Hotel;
import com.travelsphere.hotel.model.HotelBooking;
import com.travelsphere.hotel.model.HotelReview;
import com.travelsphere.hotel.model.RoomType;
import com.travelsphere.hotel.repository.HotelBookingRepository;
import com.travelsphere.hotel.repository.HotelRepository;
import com.travelsphere.hotel.repository.HotelReviewRepository;
import com.travelsphere.hotel.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelBookingRepository bookingRepository;
    private final HotelReviewRepository reviewRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<HotelSearchResponse> searchHotels(HotelSearchRequest request) {
        List<Hotel> hotels = hotelRepository.searchHotels(request.getCity(), request.getMinStars());

        return hotels.stream().map(hotel -> {
            List<RoomType> roomTypes = roomTypeRepository.findByHotelIdAndAvailableRoomsGreaterThan(hotel.getId(), 0);
            BigDecimal minPrice = roomTypes.stream()
                    .map(RoomType::getBasePricePerNight)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            int availableRooms = roomTypes.stream().mapToInt(RoomType::getAvailableRooms).sum();
            double avgRating = reviewRepository.averageRatingByHotelId(hotel.getId());
            int reviewCount = reviewRepository.countByHotelId(hotel.getId());

            return HotelSearchResponse.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .description(hotel.getDescription())
                    .starRating(hotel.getStarRating())
                    .address(hotel.getAddress())
                    .city(hotel.getCity())
                    .country(hotel.getCountry())
                    .latitude(hotel.getLatitude())
                    .longitude(hotel.getLongitude())
                    .amenities(hotel.getAmenities())
                    .imageS3Keys(hotel.getImageS3Keys())
                    .minPricePerNight(minPrice)
                    .availableRooms(availableRooms)
                    .averageRating(avgRating)
                    .reviewCount(reviewCount)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public HotelSearchResponse getHotelById(String id) {
        Hotel hotel = hotelRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with id: " + id));
        return toSearchResponse(hotel);
    }

    @Override
    public List<HotelSearchResponse> getHotelsByCity(String city) {
        List<Hotel> hotels = hotelRepository.findByCityIgnoreCaseAndIsActiveTrue(city);
        return hotels.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse bookHotel(BookingRequest request, String userId) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Room type not found"));

        if (!roomType.getHotelId().equals(hotel.getId())) {
            throw new IllegalArgumentException("Room type does not belong to this hotel");
        }

        if (roomType.getAvailableRooms() < 1) {
            throw new IllegalStateException("No rooms available of this type");
        }

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (nights < 1) {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        BigDecimal totalPrice = roomType.getBasePricePerNight().multiply(BigDecimal.valueOf(nights));

        String bookingRef = "TS-HTL-" + System.currentTimeMillis() % 1000000;

        HotelBooking booking = HotelBooking.builder()
                .bookingRef(bookingRef)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .hotelId(hotel.getId())
                .roomTypeId(roomType.getId())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .guests(request.getGuests())
                .totalPrice(totalPrice)
                .status("CONFIRMED")
                .bookedAt(LocalDateTime.now())
                .specialRequests(request.getSpecialRequests())
                .build();

        bookingRepository.save(booking);

        roomType.setAvailableRooms(roomType.getAvailableRooms() - 1);
        roomTypeRepository.save(roomType);

        kafkaTemplate.send("ts.hotels.booked", bookingRef, booking);

        return toBookingResponse(booking, hotel.getName(), roomType.getTypeName());
    }

    @Override
    public BookingResponse getBookingByRef(String ref) {
        HotelBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + ref));

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        RoomType roomType = roomTypeRepository.findById(booking.getRoomTypeId()).orElse(null);

        return toBookingResponse(booking,
                hotel != null ? hotel.getName() : "Unknown",
                roomType != null ? roomType.getTypeName() : "Unknown");
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(String ref) {
        HotelBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + ref));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Restore room availability
        RoomType roomType = roomTypeRepository.findById(booking.getRoomTypeId()).orElse(null);
        if (roomType != null) {
            roomType.setAvailableRooms(roomType.getAvailableRooms() + 1);
            roomTypeRepository.save(roomType);
        }

        kafkaTemplate.send("ts.hotels.cancelled", ref, booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        return toBookingResponse(booking,
                hotel != null ? hotel.getName() : "Unknown",
                roomType != null ? roomType.getTypeName() : "Unknown");
    }

    @Override
    @Transactional
    public ReviewResponse addReview(String hotelId, ReviewRequest request) {
        Hotel hotel = hotelRepository.findById(UUID.fromString(hotelId))
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        HotelReview review = HotelReview.builder()
                .hotelId(hotel.getId())
                .rating(request.getRating())
                .title(request.getTitle())
                .reviewText(request.getReviewText())
                .build();

        review = reviewRepository.save(review);

        return ReviewResponse.builder()
                .id(review.getId())
                .hotelId(review.getHotelId())
                .rating(review.getRating())
                .title(review.getTitle())
                .reviewText(review.getReviewText())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    public List<ReviewResponse> getHotelReviews(String hotelId) {
        List<HotelReview> reviews = reviewRepository.findByHotelId(UUID.fromString(hotelId));
        return reviews.stream().map(review -> ReviewResponse.builder()
                .id(review.getId())
                .hotelId(review.getHotelId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .title(review.getTitle())
                .reviewText(review.getReviewText())
                .createdAt(review.getCreatedAt())
                .build()).collect(Collectors.toList());
    }

    private HotelSearchResponse toSearchResponse(Hotel hotel) {
        List<RoomType> roomTypes = roomTypeRepository.findByHotelIdAndAvailableRoomsGreaterThan(hotel.getId(), 0);
        BigDecimal minPrice = roomTypes.stream()
                .map(RoomType::getBasePricePerNight)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        int availableRooms = roomTypes.stream().mapToInt(RoomType::getAvailableRooms).sum();

        return HotelSearchResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .starRating(hotel.getStarRating())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .latitude(hotel.getLatitude())
                .longitude(hotel.getLongitude())
                .amenities(hotel.getAmenities())
                .imageS3Keys(hotel.getImageS3Keys())
                .minPricePerNight(minPrice)
                .availableRooms(availableRooms)
                .build();
    }

    private BookingResponse toBookingResponse(HotelBooking booking, String hotelName, String roomTypeName) {
        return BookingResponse.builder()
                .bookingRef(booking.getBookingRef())
                .hotelName(hotelName)
                .roomTypeName(roomTypeName)
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .guests(booking.getGuests())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .bookedAt(booking.getBookedAt())
                .specialRequests(booking.getSpecialRequests())
                .build();
    }
}
