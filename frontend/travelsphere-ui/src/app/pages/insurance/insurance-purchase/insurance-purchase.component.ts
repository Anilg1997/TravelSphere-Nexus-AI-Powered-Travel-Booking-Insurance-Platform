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
import { NgFor, NgIf } from '@angular/common';
import { InsuranceService } from '../../../services/insurance.service';
import { PolicyType } from '../../../models/insurance.model';

@Component({
  selector: 'app-insurance-purchase',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, MatSnackBarModule, NgFor, NgIf],
  template: `
    <div class="page-container">
      <a routerLink="/insurance" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <div class="form-section" style="max-width:600px;margin:16px auto">
        <h2>Purchase Insurance</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Policy Type</mat-label>
            <mat-select formControlName="policyTypeId">
              <mat-option *ngFor="let p of policies" [value]="p.id">{{ p.name }} — ₹{{ p.basePremium }}</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Destination</mat-label><input matInput formControlName="destination" /></mat-form-field>
          <div class="form-row">
            <mat-form-field appearance="outline"><mat-label>Duration (days)</mat-label><input matInput type="number" formControlName="durationDays" /></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Your Age</mat-label><input matInput type="number" formControlName="travelerAge" /></mat-form-field>
          </div>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Start Date</mat-label><input matInput type="date" formControlName="startDate" /></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="form.invalid || loading">{{ loading ? 'Processing...' : 'Purchase Policy' }}</button>
        </form>
      </div>
    </div>
  `
})
export class InsurancePurchaseComponent {
  private fb = inject(FormBuilder);
  private insuranceService = inject(InsuranceService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  loading = false;
  policies: PolicyType[] = [];

  form = this.fb.nonNullable.group({
    policyTypeId: ['', Validators.required],
    destination: ['', Validators.required],
    durationDays: [7, Validators.min(1)],
    travelerAge: [30, Validators.min(1)],
    startDate: ['', Validators.required],
  });

  constructor() { this.insuranceService.getPolicies().subscribe(r => this.policies = r); }

  onSubmit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.insuranceService.purchase(this.form.getRawValue() as any).subscribe({
      next: () => { this.snackBar.open('Policy purchased!', 'Close', { duration: 5000 }); this.router.navigate(['/insurance']); },
      error: () => { this.loading = false; this.snackBar.open('Purchase failed.', 'Close', { duration: 3000 }); },
    });
  }
}
