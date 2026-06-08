package com.travelsphere.package_.service;

import com.travelsphere.package_.dto.*;
import com.travelsphere.package_.model.HolidayPackage;
import com.travelsphere.package_.model.PackageBooking;
import com.travelsphere.package_.model.PackageItinerary;
import com.travelsphere.package_.repository.HolidayPackageRepository;
import com.travelsphere.package_.repository.PackageBookingRepository;
import com.travelsphere.package_.repository.PackageItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final HolidayPackageRepository packageRepository;
    private final PackageBookingRepository bookingRepository;
    private final PackageItineraryRepository itineraryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public List<PackageSearchResponse> searchPackages(String destination) {
        List<HolidayPackage> packages = packageRepository.searchPackages(destination);
        return packages.stream().map(this::toSearchResponse).collect(Collectors.toList());
    }

    @Override
    public PackageSearchResponse getPackageById(String id) {
        HolidayPackage pkg = packageRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));
        return toSearchResponse(pkg);
    }

    @Override
    @Transactional
    public PackageBookingResponse bookPackage(PackageBookingRequest request, String userId) {
        HolidayPackage pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        if (!pkg.isActive()) {
            throw new IllegalStateException("Package is not available");
        }

        BigDecimal totalPrice = pkg.getPricePerPerson().multiply(BigDecimal.valueOf(request.getGroupSize()));
        String bookingRef = "TS-PKG-" + System.currentTimeMillis() % 1000000;

        PackageBooking booking = PackageBooking.builder()
                .bookingRef(bookingRef)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .packageId(pkg.getId())
                .travelDate(request.getTravelDate())
                .groupSize(request.getGroupSize())
                .totalPrice(totalPrice)
                .status("CONFIRMED")
                .bookedAt(LocalDateTime.now())
                .specialRequests(request.getSpecialRequests())
                .build();

        bookingRepository.save(booking);

        kafkaTemplate.send("ts.packages.booked", bookingRef, booking);

        return toBookingResponse(booking, pkg);
    }

    @Override
    public PackageBookingResponse getBookingByRef(String ref) {
        PackageBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        HolidayPackage pkg = packageRepository.findById(booking.getPackageId()).orElse(null);
        return toBookingResponse(booking, pkg);
    }

    @Override
    @Transactional
    public PackageBookingResponse cancelBooking(String ref) {
        PackageBooking booking = bookingRepository.findByBookingRef(ref)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        HolidayPackage pkg = packageRepository.findById(booking.getPackageId()).orElse(null);
        return toBookingResponse(booking, pkg);
    }

    private PackageSearchResponse toSearchResponse(HolidayPackage pkg) {
        List<PackageItinerary> itineraries = itineraryRepository.findByPackageIdOrderByDayNumber(pkg.getId());
        List<ItineraryDay> days = itineraries.stream().map(i -> ItineraryDay.builder()
                .dayNumber(i.getDayNumber())
                .dayTitle(i.getDayTitle())
                .description(i.getDescription())
                .activities(i.getActivities())
                .build()).collect(Collectors.toList());

        return PackageSearchResponse.builder()
                .id(pkg.getId())
                .packageName(pkg.getPackageName())
                .description(pkg.getDescription())
                .destination(pkg.getDestination())
                .durationDays(pkg.getDurationDays())
                .durationNights(pkg.getDurationNights())
                .pricePerPerson(pkg.getPricePerPerson())
                .maxGroupSize(pkg.getMaxGroupSize())
                .includedServices(pkg.getIncludedServices())
                .rating(pkg.getRating())
                .itinerary(days)
                .build();
    }

    private PackageBookingResponse toBookingResponse(PackageBooking booking, HolidayPackage pkg) {
        return PackageBookingResponse.builder()
                .bookingRef(booking.getBookingRef())
                .packageName(pkg != null ? pkg.getPackageName() : "Unknown")
                .destination(pkg != null ? pkg.getDestination() : "Unknown")
                .travelDate(booking.getTravelDate())
                .groupSize(booking.getGroupSize())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}
