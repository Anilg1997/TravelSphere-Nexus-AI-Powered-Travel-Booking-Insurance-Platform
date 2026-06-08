import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { NgFor, NgIf, CurrencyPipe, DecimalPipe, TitleCasePipe } from '@angular/common';
import { CarRentalService } from '../../../services/transport.service';
import { Vehicle } from '../../../models/transport.model';

@Component({
  selector: 'app-car-search',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatProgressSpinnerModule, MatChipsModule, NgFor, NgIf, CurrencyPipe, DecimalPipe, TitleCasePipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Car Rental</h1>
      <p class="section-subtitle">Find the perfect vehicle for your journey</p>

      <div class="form-section">
        <div style="display:flex;gap:16px;align-items:flex-start">
          <mat-form-field appearance="outline" style="flex:1">
            <mat-label>Pickup Location</mat-label>
            <input matInput #location placeholder="City or airport" (keyup.enter)="search(location.value)" />
            <mat-icon matIconPrefix>location_on</mat-icon>
          </mat-form-field>
          <button mat-raised-button color="primary" class="cta-button" (click)="search(location.value)" [disabled]="loading">
            <mat-icon>search</mat-icon> Search
          </button>
        </div>
      </div>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="vehicles.length > 0 && !loading" class="responsive-grid" style="margin-top:24px">
        <mat-card *ngFor="let v of vehicles" class="feature-card">
          <mat-card-header>
            <mat-icon class="card-icon">directions_car</mat-icon>
            <mat-card-title>{{ v.make }} {{ v.model }} ({{ v.year }})</mat-card-title>
            <mat-card-subtitle>{{ v.type | titlecase }} · {{ v.location }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <mat-chip-set><mat-chip *ngFor="let f of v.features.slice(0,3)">{{ f }}</mat-chip></mat-chip-set>
            <div style="display:flex;justify-content:space-between;align-items:center;margin-top:12px">
              <span style="font-size:1.3rem;font-weight:700;color:var(--primary)">₹{{ v.pricePerDay | number }}/day</span>
              <button mat-raised-button color="primary" [routerLink]="['/cars', v.id, 'book']" [disabled]="!v.available">Book</button>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class CarSearchComponent {
  private carService = inject(CarRentalService);
  loading = false;
  vehicles: Vehicle[] = [];

  search(location: string) {
    this.loading = true;
    this.carService.search(location || undefined).subscribe(r => {
      this.vehicles = r;
      this.loading = false;
    });
  }
}
