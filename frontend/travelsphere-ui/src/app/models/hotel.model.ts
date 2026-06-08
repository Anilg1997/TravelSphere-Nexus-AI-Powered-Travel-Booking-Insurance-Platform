export interface HotelSearchRequest {
  city?: string;
  minStars?: number;
  checkIn?: string;
  checkOut?: string;
  guests?: number;
}

export interface Hotel {
  id: string;
  name: string;
  description: string;
  starRating: number;
  address: string;
  city: string;
  country: string;
  latitude: number;
  longitude: number;
  amenities: string[];
  imageS3Keys: string[];
  minPricePerNight: number;
  availableRooms: number;
  averageRating: number;
  reviewCount: number;
}

export interface HotelBookingRequest {
  hotelId: string;
  roomTypeId: string;
  checkInDate: string;
  checkOutDate: string;
  guests: number;
  specialRequests?: string;
}

export interface HotelBookingResponse {
  bookingRef: string;
  hotelName: string;
  roomTypeName: string;
  checkInDate: string;
  checkOutDate: string;
  guests: number;
  totalPrice: number;
  status: string;
  bookedAt: string;
  specialRequests?: string;
}

export interface ReviewRequest {
  rating: number;
  title?: string;
  reviewText: string;
}

export interface ReviewResponse {
  id: string;
  hotelId: string;
  userId?: string;
  rating: number;
  title?: string;
  reviewText: string;
  createdAt: string;
}
