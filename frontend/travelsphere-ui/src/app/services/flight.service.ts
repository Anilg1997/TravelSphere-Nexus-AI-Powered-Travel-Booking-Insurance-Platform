import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Flight, FlightSearchRequest, BookingRequest, BookingResponse } from '../models/flight.model';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/flights`;

  search(request: FlightSearchRequest): Observable<Flight[]> {
    return this.http.get<ApiResponse<Flight[]>>(`${this.baseUrl}/search`, { params: request as any })
      .pipe(map(r => r.data || []));
  }

  getById(id: string): Observable<Flight> {
    return this.http.get<ApiResponse<Flight>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }

  book(request: BookingRequest): Observable<BookingResponse> {
    return this.http.post<ApiResponse<BookingResponse>>(`${this.baseUrl}/book`, request)
      .pipe(map(r => r.data!));
  }

  getBooking(ref: string): Observable<BookingResponse> {
    return this.http.get<ApiResponse<BookingResponse>>(`${this.baseUrl}/booking/${ref}`)
      .pipe(map(r => r.data!));
  }

  cancelBooking(ref: string): Observable<BookingResponse> {
    return this.http.put<ApiResponse<BookingResponse>>(`${this.baseUrl}/cancel/${ref}`, {})
      .pipe(map(r => r.data!));
  }

  checkIn(ref: string): Observable<BookingResponse> {
    return this.http.post<ApiResponse<BookingResponse>>(`${this.baseUrl}/check-in/${ref}`, {})
      .pipe(map(r => r.data!));
  }
}
