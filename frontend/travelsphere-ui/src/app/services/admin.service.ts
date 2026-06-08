import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { DashboardStats, FraudAlert } from '../models/admin.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/admin`;

  getDashboard(): Observable<DashboardStats> {
    return this.http.get<ApiResponse<DashboardStats>>(`${this.baseUrl}/dashboard`)
      .pipe(map(r => r.data!));
  }

  getFraudAlerts(status?: string): Observable<FraudAlert[]> {
    return this.http.get<ApiResponse<FraudAlert[]>>(`${this.baseUrl}/fraud-alerts`, {
      params: status ? { status } : {}
    }).pipe(map(r => r.data || []));
  }
}
