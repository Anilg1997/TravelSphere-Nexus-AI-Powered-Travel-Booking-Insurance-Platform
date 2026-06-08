import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf } from '@angular/common';
import { CarRentalService } from '../../../services/transport.service';

@Component({
  selector: 'app-car-booking',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSnackBarModule, NgIf],
  template: `
    <div class="page-container">
      <a routerLink="/cars" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Book Vehicle</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="form-row">
            <mat-form-field appearance="outline"><mat-label>Pickup Date</mat-label><input matInput type="date" formControlName="pickupDate" /></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Return Date</mat-label><input matInput type="date" formControlName="returnDate" /></mat-form-field>
          </div>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Pickup Location</mat-label><input matInput formControlName="pickupLocation" /></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="form.invalid || loading">{{ loading ? 'Booking...' : 'Confirm Booking' }}</button>
        </form>
      </div>
    </div>
  `
})
export class CarBookingComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private carService = inject(CarRentalService);
  private snackBar = inject(MatSnackBar);

  loading = false;
  form = this.fb.nonNullable.group({
    vehicleId: [this.route.snapshot.paramMap.get('id')!, Validators.required],
    pickupDate: ['', Validators.required],
    returnDate: ['', Validators.required],
    pickupLocation: ['', Validators.required],
  });

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.carService.book(this.form.getRawValue() as any).subscribe({
      next: (res) => { this.snackBar.open(`Booked! Ref: ${res.bookingRef}`, 'Close', { duration: 5000 }); this.router.navigate(['/bookings']); },
      error: () => { this.loading = false; this.snackBar.open('Booking failed.', 'Close', { duration: 3000 }); },
    });
  }
}
