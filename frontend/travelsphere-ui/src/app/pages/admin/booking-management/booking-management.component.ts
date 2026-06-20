import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, DatePipe, CurrencyPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../services/admin.service';
import { BookingManagementData } from '../../../models/admin.model';

@Component({
  selector: 'app-booking-management',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, DatePipe, CurrencyPipe, MatCardModule, MatButtonModule, MatIconModule, MatTableModule, MatChipsModule, MatProgressSpinnerModule, MatFormFieldModule, MatSelectModule, FormsModule],
  template: `
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand"><mat-icon>shield</mat-icon><span>Admin Panel</span></div>
      <nav class="sidebar-nav">
        <a routerLink="/admin" class="nav-item"><mat-icon>dashboard</mat-icon> Dashboard</a>
        <a routerLink="/admin/users" class="nav-item"><mat-icon>people</mat-icon> Users</a>
        <a routerLink="/admin/bookings" class="nav-item active"><mat-icon>book_online</mat-icon> Bookings</a>
        <a routerLink="/admin/analytics" class="nav-item"><mat-icon>analytics</mat-icon> Analytics</a>
        <a routerLink="/admin/tickets" class="nav-item"><mat-icon>support</mat-icon> Support</a>
        <a routerLink="/admin/fraud-alerts" class="nav-item"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>Booking Management</h1><p class="subtitle">View and manage all bookings across services</p></div>
        <a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      </header>

      <mat-card class="filter-card">
        <mat-card-content>
          <div class="filters">
            <mat-form-field appearance="outline"><mat-label>Service</mat-label><mat-select [(ngModel)]="serviceFilter" (selectionChange)="loadBookings()"><mat-option value="">All</mat-option><mat-option value="FLIGHT">Flights</mat-option><mat-option value="HOTEL">Hotels</mat-option><mat-option value="PACKAGE">Packages</mat-option><mat-option value="CAR">Cars</mat-option><mat-option value="INSURANCE">Insurance</mat-option></mat-select></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Status</mat-label><mat-select [(ngModel)]="statusFilter" (selectionChange)="loadBookings()"><mat-option value="">All</mat-option><mat-option value="CONFIRMED">Confirmed</mat-option><mat-option value="PENDING">Pending</mat-option><mat-option value="CANCELLED">Cancelled</mat-option><mat-option value="COMPLETED">Completed</mat-option></mat-select></mat-form-field>
            <button mat-raised-button color="primary" (click)="loadBookings()"><mat-icon>search</mat-icon> Search</button>
          </div>
        </mat-card-content>
      </mat-card>

      <div *ngIf="loading" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="!loading">
        <table mat-table [dataSource]="bookings" class="data-table">
          <ng-container matColumnDef="ref"><th mat-header-cell *matHeaderCellDef>Booking Ref</th><td mat-cell *matCellDef="let b">{{ b.bookingRef }}</td></ng-container>
          <ng-container matColumnDef="user"><th mat-header-cell *matHeaderCellDef>User</th><td mat-cell *matCellDef="let b">{{ b.userName }}<br><small>{{ b.userEmail }}</small></td></ng-container>
          <ng-container matColumnDef="service"><th mat-header-cell *matHeaderCellDef>Service</th><td mat-cell *matCellDef="let b"><span class="chip">{{ b.serviceType }}</span><br><small>{{ b.serviceName }}</small></td></ng-container>
          <ng-container matColumnDef="amount"><th mat-header-cell *matHeaderCellDef>Amount</th><td mat-cell *matCellDef="let b">{{ b.amount | currency:'INR' }}</td></ng-container>
          <ng-container matColumnDef="status"><th mat-header-cell *matHeaderCellDef>Status</th><td mat-cell *matCellDef="let b"><span class="status-badge" [class.confirmed]="b.status==='CONFIRMED'" [class.pending]="b.status==='PENDING'" [class.cancelled]="b.status==='CANCELLED'" [class.completed]="b.status==='COMPLETED'">{{ b.status }}</span></td></ng-container>
          <ng-container matColumnDef="payment"><th mat-header-cell *matHeaderCellDef>Payment</th><td mat-cell *matCellDef="let b"><span class="chip" [class.success]="b.paymentStatus==='PAID'" [class.warn]="b.paymentStatus==='PENDING'">{{ b.paymentStatus }}</span></td></ng-container>
          <ng-container matColumnDef="date"><th mat-header-cell *matHeaderCellDef>Date</th><td mat-cell *matCellDef="let b">{{ b.createdAt | date:'medium' }}</td></ng-container>
          <ng-container matColumnDef="actions"><th mat-header-cell *matHeaderCellDef>Actions</th><td mat-cell *matCellDef="let b">
            <button mat-icon-button color="warn" (click)="cancelBooking(b)" matTooltip="Cancel"><mat-icon>cancel</mat-icon></button>
            <button mat-icon-button color="primary" (click)="refundBooking(b)" matTooltip="Refund"><mat-icon>currency_rupee</mat-icon></button>
          </td></ng-container>
          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
        <div *ngIf="bookings.length === 0" class="empty"><mat-icon>receipt_long</mat-icon><p>No bookings found</p></div>
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
    .chip.success { background: #e8f5e9; color: #2e7d32; }
    .chip.warn { background: #fff3e0; color: #e65100; }
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .empty { text-align: center; padding: 60px; color: #999; }
    .empty mat-icon { font-size: 48px; width: 48px; height: 48px; }
    .status-badge { padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; }
    .status-badge.confirmed { background: #e8f5e9; color: #2e7d32; }
    .status-badge.pending { background: #fff3e0; color: #e65100; }
    .status-badge.cancelled { background: #fce4ec; color: #c62828; }
    .status-badge.completed { background: #e3f2fd; color: #1565c0; }
  `]
})
export class BookingManagementComponent {
  private adminService = inject(AdminService);
  bookings: BookingManagementData[] = [];
  loading = true;
  serviceFilter = '';
  statusFilter = '';
  displayedColumns = ['ref', 'user', 'service', 'amount', 'status', 'payment', 'date', 'actions'];

  constructor() { this.loadBookings(); }

  loadBookings() {
    this.loading = true;
    this.adminService.getBookings(this.serviceFilter || undefined, this.statusFilter || undefined).subscribe(r => {
      this.bookings = r; this.loading = false;
    });
  }

  cancelBooking(b: BookingManagementData) {
    if (confirm(`Cancel booking ${b.bookingRef}?`)) {
      this.adminService.cancelBooking(b.bookingRef).subscribe(() => this.loadBookings());
    }
  }

  refundBooking(b: BookingManagementData) {
    if (confirm(`Process refund for ${b.bookingRef}?`)) {
      this.adminService.refundBooking(b.bookingRef).subscribe(() => this.loadBookings());
    }
  }
}
