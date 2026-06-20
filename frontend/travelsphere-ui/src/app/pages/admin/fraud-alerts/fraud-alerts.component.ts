import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AdminService } from '../../../services/admin.service';
import { FraudAlert } from '../../../models/admin.model';

@Component({
  selector: 'app-fraud-alerts',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, DatePipe, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  template: `
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand"><mat-icon>shield</mat-icon><span>Admin Panel</span></div>
      <nav class="sidebar-nav">
        <a routerLink="/admin" class="nav-item"><mat-icon>dashboard</mat-icon> Dashboard</a>
        <a routerLink="/admin/users" class="nav-item"><mat-icon>people</mat-icon> Users</a>
        <a routerLink="/admin/bookings" class="nav-item"><mat-icon>book_online</mat-icon> Bookings</a>
        <a routerLink="/admin/analytics" class="nav-item"><mat-icon>analytics</mat-icon> Analytics</a>
        <a routerLink="/admin/tickets" class="nav-item"><mat-icon>support</mat-icon> Support</a>
        <a routerLink="/admin/fraud-alerts" class="nav-item active"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>Fraud Alerts</h1><p class="subtitle">Monitor and investigate suspicious activities</p></div>
        <a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      </header>

      <div class="alert-summary">
        <mat-card class="glass-card" *ngFor="let s of alertSummary">
          <mat-card-content>
            <div class="summary-num">{{ s.count }}</div>
            <div class="summary-label">{{ s.label }}</div>
          </mat-card-content>
        </mat-card>
      </div>

      <div class="filter-tabs">
        <button mat-stroked-button (click)="loadAlerts()" [class.active]="!activeFilter">All</button>
        <button mat-stroked-button (click)="loadAlerts('OPEN')" [class.active]="activeFilter==='OPEN'">Open</button>
        <button mat-stroked-button (click)="loadAlerts('INVESTIGATING')" [class.active]="activeFilter==='INVESTIGATING'">Investigating</button>
        <button mat-stroked-button (click)="loadAlerts('RESOLVED')" [class.active]="activeFilter==='RESOLVED'">Resolved</button>
        <button mat-stroked-button (click)="loadAlerts('DISMISSED')" [class.active]="activeFilter==='DISMISSED'">Dismissed</button>
      </div>

      <div *ngIf="loading" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="!loading && alerts.length === 0" class="empty-state">
        <mat-icon>verified</mat-icon><h3>All Clear!</h3><p>No fraud alerts to review</p>
      </div>

      <div *ngFor="let a of alerts" class="alert-card">
        <mat-card class="glass-card" [class.critical]="a.severity==='CRITICAL'" [class.high]="a.severity==='HIGH'">
          <mat-card-content>
            <div class="alert-header">
              <div class="alert-type">
                <mat-icon>{{ a.severity === 'CRITICAL' || a.severity === 'HIGH' ? 'gavel' : 'info' }}</mat-icon>
                <div>
                  <div class="alert-title">{{ a.alertType }}</div>
                  <div class="alert-desc">{{ a.description }}</div>
                </div>
              </div>
              <div class="alert-meta">
                <span class="severity" [class.critical]="a.severity==='CRITICAL'" [class.high]="a.severity==='HIGH'" [class.medium]="a.severity==='MEDIUM'" [class.low]="a.severity==='LOW'">{{ a.severity }}</span>
                <span class="alert-status" [class.open]="a.status==='OPEN'" [class.investigating]="a.status==='INVESTIGATING'" [class.resolved]="a.status==='RESOLVED'">{{ a.status }}</span>
              </div>
            </div>
            <div class="alert-footer">
              <span class="alert-user">User: {{ a.userId }} </span>
              <span class="alert-time">{{ a.createdAt | date:'medium' }}</span>
              <span class="alert-ref" *ngIf="a.referenceId">Ref: {{ a.referenceId }}</span>
            </div>
            <div class="alert-actions" *ngIf="a.status !== 'RESOLVED' && a.status !== 'DISMISSED'">
              <button mat-raised-button color="primary" (click)="updateStatus(a, 'INVESTIGATING')">Investigate</button>
              <button mat-raised-button color="warn" (click)="updateStatus(a, 'RESOLVED')">Resolve</button>
              <button mat-button (click)="updateStatus(a, 'DISMISSED')">Dismiss</button>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </main>
  </div>`,
  styles: [`
    .admin-layout { display: flex; min-height: calc(100vh - 64px); background: #f0f2f5; }
    .admin-sidebar { width: 240px; background: #1a1a2e; padding: 24px 0; color: white; position: sticky; top: 64px; height: calc(100vh - 64px); }
    .sidebar-brand { display: flex; align-items: center; gap: 12px; padding: 0 20px 24px; font-size: 1.2rem; font-weight: 700; border-bottom: 1px solid rgba(255,255,255,0.1); margin-bottom: 16px; }
    .sidebar-nav { display: flex; flex-direction: column; gap: 4px; padding: 0 12px; }
    .nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-radius: 12px; color: rgba(255,255,255,0.7); text-decoration: none; transition: all 0.2s; font-weight: 500; }
    .nav-item:hover, .nav-item.active { background: rgba(255,255,255,0.1); color: white; }
    .nav-item.active { background: var(--primary); }
    .admin-main { flex: 1; padding: 24px 32px; overflow-y: auto; }
    .admin-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
    .admin-header h1 { margin: 0; font-size: 1.6rem; font-weight: 700; }
    .subtitle { color: #888; margin: 4px 0 0; }
    .alert-summary { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 24px; }
    .glass-card { background: rgba(255,255,255,0.9) !important; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2) !important; border-radius: 16px !important; }
    .glass-card mat-card-content { padding: 24px !important; }
    .summary-num { font-size: 2rem; font-weight: 800; color: var(--primary); }
    .summary-label { color: #888; font-size: 0.85rem; }
    .filter-tabs { display: flex; gap: 8px; margin-bottom: 24px; flex-wrap: wrap; }
    .filter-tabs button.active { background: var(--primary); color: white; }
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .empty-state { text-align: center; padding: 60px; color: #999; }
    .empty-state mat-icon { font-size: 64px; width: 64px; height: 64px; }
    .alert-card { margin-bottom: 16px; }
    .alert-card.critical { border-left: 4px solid #f44336; }
    .alert-card.high { border-left: 4px solid #ff9800; }
    .alert-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; }
    .alert-type { display: flex; gap: 16px; }
    .alert-type mat-icon { color: var(--warn); }
    .alert-title { font-weight: 600; margin-bottom: 4px; }
    .alert-desc { color: #666; font-size: 0.9rem; }
    .alert-meta { display: flex; gap: 8px; flex-shrink: 0; }
    .severity, .alert-status { padding: 2px 10px; border-radius: 12px; font-size: 0.75rem; font-weight: 600; }
    .severity.critical { background: #fce4ec; color: #c62828; }
    .severity.high { background: #fff3e0; color: #e65100; }
    .severity.medium { background: #fff8e1; color: #f9a825; }
    .severity.low { background: #e8f5e9; color: #2e7d32; }
    .alert-status.open { background: #fff3e0; color: #e65100; }
    .alert-status.investigating { background: #e3f2fd; color: #1565c0; }
    .alert-status.resolved { background: #e8f5e9; color: #2e7d32; }
    .alert-footer { display: flex; gap: 16px; margin-top: 12px; font-size: 0.8rem; color: #888; }
    .alert-actions { display: flex; gap: 8px; margin-top: 16px; padding-top: 12px; border-top: 1px solid #f0f0f0; }
  `]
})
export class FraudAlertsComponent {
  private adminService = inject(AdminService);
  loading = true;
  alerts: FraudAlert[] = [];
  activeFilter = '';
  alertSummary = [
    { label: 'Open Alerts', count: 0 },
    { label: 'Investigating', count: 0 },
    { label: 'Resolved', count: 0 },
    { label: 'Total', count: 0 }
  ];

  constructor() { this.loadAlerts(); }

  loadAlerts(status?: string) {
    this.loading = true;
    this.activeFilter = status || '';
    this.adminService.getFraudAlerts(status).subscribe(r => {
      this.alerts = r;
      this.alertSummary = [
        { label: 'Open Alerts', count: r.filter(a => a.status === 'OPEN').length },
        { label: 'Investigating', count: r.filter(a => a.status === 'INVESTIGATING').length },
        { label: 'Resolved', count: r.filter(a => a.status === 'RESOLVED').length },
        { label: 'Total', count: r.length }
      ];
      this.loading = false;
    });
  }

  updateStatus(alert: FraudAlert, status: string) {
    this.adminService.updateFraudAlert(alert.id, status).subscribe(() => this.loadAlerts(this.activeFilter));
  }
}
