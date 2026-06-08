export interface FlightSearchRequest {
  from?: string;
  to?: string;
  date?: string;
  passengers?: number;
  cabinClass?: string;
}

export interface Flight {
  id: string;
  flightNumber: string;
  airline: string;
  departureTime: string;
  arrivalTime: string;
  durationMinutes: number;
  aircraftType: string;
  price: number;
  availableSeats: number;
  from?: string;
  to?: string;
}

export interface BookingRequest {
  flightId: string;
  passengerName: string;
  passengerEmail: string;
  seatNumber: string;
  cabinClass: string;
}

export interface BookingResponse {
  bookingRef: string;
  pnr: string;
  passengerName: string;
  seatNumber: string;
  cabinClass: string;
  pricePaid: number;
  status: string;
  bookedAt: string;
}
