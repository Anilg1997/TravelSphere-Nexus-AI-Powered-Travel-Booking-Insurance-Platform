package com.travelsphere.transport.service;

import com.travelsphere.transport.dto.*;

import java.util.List;

public interface TransportService {
    List<TransportSearchResponse> searchRoutes(TransportSearchRequest request);
    TransportSearchResponse getRouteById(String id);
    TransportBookingResponse bookTransport(TransportBookingRequest request);
    TransportBookingResponse getBookingByRef(String ref);
    TransportBookingResponse cancelBooking(String ref);
    TransportBookingResponse getBookingByPnr(String pnr);
}
