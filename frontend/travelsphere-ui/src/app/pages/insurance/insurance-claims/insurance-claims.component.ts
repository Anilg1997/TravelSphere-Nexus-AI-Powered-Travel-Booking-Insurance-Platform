import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgFor, NgIf, DatePipe, CurrencyPipe, SlicePipe, DecimalPipe } from '@angular/common';
import { InsuranceService } from '../../../services/insurance.service';
import { InsuranceClaim } from '../../../models/insurance.model';

@Component({
  selector: 'app-insurance-claims',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, NgFor, NgIf, DatePipe, CurrencyPipe, SlicePipe, DecimalPipe],
  template: `
    <div class="page-container">
      <a routerLink="/insurance" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <h1 class="section-title" style="margin-top:16px">My Claims</h1>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="!loading && claims.length === 0" class="empty-state">
        <mat-icon>description</mat-icon><h3>No claims filed</h3>
      </div>

      <div *ngFor="let c of claims" style="margin-top:16px">
        <mat-card>
          <mat-card-header>
            <mat-card-title>{{ c.description | slice:0:50 }}{{ c.description.length > 50 ? '...' : '' }}</mat-card-title>
            <span class="status-badge" [class.confirmed]="c.status === 'RESOLVED'" [class.pending]="c.status === 'OPEN'" [class.cancelled]="c.status === 'REJECTED'">{{ c.status }}</span>
          </mat-card-header>
          <mat-card-content>
            <p>Amount: ₹{{ c.amount | number }} · Filed: {{ c.filedAt | date }}</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class InsuranceClaimsComponent {
  private insuranceService = inject(InsuranceService);
  loading = true;
  claims: InsuranceClaim[] = [];

  constructor() { this.insuranceService.getClaims().subscribe(r => { this.claims = r; this.loading = false; }); }
}
