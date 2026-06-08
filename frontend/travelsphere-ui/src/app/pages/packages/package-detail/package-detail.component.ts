import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { NgFor, NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { PackageService } from '../../../services/package.service';
import { HolidayPackage } from '../../../models/package.model';

@Component({
  selector: 'app-package-detail',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatChipsModule, MatProgressSpinnerModule, MatDividerModule, NgFor, NgIf, CurrencyPipe, DecimalPipe],
  styles: [`:host { display: block; }`],
  template: `
    <div class="page-container">
      <a routerLink="/packages" mat-button><mat-icon>arrow_back</mat-icon> Back</a>

      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="pkg && !loading">
        <mat-card style="margin-top:16px">
          <mat-card-header>
            <mat-icon style="font-size:48px;width:48px;height:48px;color:var(--primary)">card_giftcard</mat-icon>
            <mat-card-title style="font-size:1.8rem">{{ pkg.packageName }}</mat-card-title>
            <mat-card-subtitle>{{ pkg.destination }} · {{ pkg.durationDays }} Days / {{ pkg.durationNights }} Nights · ⭐ {{ pkg.rating }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p style="line-height:1.6">{{ pkg.description }}</p>

            <mat-chip-set><mat-chip *ngFor="let s of pkg.includedServices">{{ s }}</mat-chip></mat-chip-set>

            <mat-divider style="margin:24px 0"></mat-divider>

            <h3>Itinerary</h3>
            <div *ngFor="let day of pkg.itinerary" style="padding:12px 0;border-left:3px solid var(--primary);padding-left:16px;margin-bottom:12px">
              <strong>Day {{ day.dayNumber }}: {{ day.dayTitle }}</strong>
              <p style="margin:4px 0">{{ day.description }}</p>
              <div *ngIf="day.activities.length > 0">
                <mat-chip-set><mat-chip *ngFor="let a of day.activities">{{ a }}</mat-chip></mat-chip-set>
              </div>
            </div>

            <mat-divider style="margin:24px 0"></mat-divider>

            <div style="display:flex;justify-content:space-between;align-items:center">
              <div>
                <span style="font-size:0.9rem;color:#666">Per person</span>
                <div style="font-size:2rem;font-weight:800;color:var(--primary)">₹{{ pkg.pricePerPerson | number }}</div>
                <span style="color:#666">Max group: {{ pkg.maxGroupSize }} people</span>
              </div>
              <button mat-raised-button color="primary" class="cta-button" [routerLink]="['/packages', pkg.id, 'book']">
                <mat-icon>book_online</mat-icon> Book Now
              </button>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class PackageDetailComponent {
  private route = inject(ActivatedRoute);
  private packageService = inject(PackageService);
  pkg: HolidayPackage | null = null;
  loading = true;

  constructor() {
    this.packageService.getById(this.route.snapshot.paramMap.get('id')!).subscribe(p => {
      this.pkg = p;
      this.loading = false;
    });
  }
}
