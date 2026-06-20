import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../services/admin.service';
import { SupportTicket } from '../../../models/admin.model';

@Component({
  selector: 'app-support-tickets',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, DatePipe, MatCardModule, MatButtonModule, MatIconModule, MatTableModule, MatChipsModule, MatProgressSpinnerModule, MatFormFieldModule, MatSelectModule, MatDialogModule, FormsModule],
  template: `
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand"><mat-icon>shield</mat-icon><span>Admin Panel</span></div>
      <nav class="sidebar-nav">
        <a routerLink="/admin" class="nav-item"><mat-icon>dashboard</mat-icon> Dashboard</a>
        <a routerLink="/admin/users" class="nav-item"><mat-icon>people</mat-icon> Users</a>
        <a routerLink="/admin/bookings" class="nav-item"><mat-icon>book_online</mat-icon> Bookings</a>
        <a routerLink="/admin/analytics" class="nav-item"><mat-icon>analytics</mat-icon> Analytics</a>
        <a routerLink="/admin/tickets" class="nav-item active"><mat-icon>support</mat-icon> Support</a>
        <a routerLink="/admin/fraud-alerts" class="nav-item"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>Support Tickets</h1><p class="subtitle">Manage customer inquiries and support requests</p></div>
        <a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      </header>

      <mat-card class="filter-card">
        <mat-card-content>
          <div class="filters">
            <mat-form-field appearance="outline"><mat-label>Status</mat-label><mat-select [(ngModel)]="statusFilter" (selectionChange)="loadTickets()"><mat-option value="">All</mat-option><mat-option value="OPEN">Open</mat-option><mat-option value="IN_PROGRESS">In Progress</mat-option><mat-option value="RESOLVED">Resolved</mat-option><mat-option value="CLOSED">Closed</mat-option></mat-select></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Priority</mat-label><mat-select [(ngModel)]="priorityFilter" (selectionChange)="loadTickets()"><mat-option value="">All</mat-option><mat-option value="URGENT">Urgent</mat-option><mat-option value="HIGH">High</mat-option><mat-option value="MEDIUM">Medium</mat-option><mat-option value="LOW">Low</mat-option></mat-select></mat-form-field>
            <button mat-raised-button color="primary" (click)="loadTickets()"><mat-icon>search</mat-icon> Refresh</button>
          </div>
        </mat-card-content>
      </mat-card>

      <div *ngIf="loading" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="!loading">
        <table mat-table [dataSource]="tickets" class="data-table">
          <ng-container matColumnDef="subject"><th mat-header-cell *matHeaderCellDef>Subject</th><td mat-cell *matCellDef="let t">{{ t.subject }}</td></ng-container>
          <ng-container matColumnDef="category"><th mat-header-cell *matHeaderCellDef>Category</th><td mat-cell *matCellDef="let t"><span class="chip">{{ t.category }}</span></td></ng-container>
          <ng-container matColumnDef="priority"><th mat-header-cell *matHeaderCellDef>Priority</th><td mat-cell *matCellDef="let t">
            <span class="priority" [class.urgent]="t.priority==='URGENT'" [class.high]="t.priority==='HIGH'" [class.medium]="t.priority==='MEDIUM'" [class.low]="t.priority==='LOW'">{{ t.priority }}</span>
          </td></ng-container>
          <ng-container matColumnDef="status"><th mat-header-cell *matHeaderCellDef>Status</th><td mat-cell *matCellDef="let t">
            <span class="status-badge" [class.confirmed]="t.status==='RESOLVED'||t.status==='CLOSED'" [class.pending]="t.status==='OPEN'" [class.cancelled]="t.status==='IN_PROGRESS'">{{ t.status.replace('_',' ') }}</span>
          </td></ng-container>
          <ng-container matColumnDef="assigned"><th mat-header-cell *matHeaderCellDef>Assigned To</th><td mat-cell *matCellDef="let t">{{ t.assignedTo || 'Unassigned' }}</td></ng-container>
          <ng-container matColumnDef="created"><th mat-header-cell *matHeaderCellDef>Created</th><td mat-cell *matCellDef="let t">{{ t.createdAt | date:'medium' }}</td></ng-container>
          <ng-container matColumnDef="actions"><th mat-header-cell *matHeaderCellDef>Actions</th><td mat-cell *matCellDef="let t">
            <button mat-icon-button color="primary" (click)="resolveTicket(t)" matTooltip="Resolve"><mat-icon>check_circle</mat-icon></button>
          </td></ng-container>
          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
        <div *ngIf="tickets.length === 0" class="empty"><mat-icon>support</mat-icon><p>No tickets found</p></div>
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
    .filter-card { margin-bottom: 24px; border-radius: 16px !important; }
    .filters { display: flex; gap: 16px; align-items: center; flex-wrap: wrap; }
    .filters mat-form-field { min-width: 180px; }
    .data-table { width: 100%; background: white; border-radius: 16px; overflow: hidden; }
    .chip { padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; background: #e3f2fd; color: #1565c0; }
    .priority { padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; }
    .priority.urgent { background: #fce4ec; color: #c62828; }
    .priority.high { background: #fff3e0; color: #e65100; }
    .priority.medium { background: #fff8e1; color: #f9a825; }
    .priority.low { background: #e8f5e9; color: #2e7d32; }
    .status-badge { padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; }
    .status-badge.confirmed { background: #e8f5e9; color: #2e7d32; }
    .status-badge.pending { background: #fff3e0; color: #e65100; }
    .status-badge.cancelled { background: #e3f2fd; color: #1565c0; }
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .empty { text-align: center; padding: 60px; color: #999; }
    .empty mat-icon { font-size: 48px; width: 48px; height: 48px; }
  `]
})
export class SupportTicketsComponent {
  private adminService = inject(AdminService);
  tickets: SupportTicket[] = [];
  loading = true;
  statusFilter = '';
  priorityFilter = '';
  displayedColumns = ['subject', 'category', 'priority', 'status', 'assigned', 'created', 'actions'];

  constructor() { this.loadTickets(); }

  loadTickets() {
    this.loading = true;
    this.adminService.getTickets(this.statusFilter || undefined, this.priorityFilter || undefined).subscribe(r => {
      this.tickets = r; this.loading = false;
    });
  }

  resolveTicket(t: SupportTicket) {
    if (confirm(`Resolve ticket "${t.subject}"?`)) {
      this.adminService.resolveTicket(t.id, 'Issue resolved').subscribe(() => this.loadTickets());
    }
  }
}
