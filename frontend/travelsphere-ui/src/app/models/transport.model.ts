export interface TransportRoute {
  id: string;
  routeName: string;
  from: string;
  to: string;
  departureTime: string;
  arrivalTime: string;
  price: number;
  availableSeats: number;
  vehicleType: string;
}

export interface TransportBookingRequest {
  routeId: string;
  passengerName: string;
  passengerEmail: string;
  seatCount: number;
}

export interface Vehicle {
  id: string;
  model: string;
  make: string;
  year: number;
  type: string;
  pricePerDay: number;
  available: boolean;
  location: string;
  imageUrl?: string;
  features: string[];
}

export interface CarBookingRequest {
  vehicleId: string;
  pickupDate: string;
  returnDate: string;
  pickupLocation: string;
  driverAge?: number;
}

export interface CarBookingResponse {
  bookingRef: string;
  vehicleModel: string;
  pickupDate: string;
  returnDate: string;
  totalPrice: number;
  status: string;
  bookedAt: string;
}
