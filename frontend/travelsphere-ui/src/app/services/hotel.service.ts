import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Hotel, HotelBookingRequest, HotelBookingResponse, ReviewRequest, ReviewResponse } from '../models/hotel.model';

@Injectable({ providedIn: 'root' })
export class HotelService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/hotels`;

  search(city?: string, minStars?: number): Observable<Hotel[]> {
    let params = new HttpParams();
    if (city) params = params.set('city', city);
    if (minStars) params = params.set('minStars', minStars);
    return this.http.get<ApiResponse<Hotel[]>>(`${this.baseUrl}/search`, { params })
      .pipe(map(r => r.data || []));
  }

  getById(id: string): Observable<Hotel> {
    return this.http.get<ApiResponse<Hotel>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }

  getByCity(city: string): Observable<Hotel[]> {
    return this.http.get<ApiResponse<Hotel[]>>(`${this.baseUrl}/city/${city}`).pipe(map(r => r.data || []));
  }

  book(request: HotelBookingRequest): Observable<HotelBookingResponse> {
    return this.http.post<ApiResponse<HotelBookingResponse>>(`${this.baseUrl}/book`, request)
      .pipe(map(r => r.data!));
  }

  getBooking(ref: string): Observable<HotelBookingResponse> {
    return this.http.get<ApiResponse<HotelBookingResponse>>(`${this.baseUrl}/booking/${ref}`)
      .pipe(map(r => r.data!));
  }

  cancelBooking(ref: string): Observable<HotelBookingResponse> {
    return this.http.put<ApiResponse<HotelBookingResponse>>(`${this.baseUrl}/cancel/${ref}`, {})
      .pipe(map(r => r.data!));
  }

  addReview(hotelId: string, request: ReviewRequest): Observable<ReviewResponse> {
    return this.http.post<ApiResponse<ReviewResponse>>(`${this.baseUrl}/${hotelId}/reviews`, request)
      .pipe(map(r => r.data!));
  }

  getReviews(hotelId: string): Observable<ReviewResponse[]> {
    return this.http.get<ApiResponse<ReviewResponse[]>>(`${this.baseUrl}/${hotelId}/reviews`)
      .pipe(map(r => r.data || []));
  }
}
