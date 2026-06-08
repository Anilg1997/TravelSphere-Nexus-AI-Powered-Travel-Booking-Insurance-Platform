import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { TransportRoute, TransportBookingRequest, Vehicle, CarBookingRequest, CarBookingResponse } from '../models/transport.model';

@Injectable({ providedIn: 'root' })
export class TransportService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/transport`;

  search(from?: string, to?: string): Observable<TransportRoute[]> {
    let params = new HttpParams();
    if (from) params = params.set('from', from);
    if (to) params = params.set('to', to);
    return this.http.get<ApiResponse<TransportRoute[]>>(`${this.baseUrl}/search`, { params })
      .pipe(map(r => r.data || []));
  }

  getRoute(id: string): Observable<TransportRoute> {
    return this.http.get<ApiResponse<TransportRoute>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }

  book(request: TransportBookingRequest): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/book`, request).pipe(map(r => r.data!));
  }

  getBooking(ref: string): Observable<any> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/booking/${ref}`).pipe(map(r => r.data!));
  }
}

@Injectable({ providedIn: 'root' })
export class CarRentalService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/cars`;

  search(location?: string): Observable<Vehicle[]> {
    let params = new HttpParams();
    if (location) params = params.set('location', location);
    return this.http.get<ApiResponse<Vehicle[]>>(`${this.baseUrl}/search`, { params })
      .pipe(map(r => r.data || []));
  }

  getVehicle(id: string): Observable<Vehicle> {
    return this.http.get<ApiResponse<Vehicle>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }

  book(request: CarBookingRequest): Observable<CarBookingResponse> {
    return this.http.post<ApiResponse<CarBookingResponse>>(`${this.baseUrl}/book`, request)
      .pipe(map(r => r.data!));
  }

  getBooking(ref: string): Observable<CarBookingResponse> {
    return this.http.get<ApiResponse<CarBookingResponse>>(`${this.baseUrl}/booking/${ref}`)
      .pipe(map(r => r.data!));
  }
}
