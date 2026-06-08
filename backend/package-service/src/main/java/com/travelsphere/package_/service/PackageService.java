package com.travelsphere.package_.service;

import com.travelsphere.package_.dto.*;

import java.util.List;

public interface PackageService {
    List<PackageSearchResponse> searchPackages(String destination);
    PackageSearchResponse getPackageById(String id);
    PackageBookingResponse bookPackage(PackageBookingRequest request, String userId);
    PackageBookingResponse getBookingByRef(String ref);
    PackageBookingResponse cancelBooking(String ref);
}
