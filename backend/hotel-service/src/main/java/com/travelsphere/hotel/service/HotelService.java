package com.travelsphere.hotel.service;

import com.travelsphere.hotel.dto.*;

import java.util.List;

public interface HotelService {
    List<HotelSearchResponse> searchHotels(HotelSearchRequest request);
    HotelSearchResponse getHotelById(String id);
    List<HotelSearchResponse> getHotelsByCity(String city);
    BookingResponse bookHotel(BookingRequest request, String userId);
    BookingResponse getBookingByRef(String ref);
    BookingResponse cancelBooking(String ref);
    ReviewResponse addReview(String hotelId, ReviewRequest request);
    List<ReviewResponse> getHotelReviews(String hotelId);
}
