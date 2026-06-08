import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf } from '@angular/common';
import { PaymentService } from '../../../services/payment.service';

@Component({
  selector: 'app-payment-init',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, MatSnackBarModule, NgIf],
  template: `
    <div class="page-container">
      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Make a Payment</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline"><mat-label>Booking Reference</mat-label><input matInput formControlName="bookingRef" placeholder="e.g., TS-FLT-123456" /></mat-form-field>
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Service Type</mat-label>
            <mat-select formControlName="serviceType">
              <mat-option value="FLIGHT">Flight</mat-option><mat-option value="HOTEL">Hotel</mat-option>
              <mat-option value="TRANSPORT">Transport</mat-option><mat-option value="CAR_RENTAL">Car Rental</mat-option>
              <mat-option value="PACKAGE">Package</mat-option><mat-option value="INSURANCE">Insurance</mat-option>
            </mat-select>
          </mat-form-field>
          <div class="form-row">
            <mat-form-field appearance="outline"><mat-label>Amount (₹)</mat-label><input matInput type="number" formControlName="amount" /></mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Payment Method</mat-label>
              <mat-select formControlName="paymentMethod">
                <mat-option value="CREDIT_CARD">Credit Card</mat-option><mat-option value="DEBIT_CARD">Debit Card</mat-option>
                <mat-option value="UPI">UPI</mat-option><mat-option value="WALLET">Wallet</mat-option>
                <mat-option value="NET_BANKING">Net Banking</mat-option>
              </mat-select>
            </mat-form-field>
          </div>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Promo Code (optional)</mat-label><input matInput formControlName="promoCode" /></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="form.invalid || loading">{{ loading ? 'Processing...' : 'Pay Now' }}</button>
        </form>
      </div>
    </div>
  `
})
export class PaymentInitComponent {
  private fb = inject(FormBuilder);
  private paymentService = inject(PaymentService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  loading = false;

  form = this.fb.nonNullable.group({
    bookingRef: ['', Validators.required],
    serviceType: ['FLIGHT'],
    amount: [0, [Validators.required, Validators.min(1)]],
    currency: ['INR'],
    paymentMethod: ['UPI', Validators.required],
    promoCode: [''],
  });

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.paymentService.initiatePayment(this.form.getRawValue() as any).subscribe({
      next: (res) => { this.snackBar.open(`Payment initiated! Ref: ${res.paymentRef}`, 'Close', { duration: 5000 }); this.router.navigate(['/payments/confirm', res.paymentRef]); },
      error: () => { this.loading = false; this.snackBar.open('Payment failed.', 'Close', { duration: 3000 }); },
    });
  }
}
