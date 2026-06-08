import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { NgFor, NgIf, CurrencyPipe, SlicePipe, DecimalPipe } from '@angular/common';
import { PackageService } from '../../../services/package.service';
import { HolidayPackage } from '../../../models/package.model';

@Component({
  selector: 'app-package-list',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatProgressSpinnerModule, MatChipsModule, NgFor, NgIf, CurrencyPipe, SlicePipe, DecimalPipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Holiday Packages</h1>
      <p class="section-subtitle">All-in-one holiday deals at unbeatable prices</p>

      <div class="form-section" style="display:flex;gap:16px;align-items:center">
        <mat-form-field appearance="outline" style="flex:1">
          <mat-label>Destination</mat-label>
          <input matInput #dest placeholder="Search destinations" (keyup.enter)="search(dest.value)" />
          <mat-icon matIconPrefix>search</mat-icon>
        </mat-form-field>
        <button mat-raised-button color="primary" class="cta-button" (click)="search(dest.value)">Search</button>
      </div>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div class="responsive-grid" *ngIf="!loading">
        <mat-card *ngFor="let pkg of packages" class="feature-card" [routerLink]="['/packages', pkg.id]">
          <mat-card-header>
            <mat-icon class="card-icon">card_giftcard</mat-icon>
            <mat-card-title>{{ pkg.packageName }}</mat-card-title>
            <mat-card-subtitle>{{ pkg.destination }} · {{ pkg.durationDays }}D/{{ pkg.durationNights }}N</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>{{ pkg.description | slice:0:120 }}{{ pkg.description.length > 120 ? '...' : '' }}</p>
            <mat-chip-set><mat-chip *ngFor="let s of pkg.includedServices.slice(0,3)">{{ s }}</mat-chip></mat-chip-set>
            <div style="display:flex;justify-content:space-between;align-items:center;margin-top:16px">
              <div>
                <span style="font-size:0.8rem;color:#666">Starting from</span>
                <div style="font-size:1.5rem;font-weight:800;color:var(--primary)">₹{{ pkg.pricePerPerson | number }}/person</div>
              </div>
              <span style="color:#666">⭐ {{ pkg.rating }}</span>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class PackageListComponent {
  private packageService = inject(PackageService);
  loading = false;
  packages: HolidayPackage[] = [];

  constructor() { this.search(); }

  search(destination?: string) {
    this.loading = true;
    this.packageService.search(destination).subscribe(r => {
      this.packages = r;
      this.loading = false;
    });
  }
}
