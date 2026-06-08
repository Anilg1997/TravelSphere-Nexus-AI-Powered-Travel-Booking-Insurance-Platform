import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf, NgFor } from '@angular/common';
import { FlightService } from '../../../services/flight.service';

@Component({
  selector: 'app-flight-booking',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, MatSnackBarModule, NgIf, NgFor],
  template: `
    <div class="page-container">
      <a routerLink="/flights" mat-button><mat-icon>arrow_back</mat-icon> Back</a>

      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Book Flight</h2>
        <p style="color:#666;margin-bottom:24px">Flight ID: {{ flightId }}</p>

        <form [formGroup]="bookingForm" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Passenger Name</mat-label>
            <input matInput formControlName="passengerName" placeholder="Enter full name" />
            <mat-error>Name is required</mat-error>
          </mat-form-field>

          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Email</mat-label>
            <input matInput type="email" formControlName="passengerEmail" placeholder="Enter email" />
            <mat-error>Valid email is required</mat-error>
          </mat-form-field>

          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>Seat Number</mat-label>
              <input matInput formControlName="seatNumber" placeholder="e.g., 12A" />
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Cabin Class</mat-label>
              <mat-select formControlName="cabinClass">
                <mat-option value="ECONOMY">Economy</mat-option>
                <mat-option value="PREMIUM_ECONOMY">Premium Economy</mat-option>
                <mat-option value="BUSINESS">Business</mat-option>
                <mat-option value="FIRST">First Class</mat-option>
              </mat-select>
            </mat-form-field>
          </div>

          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="bookingForm.invalid || loading">
            <mat-icon *ngIf="!loading">payment</mat-icon>
            {{ loading ? 'Processing...' : 'Confirm Booking & Proceed to Payment' }}
          </button>
        </form>
      </div>
    </div>
  `
})
export class FlightBookingComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private flightService = inject(FlightService);
  private snackBar = inject(MatSnackBar);

  flightId = this.route.snapshot.paramMap.get('id') || '';
  loading = false;

  bookingForm = this.fb.nonNullable.group({
    passengerName: ['', Validators.required],
    passengerEmail: ['', [Validators.required, Validators.email]],
    seatNumber: ['12A'],
    cabinClass: ['ECONOMY'],
  });

  onSubmit() {
    if (this.bookingForm.invalid) return;
    this.loading = true;
    const request = { flightId: this.flightId, ...this.bookingForm.getRawValue() };
    this.flightService.book(request).subscribe({
      next: (res) => {
        this.snackBar.open(`Booking confirmed! Ref: ${res.bookingRef}`, 'Close', { duration: 5000 });
        this.router.navigate(['/bookings']);
      },
      error: () => { this.loading = false; this.snackBar.open('Booking failed. Please try again.', 'Close', { duration: 3000 }); },
    });
  }
}
