import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { NgFor, NgIf, CurrencyPipe, DatePipe, DecimalPipe } from '@angular/common';
import { FlightService } from '../../../services/flight.service';
import { Flight } from '../../../models/flight.model';

@Component({
  selector: 'app-flight-search',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, MatChipsModule, NgFor, NgIf, CurrencyPipe, DatePipe, DecimalPipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Search Flights</h1>
      <p class="section-subtitle">Find the best flight deals across 500+ airlines</p>

      <div class="form-section">
        <form [formGroup]="searchForm">
          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>From</mat-label>
              <input matInput formControlName="from" placeholder="City or Airport" />
              <mat-icon matIconPrefix>flight_takeoff</mat-icon>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>To</mat-label>
              <input matInput formControlName="to" placeholder="City or Airport" />
              <mat-icon matIconPrefix>flight_land</mat-icon>
            </mat-form-field>
          </div>
          <div class="form-row">
            <mat-form-field appearance="outline">
              <mat-label>Date</mat-label>
              <input matInput type="date" formControlName="date" />
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Cabin Class</mat-label>
              <mat-select formControlName="cabinClass">
                <mat-option value="">Any</mat-option>
                <mat-option value="ECONOMY">Economy</mat-option>
                <mat-option value="PREMIUM_ECONOMY">Premium Economy</mat-option>
                <mat-option value="BUSINESS">Business</mat-option>
                <mat-option value="FIRST">First Class</mat-option>
              </mat-select>
            </mat-form-field>
          </div>
          <button mat-raised-button color="primary" class="cta-button" (click)="search()" [disabled]="loading">
            <mat-icon>search</mat-icon> Search Flights
          </button>
        </form>
      </div>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="flights.length > 0 && !loading" class="responsive-grid" style="margin-top:24px">
        <mat-card *ngFor="let flight of flights" class="feature-card" [routerLink]="['/flights', flight.id]">
          <mat-card-header>
            <mat-icon class="card-icon">flight</mat-icon>
            <mat-card-title>{{ flight.airline }} · {{ flight.flightNumber }}</mat-card-title>
            <mat-card-subtitle>
              {{ flight.departureTime | date:'short' }} — {{ flight.arrivalTime | date:'short' }}
            </mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>{{ flight.durationMinutes }} min · {{ flight.aircraftType }}</p>
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span style="font-size:1.5rem;font-weight:700;color:var(--primary)">₹{{ flight.price | number }}</span>
              <span class="status-badge" [class.confirmed]="flight.availableSeats > 0" [class.cancelled]="flight.availableSeats === 0">
                {{ flight.availableSeats > 0 ? flight.availableSeats + ' seats left' : 'Sold out' }}
              </span>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class FlightSearchComponent {
  private fb = inject(FormBuilder);
  private flightService = inject(FlightService);

  loading = false;
  flights: Flight[] = [];

  searchForm = this.fb.group({
    from: [''],
    to: [''],
    date: [''],
    cabinClass: [''],
  });

  search() {
    this.loading = true;
    this.flightService.search(this.searchForm.value as any).subscribe(r => {
      this.flights = r;
      this.loading = false;
    });
  }
}
