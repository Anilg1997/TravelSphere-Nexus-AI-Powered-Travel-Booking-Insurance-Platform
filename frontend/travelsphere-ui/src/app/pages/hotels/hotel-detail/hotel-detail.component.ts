import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { NgIf, CurrencyPipe, SlicePipe, DecimalPipe, DatePipe } from '@angular/common';
import { HotelService } from '../../../services/hotel.service';
import { Hotel, ReviewResponse } from '../../../models/hotel.model';

@Component({
  selector: 'app-hotel-detail',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatChipsModule, MatProgressSpinnerModule, MatDividerModule, NgIf, CurrencyPipe, SlicePipe, DecimalPipe, DatePipe],
  template: `
    <div class="page-container">
      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="hotel && !loading">
        <a routerLink="/hotels" mat-button><mat-icon>arrow_back</mat-icon> Back to hotels</a>

        <mat-card style="margin-top:16px">
          <mat-card-header>
            <mat-icon style="font-size:48px;width:48px;height:48px;color:var(--primary)">hotel</mat-icon>
            <mat-card-title style="font-size:1.8rem">{{ hotel.name }}</mat-card-title>
            <mat-card-subtitle>{{ '⭐'.repeat(hotel.starRating) }} · {{ hotel.city }}, {{ hotel.country }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p style="line-height:1.6">{{ hotel.description }}</p>
            <p><strong>Address:</strong> {{ hotel.address }}</p>

            <div *ngIf="hotel.amenities.length > 0">
              <h3>Amenities</h3>
              <mat-chip-set>
                <mat-chip *ngFor="let a of hotel.amenities">{{ a }}</mat-chip>
              </mat-chip-set>
            </div>

            <mat-divider style="margin:24px 0"></mat-divider>

            <div style="display:flex;justify-content:space-between;align-items:center">
              <div>
                <span style="font-size:0.9rem;color:#666">Starting from</span>
                <div style="font-size:2rem;font-weight:800;color:var(--primary)">₹{{ hotel.minPricePerNight | number }}/night</div>
                <span style="color:#666">⭐ {{ hotel.averageRating | number:'1.1-1' }} ({{ hotel.reviewCount }} reviews)</span>
              </div>
              <button mat-raised-button color="primary" class="cta-button" [routerLink]="['/hotels', hotel.id, 'book']">
                <mat-icon>book_online</mat-icon> Book Now
              </button>
            </div>
          </mat-card-content>
        </mat-card>

        <!-- Reviews Section -->
        <mat-card style="margin-top:24px">
          <mat-card-header><mat-card-title>Reviews</mat-card-title></mat-card-header>
          <mat-card-content>
            <div *ngIf="reviews.length === 0" class="empty-state"><p>No reviews yet.</p></div>
            <div *ngFor="let review of reviews" style="padding:12px 0;border-bottom:1px solid #eee">
              <div style="display:flex;justify-content:space-between">
                <strong>{{ '⭐'.repeat(review.rating) }}</strong>
                <span style="color:#999;font-size:0.85rem">{{ review.createdAt | date }}</span>
              </div>
              <p style="margin:8px 0 0">{{ review.reviewText }}</p>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class HotelDetailComponent {
  private route = inject(ActivatedRoute);
  private hotelService = inject(HotelService);

  hotel: Hotel | null = null;
  reviews: ReviewResponse[] = [];
  loading = true;

  constructor() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.hotelService.getById(id).subscribe(h => {
      this.hotel = h;
      this.loading = false;
    });
    this.hotelService.getReviews(id).subscribe(r => this.reviews = r);
  }
}
