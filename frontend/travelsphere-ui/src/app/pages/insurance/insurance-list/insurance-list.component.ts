import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgFor, NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { InsuranceService } from '../../../services/insurance.service';
import { PolicyType } from '../../../models/insurance.model';

@Component({
  selector: 'app-insurance-list',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, NgFor, NgIf, CurrencyPipe, DecimalPipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Travel Insurance</h1>
      <p class="section-subtitle">Protect your trip with comprehensive coverage</p>

      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div class="responsive-grid" *ngIf="!loading">
        <mat-card *ngFor="let p of policies" class="feature-card" style="text-align:center;padding:24px">
          <mat-icon style="font-size:48px;width:48px;height:48px;color:var(--primary);margin-bottom:16px">umbrella</mat-icon>
          <mat-card-title>{{ p.name }}</mat-card-title>
          <mat-card-content>
            <p>{{ p.description }}</p>
            <div style="font-size:2rem;font-weight:800;color:var(--primary);margin:16px 0">₹{{ p.basePremium | number }}</div>
            <p style="color:#666">Coverage: ₹{{ p.coverageAmount | number }} · {{ p.durationDays }} days</p>
            <button mat-raised-button color="primary" class="cta-button" routerLink="/insurance/purchase">
              <mat-icon>shopping_cart</mat-icon> Buy Now
            </button>
          </mat-card-content>
        </mat-card>
      </div>

      <div style="text-align:center;margin-top:24px">
        <button mat-button routerLink="/insurance/claims">View My Claims</button>
      </div>
    </div>
  `
})
export class InsuranceListComponent {
  private insuranceService = inject(InsuranceService);
  loading = true;
  policies: PolicyType[] = [];

  constructor() {
    this.insuranceService.getPolicies().subscribe(r => {
      this.policies = r;
      this.loading = false;
    });
  }
}
