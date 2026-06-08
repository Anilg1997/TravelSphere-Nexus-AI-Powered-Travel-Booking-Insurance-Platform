import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf } from '@angular/common';
import { HotelService } from '../../../services/hotel.service';

@Component({
  selector: 'app-hotel-booking',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatProgressSpinnerModule, MatSnackBarModule, NgIf],
  template: `
    <div class="page-container">
      <a routerLink="/hotels" mat-button><mat-icon>arrow_back</mat-icon> Back</a>

      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Book Hotel</h2>
        <form [formGroup]="bookingForm" (ngSubmit)="onSubmit()">
          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>Check-in Date</mat-label>
              <input matInput type="date" formControlName="checkInDate" />
              <mat-error>Required</mat-error>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Check-out Date</mat-label>
              <input matInput type="date" formControlName="checkOutDate" />
              <mat-error>Required</mat-error>
            </mat-form-field>
          </div>
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Guests</mat-label>
            <input matInput type="number" formControlName="guests" min="1" />
          </mat-form-field>
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Special Requests (optional)</mat-label>
            <textarea matInput formControlName="specialRequests" rows="3"></textarea>
          </mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="bookingForm.invalid || loading">
            {{ loading ? 'Processing...' : 'Confirm Booking' }}
          </button>
        </form>
      </div>
    </div>
  `
})
export class HotelBookingComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private hotelService = inject(HotelService);
  private snackBar = inject(MatSnackBar);

  hotelId = this.route.snapshot.paramMap.get('id') || '';
  loading = false;

  bookingForm = this.fb.nonNullable.group({
    hotelId: [this.hotelId],
    roomTypeId: [''],
    checkInDate: ['', Validators.required],
    checkOutDate: ['', Validators.required],
    guests: [2, [Validators.required, Validators.min(1)]],
    specialRequests: [''],
  });

  onSubmit() {
    if (this.bookingForm.invalid) return;
    this.loading = true;
    this.hotelService.book(this.bookingForm.getRawValue() as any).subscribe({
      next: (res) => { this.snackBar.open(`Booked! Ref: ${res.bookingRef}`, 'Close', { duration: 5000 }); this.router.navigate(['/bookings']); },
      error: () => { this.loading = false; this.snackBar.open('Booking failed.', 'Close', { duration: 3000 }); },
    });
  }
}
