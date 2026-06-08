import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { PaymentInitRequest, PaymentResponse, WalletResponse, TopUpRequest, RefundRequest, RefundResponse } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/payments`;
  private walletUrl = `${environment.apiUrl}/wallet`;

  initiatePayment(request: PaymentInitRequest): Observable<PaymentResponse> {
    return this.http.post<ApiResponse<PaymentResponse>>(`${this.baseUrl}/initiate`, request)
      .pipe(map(r => r.data!));
  }

  confirmPayment(ref: string): Observable<PaymentResponse> {
    return this.http.post<ApiResponse<PaymentResponse>>(`${this.baseUrl}/confirm/${ref}`, {})
      .pipe(map(r => r.data!));
  }

  processRefund(request: RefundRequest): Observable<RefundResponse> {
    return this.http.post<ApiResponse<RefundResponse>>(`${this.baseUrl}/refund`, request)
      .pipe(map(r => r.data!));
  }

  getWalletBalance(): Observable<WalletResponse> {
    return this.http.get<ApiResponse<WalletResponse>>(`${this.walletUrl}/balance`)
      .pipe(map(r => r.data!));
  }

  topUpWallet(request: TopUpRequest): Observable<WalletResponse> {
    return this.http.post<ApiResponse<WalletResponse>>(`${this.walletUrl}/topup`, request)
      .pipe(map(r => r.data!));
  }
}
