import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { PaymentService } from '../../../services/payment.service';
import { PaymentResponse } from '../../../models/payment.model';

@Component({
  selector: 'app-payment-confirm',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, MatSnackBarModule, NgIf, CurrencyPipe, DecimalPipe],
  template: `
    <div class="page-container">
      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <mat-card *ngIf="payment && !loading" style="max-width:600px;margin:16px auto;text-align:center;padding:32px">
        <mat-icon style="font-size:72px;width:72px;height:72px;color:var(--success)">check_circle</mat-icon>
        <h2 style="margin:16px 0">Payment Confirmed!</h2>
        <p>Payment Reference: <strong>{{ payment.paymentRef }}</strong></p>
        <p>Transaction ID: {{ payment.transactionId }}</p>
        <p>Amount: <strong>₹{{ payment.finalAmount | number }}</strong></p>
        <p>Method: {{ payment.paymentMethod }} · Status: {{ payment.status }}</p>
        <div style="display:flex;gap:12px;justify-content:center;margin-top:24px">
          <button mat-raised-button color="primary" routerLink="/bookings">My Bookings</button>
          <button mat-button routerLink="/home">Home</button>
        </div>
      </mat-card>
    </div>
  `
})
export class PaymentConfirmComponent {
  private route = inject(ActivatedRoute);
  private paymentService = inject(PaymentService);
  loading = true;
  payment: PaymentResponse | null = null;

  constructor() {
    const ref = this.route.snapshot.paramMap.get('ref')!;
    this.paymentService.confirmPayment(ref).subscribe(p => {
      this.payment = p;
      this.loading = false;
    });
  }
}
