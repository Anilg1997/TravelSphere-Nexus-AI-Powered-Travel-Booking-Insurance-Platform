import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { HolidayPackage, PackageBookingRequest, PackageBookingResponse } from '../models/package.model';

@Injectable({ providedIn: 'root' })
export class PackageService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/packages`;

  search(destination?: string): Observable<HolidayPackage[]> {
    let params = new HttpParams();
    if (destination) { params = params.set('destination', destination); }
    return this.http.get<ApiResponse<HolidayPackage[]>>(`${this.baseUrl}/search`, { params })
      .pipe(map(r => r.data || []));
  }

  getById(id: string): Observable<HolidayPackage> {
    return this.http.get<ApiResponse<HolidayPackage>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }

  book(request: PackageBookingRequest): Observable<PackageBookingResponse> {
    return this.http.post<ApiResponse<PackageBookingResponse>>(`${this.baseUrl}/book`, request)
      .pipe(map(r => r.data!));
  }

  getBooking(ref: string): Observable<PackageBookingResponse> {
    return this.http.get<ApiResponse<PackageBookingResponse>>(`${this.baseUrl}/booking/${ref}`)
      .pipe(map(r => r.data!));
  }
}
