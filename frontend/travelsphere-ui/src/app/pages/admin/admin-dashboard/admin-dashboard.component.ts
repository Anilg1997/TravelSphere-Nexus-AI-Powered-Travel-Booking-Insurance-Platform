import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, NgIf, CurrencyPipe, DecimalPipe],
  template: `
    <div class="page-container" style="max-width:1100px">
      <h1 class="section-title">Admin Dashboard</h1>
      <p class="section-subtitle">Platform analytics and management</p>

      <div *ngIf="!stats" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="stats">
        <div class="stats-grid">
          <mat-card class="stat-card"><div class="stat-value">{{ stats.totalUsers }}</div><div class="stat-label">Total Users</div></mat-card>
          <mat-card class="stat-card"><div class="stat-value">{{ stats.totalBookings }}</div><div class="stat-label">Total Bookings</div></mat-card>
          <mat-card class="stat-card"><div class="stat-value">₹{{ (stats.totalRevenue || 0) | number }}</div><div class="stat-label">Revenue</div></mat-card>
          <mat-card class="stat-card"><div class="stat-value">{{ stats.activeTrips }}</div><div class="stat-label">Active Trips</div></mat-card>
        </div>

        <div style="display:flex;gap:16px;margin-top:24px">
          <button mat-raised-button color="warn" routerLink="/admin/fraud-alerts">
            <mat-icon>warning</mat-icon> View Fraud Alerts
          </button>
        </div>
      </div>
    </div>
  `
})
export class AdminDashboardComponent {
  private adminService = inject(AdminService);
  stats: any = null;

  constructor() { this.adminService.getDashboard().subscribe(s => this.stats = s); }
}
