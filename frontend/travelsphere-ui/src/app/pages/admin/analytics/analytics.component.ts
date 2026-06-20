import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { AdminService } from '../../../services/admin.service';
import { AnalyticsData } from '../../../models/admin.model';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, CurrencyPipe, DecimalPipe, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, MatButtonToggleModule],
  template: `
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand"><mat-icon>shield</mat-icon><span>Admin Panel</span></div>
      <nav class="sidebar-nav">
        <a routerLink="/admin" class="nav-item"><mat-icon>dashboard</mat-icon> Dashboard</a>
        <a routerLink="/admin/users" class="nav-item"><mat-icon>people</mat-icon> Users</a>
        <a routerLink="/admin/bookings" class="nav-item"><mat-icon>book_online</mat-icon> Bookings</a>
        <a routerLink="/admin/analytics" class="nav-item active"><mat-icon>analytics</mat-icon> Analytics</a>
        <a routerLink="/admin/tickets" class="nav-item"><mat-icon>support</mat-icon> Support</a>
        <a routerLink="/admin/fraud-alerts" class="nav-item"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>Analytics & Reports</h1><p class="subtitle">Platform performance metrics and trends</p></div>
        <div><a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back</a></div>
      </header>

      <div class="period-selector">
        <mat-button-toggle-group [(value)]="period" (change)="loadAnalytics()">
          <mat-button-toggle value="24h">24H</mat-button-toggle>
          <mat-button-toggle value="7d">7 Days</mat-button-toggle>
          <mat-button-toggle value="30d">30 Days</mat-button-toggle>
          <mat-button-toggle value="90d">90 Days</mat-button-toggle>
        </mat-button-toggle-group>
      </div>

      <div *ngIf="!data" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="data">
        <div class="analytics-grid">
          <mat-card class="glass-card">
            <mat-card-header><mat-icon>account_balance</mat-icon><mat-card-title>Revenue</mat-card-title></mat-card-header>
            <mat-card-content>
              <div class="big-num">{{ data.revenue.totalRevenue | currency:'INR' }}</div>
              <div class="metrics-row">
                <div><span class="label">Monthly</span><span class="val">{{ data.revenue.monthlyRevenue | currency:'INR' }}</span></div>
                <div><span class="label">Daily Avg</span><span class="val">{{ data.revenue.dailyAverage | currency:'INR' }}</span></div>
                <div><span class="label">Projected</span><span class="val">{{ data.revenue.projectedMonthly | currency:'INR' }}</span></div>
              </div>
            </mat-card-content>
          </mat-card>

          <mat-card class="glass-card">
            <mat-card-header><mat-icon>people</mat-icon><mat-card-title>Users</mat-card-title></mat-card-header>
            <mat-card-content>
              <div class="big-num">{{ data.users.totalUsers | number }}</div>
              <div class="metrics-row">
                <div><span class="label">New Today</span><span class="val">{{ data.users.newUsersToday }}</span></div>
                <div><span class="label">This Month</span><span class="val">{{ data.users.newUsersThisMonth }}</span></div>
                <div><span class="label">Growth Rate</span><span class="val">{{ data.users.growthRate }}%</span></div>
              </div>
            </mat-card-content>
          </mat-card>

          <mat-card class="glass-card">
            <mat-card-header><mat-icon>book_online</mat-icon><mat-card-title>Bookings</mat-card-title></mat-card-header>
            <mat-card-content>
              <div class="big-num">{{ data.bookings.totalBookings | number }}</div>
              <div class="metrics-row">
                <div><span class="label">Today</span><span class="val">{{ data.bookings.bookingsToday }}</span></div>
                <div><span class="label">Conversion</span><span class="val">{{ data.bookings.conversionRate }}%</span></div>
                <div><span class="label">Cancellation</span><span class="val">{{ data.bookings.cancellationRate | number:'1.1-1' }}%</span></div>
              </div>
            </mat-card-content>
          </mat-card>

          <mat-card class="glass-card full-width">
            <mat-card-header><mat-icon>pie_chart</mat-icon><mat-card-title>Service Distribution</mat-card-title></mat-card-header>
            <mat-card-content>
              <div class="bar-chart">
                <div class="bar-item" *ngFor="let item of serviceDistribution">
                  <div class="bar-label">{{ item.name }}</div>
                  <div class="bar-track"><div class="bar-fill" [style.width.%]="item.percent" [style.background]="item.color"></div></div>
                  <div class="bar-value">{{ item.count }}</div>
                </div>
              </div>
            </mat-card-content>
          </mat-card>
        </div>
      </div>
    </main>
  </div>`,
  styles: [`
    .admin-layout { display: flex; min-height: calc(100vh - 64px); background: #f0f2f5; }
    .admin-sidebar { width: 240px; background: #1a1a2e; padding: 24px 0; position: sticky; top: 64px; height: calc(100vh - 64px); }
    .admin-main { flex: 1; padding: 24px 32px; overflow-y: auto; }
    .admin-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .admin-header h1 { margin: 0; font-size: 1.6rem; font-weight: 700; }
    .subtitle { color: #888; margin: 4px 0 0; }
    .period-selector { margin-bottom: 24px; }
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .analytics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 20px; }
    .glass-card { background: rgba(255,255,255,0.9) !important; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2) !important; border-radius: 16px !important; }
    .full-width { grid-column: 1 / -1; }
    .big-num { font-size: 2.2rem; font-weight: 800; color: var(--primary); margin: 16px 0; }
    .metrics-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-top: 16px; }
    .label { display: block; font-size: 0.75rem; color: #888; margin-bottom: 4px; }
    .val { font-size: 1.1rem; font-weight: 700; }
    .bar-chart { display: flex; flex-direction: column; gap: 16px; padding: 16px 0; }
    .bar-item { display: flex; align-items: center; gap: 16px; }
    .bar-label { width: 100px; font-weight: 600; font-size: 0.9rem; }
    .bar-track { flex: 1; height: 24px; background: #f0f0f0; border-radius: 12px; overflow: hidden; }
    .bar-fill { height: 100%; border-radius: 12px; transition: width 1s ease; }
    .bar-value { width: 60px; text-align: right; font-weight: 700; }
  `]
})
export class AnalyticsComponent {
  private adminService = inject(AdminService);
  data: AnalyticsData | null = null;
  period = '7d';
  serviceDistribution: { name: string; count: number; percent: number; color: string }[] = [];
  colors = ['#3f51b5', '#ff4081', '#4caf50', '#ff9800', '#9c27b0', '#00bcd4'];

  constructor() { this.loadAnalytics(); }

  loadAnalytics() {
    this.adminService.getAnalytics(this.period).subscribe(d => {
      this.data = d;
      const total = Object.values(d.serviceDistribution).reduce((a, b) => a + b, 0);
      this.serviceDistribution = Object.entries(d.serviceDistribution).map(([k, v], i) => ({
        name: k, count: v,
        percent: total > 0 ? (v / total) * 100 : 0,
        color: this.colors[i % this.colors.length]
      }));
    });
  }
}
