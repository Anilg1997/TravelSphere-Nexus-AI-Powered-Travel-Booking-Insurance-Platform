package com.travelsphere.car.service;

import com.travelsphere.car.dto.*;

import java.util.List;

public interface CarRentalService {
    List<VehicleSearchResponse> searchVehicles(VehicleSearchRequest request);
    VehicleSearchResponse getVehicleById(String id);
    VehicleBookingResponse bookVehicle(VehicleBookingRequest request, String userId);
    VehicleBookingResponse getBookingByRef(String ref);
    VehicleBookingResponse cancelBooking(String ref);
}
