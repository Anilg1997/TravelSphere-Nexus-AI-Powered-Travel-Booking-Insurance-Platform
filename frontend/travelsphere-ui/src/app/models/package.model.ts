export interface PackageItinerary {
  dayNumber: number;
  dayTitle: string;
  description: string;
  activities: string[];
}

export interface HolidayPackage {
  id: string;
  packageName: string;
  description: string;
  destination: string;
  durationDays: number;
  durationNights: number;
  pricePerPerson: number;
  maxGroupSize: number;
  includedServices: string[];
  rating: number;
  itinerary: PackageItinerary[];
}

export interface PackageBookingRequest {
  packageId: string;
  travelDate: string;
  groupSize: number;
  specialRequests?: string;
}

export interface PackageBookingResponse {
  bookingRef: string;
  packageName: string;
  destination: string;
  travelDate: string;
  groupSize: number;
  totalPrice: number;
  status: string;
  bookedAt: string;
}
