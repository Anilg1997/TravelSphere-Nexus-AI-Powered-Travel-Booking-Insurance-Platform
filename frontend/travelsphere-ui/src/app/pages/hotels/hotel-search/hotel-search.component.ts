import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { NgFor, NgIf, CurrencyPipe, SlicePipe, DecimalPipe } from '@angular/common';
import { HotelService } from '../../../services/hotel.service';
import { Hotel } from '../../../models/hotel.model';

@Component({
  selector: 'app-hotel-search',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, MatChipsModule, NgFor, NgIf, CurrencyPipe, SlicePipe, DecimalPipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Find Hotels</h1>
      <p class="section-subtitle">Discover the perfect stay for your trip</p>

      <div class="form-section">
        <form [formGroup]="searchForm" class="form-row">
          <mat-form-field appearance="outline">
            <mat-label>City</mat-label>
            <input matInput formControlName="city" placeholder="Where are you going?" />
            <mat-icon matIconPrefix>location_city</mat-icon>
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Min Stars</mat-label>
            <mat-select formControlName="minStars">
              <mat-option [value]="0">Any</mat-option>
              <mat-option [value]="3">3 Stars</mat-option>
              <mat-option [value]="4">4 Stars</mat-option>
              <mat-option [value]="5">5 Stars</mat-option>
            </mat-select>
          </mat-form-field>
        </form>
        <button mat-raised-button color="primary" class="cta-button" (click)="search()" [disabled]="loading">
          <mat-icon>search</mat-icon> Search Hotels
        </button>
      </div>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="hotels.length > 0 && !loading" class="responsive-grid" style="margin-top:24px">
        <mat-card *ngFor="let hotel of hotels" class="feature-card" [routerLink]="['/hotels', hotel.id]">
          <mat-card-header>
            <mat-icon class="card-icon">hotel</mat-icon>
            <mat-card-title>{{ hotel.name }}</mat-card-title>
            <mat-card-subtitle>{{ hotel.city }}, {{ hotel.country }} · {{ '⭐'.repeat(hotel.starRating) }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>{{ hotel.description | slice:0:100 }}{{ hotel.description.length > 100 ? '...' : '' }}</p>
            <mat-chip-set>
              <mat-chip *ngFor="let amenity of hotel.amenities.slice(0,3)">{{ amenity }}</mat-chip>
            </mat-chip-set>
            <div style="display:flex;justify-content:space-between;align-items:center;margin-top:12px">
              <span style="font-size:1.3rem;font-weight:700;color:var(--primary)">₹{{ hotel.minPricePerNight | number }}/night</span>
              <span style="color:#666">⭐ {{ hotel.averageRating | number:'1.1-1' }} ({{ hotel.reviewCount }})</span>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class HotelSearchComponent {
  private fb = inject(FormBuilder);
  private hotelService = inject(HotelService);

  loading = false;
  hotels: Hotel[] = [];

  searchForm = this.fb.group({ city: [''], minStars: [0] });

  search() {
    this.loading = true;
    const { city, minStars } = this.searchForm.value;
    this.hotelService.search(city || undefined, minStars || undefined).subscribe(r => {
      this.hotels = r;
      this.loading = false;
    });
  }
}
