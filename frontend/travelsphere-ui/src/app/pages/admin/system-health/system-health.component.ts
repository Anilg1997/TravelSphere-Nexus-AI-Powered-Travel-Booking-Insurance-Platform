import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, DatePipe, DecimalPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AdminService } from '../../../services/admin.service';
import { SystemHealth } from '../../../models/admin.model';

@Component({
  selector: 'app-system-health',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, DatePipe, DecimalPipe, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
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
        <a routerLink="/admin/fraud-alerts" class="nav-item"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item active"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>System Health</h1><p class="subtitle">Monitor microservices and infrastructure status</p></div>
        <button mat-raised-button color="primary" (click)="refresh()"><mat-icon>refresh</mat-icon> Refresh</button>
      </header>

      <div *ngIf="!health" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="health" class="health-grid">
        <mat-card class="glass-card overall" [class.healthy]="health.overallStatus === 'HEALTHY'">
          <mat-card-content>
            <div class="overall-status">
              <mat-icon>{{ health.overallStatus === 'HEALTHY' ? 'check_circle' : 'warning' }}</mat-icon>
              <div>
                <div class="status-text">{{ health.overallStatus }}</div>
                <div class="uptime">Uptime: {{ formatUptime(health.uptime) }}</div>
              </div>
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card class="glass-card">
          <mat-card-header><mat-icon>cloud</mat-icon><mat-card-title>Microservices</mat-card-title></mat-card-header>
          <mat-card-content>
            <div class="service-list">
              <div class="service-item" *ngFor="let svc of servicesList">
                <div class="service-name">{{ svc.name }}</div>
                <div class="service-status" [class.up]="svc.status==='UP'" [class.down]="svc.status!=='UP'">{{ svc.status }}</div>
                <div class="service-meta">{{ svc.responseTimeMs }}ms · {{ svc.instanceCount }} instance(s)</div>
              </div>
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card class="glass-card">
          <mat-card-header><mat-icon>storage</mat-icon><mat-card-title>Database</mat-card-title></mat-card-header>
          <mat-card-content>
            <div class="metric-item"><span>Status</span><span class="up">{{ health.database.status }}</span></div>
            <div class="metric-item"><span>Active Connections</span><span>{{ health.database.activeConnections }}/{{ health.database.totalConnections }}</span></div>
            <div class="metric-item"><span>Avg Query Time</span><span>{{ health.database.averageQueryTimeMs }}ms</span></div>
          </mat-card-content>
        </mat-card>

        <mat-card class="glass-card">
          <mat-card-header><mat-icon>memory</mat-icon><mat-card-title>Cache (Redis)</mat-card-title></mat-card-header>
          <mat-card-content>
            <div class="metric-item"><span>Status</span><span class="up">{{ health.cache.status }}</span></div>
            <div class="metric-item"><span>Hit Rate</span><span>{{ health.cache.hitRate }}%</span></div>
            <div class="metric-item"><span>Memory Usage</span><span>{{ health.cache.memoryUsage }} MB</span></div>
            <div class="metric-item"><span>Keys</span><span>{{ health.cache.keysCount | number }}</span></div>
          </mat-card-content>
        </mat-card>

        <mat-card class="glass-card">
          <mat-card-header><mat-icon>hub</mat-icon><mat-card-title>Messaging (Kafka)</mat-card-title></mat-card-header>
          <mat-card-content>
            <div class="metric-item"><span>Status</span><span class="up">{{ health.messaging.status }}</span></div>
            <div class="metric-item"><span>Msgs/Second</span><span>{{ health.messaging.messagesPerSecond }}</span></div>
            <div class="metric-item"><span>Consumer Lag</span><span>{{ health.messaging.consumerLag }}</span></div>
            <div class="metric-item"><span>Queue Depth</span><span>{{ health.messaging.queueDepth }}</span></div>
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
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .health-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(350px, 1fr)); gap: 20px; }
    .glass-card { background: rgba(255,255,255,0.9) !important; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.2) !important; border-radius: 16px !important; }
    .overall { grid-column: 1 / -1; }
    .overall.healthy mat-card-content { border-left: 4px solid #4caf50; }
    .overall-status { display: flex; align-items: center; gap: 16px; padding: 8px 0; }
    .overall-status mat-icon { font-size: 48px; width: 48px; height: 48px; color: #4caf50; }
    .status-text { font-size: 1.5rem; font-weight: 700; }
    .uptime { color: #888; font-size: 0.9rem; }
    .service-list { display: flex; flex-direction: column; gap: 12px; }
    .service-item { display: grid; grid-template-columns: 1fr auto 1fr; gap: 12px; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
    .service-name { font-weight: 600; }
    .service-status { padding: 2px 10px; border-radius: 12px; font-size: 0.75rem; font-weight: 600; text-align: center; }
    .service-status.up { background: #e8f5e9; color: #2e7d32; }
    .service-status.down { background: #fce4ec; color: #c62828; }
    .service-meta { text-align: right; font-size: 0.8rem; color: #888; }
    .metric-item { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
    .metric-item span:first-child { color: #666; }
    .metric-item .up { color: var(--success); font-weight: 600; }
  `]
})
export class SystemHealthComponent {
  private adminService = inject(AdminService);
  health: SystemHealth | null = null;
  servicesList: { name: string; status: string; responseTimeMs: number; instanceCount: number }[] = [];

  constructor() { this.refresh(); }

  refresh() {
    this.adminService.getSystemHealth().subscribe(h => {
      this.health = h;
      this.servicesList = Object.values(h.services);
    });
  }

  formatUptime(seconds: number): string {
    const d = Math.floor(seconds / 86400);
    const h = Math.floor((seconds % 86400) / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    return `${d}d ${h}h ${m}m`;
  }
}
