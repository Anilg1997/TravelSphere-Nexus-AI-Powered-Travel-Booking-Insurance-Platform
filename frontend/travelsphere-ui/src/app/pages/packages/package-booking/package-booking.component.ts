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
import { PackageService } from '../../../services/package.service';

@Component({
  selector: 'app-package-booking',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSnackBarModule, NgIf],
  template: `
    <div class="page-container">
      <a routerLink="/packages" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Book Package</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline"><mat-label>Travel Date</mat-label><input matInput type="date" formControlName="travelDate" /></mat-form-field>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Group Size</mat-label><input matInput type="number" formControlName="groupSize" min="1" /></mat-form-field>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Special Requests</mat-label><textarea matInput formControlName="specialRequests" rows="3"></textarea></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="form.invalid || loading">{{ loading ? 'Booking...' : 'Confirm Booking' }}</button>
        </form>
      </div>
    </div>
  `
})
export class PackageBookingComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private packageService = inject(PackageService);
  private snackBar = inject(MatSnackBar);

  loading = false;
  form = this.fb.nonNullable.group({
    packageId: [this.route.snapshot.paramMap.get('id')!, Validators.required],
    travelDate: ['', Validators.required],
    groupSize: [2, [Validators.required, Validators.min(1)]],
    specialRequests: [''],
  });

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.packageService.book(this.form.getRawValue() as any).subscribe({
      next: (res) => { this.snackBar.open(`Booked! Ref: ${res.bookingRef}`, 'Close', { duration: 5000 }); this.router.navigate(['/bookings']); },
      error: () => { this.loading = false; this.snackBar.open('Booking failed.', 'Close', { duration: 3000 }); },
    });
  }
}
