import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { PolicyType, PremiumCalculateRequest, PremiumResult, PurchaseRequest, InsuranceClaim } from '../models/insurance.model';

@Injectable({ providedIn: 'root' })
export class InsuranceService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/insurance`;

  getPolicies(): Observable<PolicyType[]> {
    return this.http.get<ApiResponse<PolicyType[]>>(`${this.baseUrl}/policies`)
      .pipe(map(r => r.data as PolicyType[] || []));
  }

  calculatePremium(request: PremiumCalculateRequest): Observable<PremiumResult> {
    return this.http.post<ApiResponse<PremiumResult>>(`${this.baseUrl}/calculate`, request)
      .pipe(map(r => r.data!));
  }

  purchase(request: PurchaseRequest): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/purchase`, request)
      .pipe(map(r => r.data!));
  }

  getClaims(): Observable<InsuranceClaim[]> {
    return this.http.get<ApiResponse<InsuranceClaim[]>>(`${this.baseUrl}/claims`)
      .pipe(map(r => r.data || []));
  }

  getClaim(id: string): Observable<InsuranceClaim> {
    return this.http.get<ApiResponse<InsuranceClaim>>(`${this.baseUrl}/claims/${id}`)
      .pipe(map(r => r.data!));
  }

  fileClaim(request: any): Observable<InsuranceClaim> {
    return this.http.post<ApiResponse<InsuranceClaim>>(`${this.baseUrl}/claims`, request)
      .pipe(map(r => r.data!));
  }
}
