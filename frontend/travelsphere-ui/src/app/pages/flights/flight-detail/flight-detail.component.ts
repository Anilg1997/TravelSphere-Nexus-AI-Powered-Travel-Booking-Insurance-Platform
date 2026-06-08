import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgIf, CurrencyPipe, DatePipe, DecimalPipe } from '@angular/common';
import { FlightService } from '../../../services/flight.service';
import { Flight } from '../../../models/flight.model';

@Component({
  selector: 'app-flight-detail',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatChipsModule, MatProgressSpinnerModule, NgIf, CurrencyPipe, DatePipe, DecimalPipe],
  template: `
    <div class="page-container">
      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="flight && !loading">
        <a routerLink="/flights" mat-button><mat-icon>arrow_back</mat-icon> Back to flights</a>

        <mat-card style="margin-top:16px">
          <mat-card-header>
            <mat-icon style="font-size:48px;width:48px;height:48px;color:var(--primary)">flight</mat-icon>
            <mat-card-title style="font-size:1.8rem">{{ flight.airline }} · {{ flight.flightNumber }}</mat-card-title>
            <mat-card-subtitle>{{ flight.aircraftType }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <div style="display:grid;grid-template-columns:1fr auto 1fr;gap:24px;align-items:center;padding:24px 0">
              <div style="text-align:right">
                <div style="font-size:2rem;font-weight:700">{{ flight.departureTime | date:'HH:mm' }}</div>
                <div style="color:#666">{{ flight.departureTime | date:'mediumDate' }}</div>
              </div>
              <div style="text-align:center">
                <div style="color:#999;margin-bottom:8px">{{ flight.durationMinutes }} min</div>
                <div style="border-top:2px dashed #ddd;width:120px;position:relative">
                  <mat-icon style="position:absolute;top:-12px;left:50%;transform:translateX(-50%);color:var(--primary)">flight</mat-icon>
                </div>
              </div>
              <div>
                <div style="font-size:2rem;font-weight:700">{{ flight.arrivalTime | date:'HH:mm' }}</div>
                <div style="color:#666">{{ flight.arrivalTime | date:'mediumDate' }}</div>
              </div>
            </div>

            <div style="display:flex;justify-content:space-between;align-items:center;padding-top:16px;border-top:1px solid #eee">
              <div>
                <div style="font-size:0.9rem;color:#666">Price per passenger</div>
                <div style="font-size:2rem;font-weight:800;color:var(--primary)">₹{{ flight.price | number }}</div>
              </div>
              <div style="text-align:right">
                <div class="status-badge" [class.confirmed]="flight.availableSeats > 0" [class.cancelled]="flight.availableSeats === 0">
                  {{ flight.availableSeats > 0 ? flight.availableSeats + ' seats available' : 'Sold out' }}
                </div>
                <button mat-raised-button color="primary" class="cta-button" style="margin-top:12px" [routerLink]="['/flights', flight.id, 'book']" [disabled]="flight.availableSeats === 0">
                  <mat-icon>book_online</mat-icon> Book Now
                </button>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class FlightDetailComponent {
  private route = inject(ActivatedRoute);
  private flightService = inject(FlightService);

  flight: Flight | null = null;
  loading = true;

  constructor() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.flightService.getById(id).subscribe(f => {
      this.flight = f;
      this.loading = false;
    });
  }
}
