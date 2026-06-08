import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-fraud-alerts',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatChipsModule, MatProgressSpinnerModule, NgFor, NgIf, DatePipe],
  template: `
    <div class="page-container" style="max-width:800px">
      <a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back to Dashboard</a>
      <h1 class="section-title" style="margin-top:16px">Fraud Alerts</h1>

      <div style="display:flex;gap:8px;margin-bottom:24px">
        <button mat-button (click)="loadAlerts()">All</button>
        <button mat-button (click)="loadAlerts('OPEN')">Open</button>
        <button mat-button (click)="loadAlerts('INVESTIGATING')">Investigating</button>
        <button mat-button (click)="loadAlerts('RESOLVED')">Resolved</button>
      </div>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="!loading && alerts.length === 0" class="empty-state">
        <mat-icon>verified</mat-icon><h3>All clear!</h3><p>No fraud alerts found.</p>
      </div>

      <div *ngFor="let a of alerts" style="margin-bottom:12px">
        <mat-card>
          <mat-card-header>
            <mat-icon style="color:var(--warn)">warning</mat-icon>
            <mat-card-title>{{ a.reason }}</mat-card-title>
            <span class="status-badge" [class.confirmed]="a.status === 'RESOLVED'" [class.pending]="a.status === 'OPEN'" [class.cancelled]="a.status === 'INVESTIGATING'">{{ a.status }}</span>
          </mat-card-header>
          <mat-card-content>
            <p>User: {{ a.userId }} · Risk Score: {{ a.riskScore }} · {{ a.createdAt | date:'short' }}</p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class FraudAlertsComponent {
  private adminService = inject(AdminService);
  loading = true;
  alerts: any[] = [];

  constructor() { this.loadAlerts(); }

  loadAlerts(status?: string) {
    this.loading = true;
    this.adminService.getFraudAlerts(status).subscribe(r => { this.alerts = r; this.loading = false; });
  }
}
