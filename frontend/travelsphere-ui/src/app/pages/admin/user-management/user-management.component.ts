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
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../services/admin.service';
import { UserManagementData } from '../../../models/admin.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [RouterLink, NgFor, NgIf, DatePipe, CurrencyPipe, MatCardModule, MatButtonModule, MatIconModule, MatTableModule, MatChipsModule, MatProgressSpinnerModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatTooltipModule, FormsModule],
  template: `
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand"><mat-icon>shield</mat-icon><span>Admin Panel</span></div>
      <nav class="sidebar-nav">
        <a routerLink="/admin" class="nav-item"><mat-icon>dashboard</mat-icon> Dashboard</a>
        <a routerLink="/admin/users" class="nav-item active"><mat-icon>people</mat-icon> Users</a>
        <a routerLink="/admin/bookings" class="nav-item"><mat-icon>book_online</mat-icon> Bookings</a>
        <a routerLink="/admin/analytics" class="nav-item"><mat-icon>analytics</mat-icon> Analytics</a>
        <a routerLink="/admin/tickets" class="nav-item"><mat-icon>support</mat-icon> Support</a>
        <a routerLink="/admin/fraud-alerts" class="nav-item"><mat-icon>warning</mat-icon> Fraud Alerts</a>
        <a routerLink="/admin/system-health" class="nav-item"><mat-icon>monitor_heart</mat-icon> System</a>
      </nav>
    </aside>
    <main class="admin-main">
      <header class="admin-header">
        <div><h1>User Management</h1><p class="subtitle">Manage all registered users</p></div>
        <a routerLink="/admin" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      </header>

      <mat-card class="filter-card">
        <mat-card-content>
          <div class="filters">
            <mat-form-field appearance="outline"><mat-label>Search</mat-label><input matInput [(ngModel)]="search" placeholder="Name or email..." (keyup.enter)="loadUsers()" /></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Role</mat-label><mat-select [(ngModel)]="roleFilter" (selectionChange)="loadUsers()"><mat-option value="">All</mat-option><mat-option value="USER">User</mat-option><mat-option value="ADMIN">Admin</mat-option></mat-select></mat-form-field>
            <button mat-raised-button color="primary" (click)="loadUsers()"><mat-icon>search</mat-icon> Search</button>
          </div>
        </mat-card-content>
      </mat-card>

      <div *ngIf="loading" class="loading-container"><mat-spinner diameter="40"></mat-spinner></div>

      <div *ngIf="!loading">
        <table mat-table [dataSource]="users" class="data-table">
          <ng-container matColumnDef="name"><th mat-header-cell *matHeaderCellDef>Name</th><td mat-cell *matCellDef="let u">{{ u.fullName }}</td></ng-container>
          <ng-container matColumnDef="email"><th mat-header-cell *matHeaderCellDef>Email</th><td mat-cell *matCellDef="let u">{{ u.email }}</td></ng-container>
          <ng-container matColumnDef="role"><th mat-header-cell *matHeaderCellDef>Role</th><td mat-cell *matCellDef="let u"><span class="chip" [class.admin]="u.role==='ADMIN'">{{ u.role }}</span></td></ng-container>
          <ng-container matColumnDef="status"><th mat-header-cell *matHeaderCellDef>Status</th><td mat-cell *matCellDef="let u"><span class="chip success" *ngIf="u.accountEnabled">Active</span><span class="chip warn" *ngIf="!u.accountEnabled">Disabled</span></td></ng-container>
          <ng-container matColumnDef="loyalty"><th mat-header-cell *matHeaderCellDef>Loyalty</th><td mat-cell *matCellDef="let u">{{ u.loyaltyTier }}</td></ng-container>
          <ng-container matColumnDef="bookings"><th mat-header-cell *matHeaderCellDef>Bookings</th><td mat-cell *matCellDef="let u">{{ u.totalBookings }}</td></ng-container>
          <ng-container matColumnDef="spent"><th mat-header-cell *matHeaderCellDef>Total Spent</th><td mat-cell *matCellDef="let u">{{ u.totalSpent | currency:'INR' }}</td></ng-container>
          <ng-container matColumnDef="joined"><th mat-header-cell *matHeaderCellDef>Joined</th><td mat-cell *matCellDef="let u">{{ u.createdAt | date:'mediumDate' }}</td></ng-container>
          <ng-container matColumnDef="actions"><th mat-header-cell *matHeaderCellDef>Actions</th><td mat-cell *matCellDef="let u">
            <button mat-icon-button color="warn" (click)="toggleStatus(u)" matTooltip="{{ u.accountEnabled ? 'Disable' : 'Enable' }}"><mat-icon>{{ u.accountEnabled ? 'block' : 'check_circle' }}</mat-icon></button>
          </td></ng-container>
          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
        <div *ngIf="users.length === 0" class="empty"><mat-icon>people_outline</mat-icon><p>No users found</p></div>
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
    .filters mat-form-field { min-width: 200px; }
    .data-table { width: 100%; background: white; border-radius: 16px; overflow: hidden; }
    .chip { padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; background: #e3f2fd; color: #1565c0; }
    .chip.admin { background: #fce4ec; color: #c62828; }
    .chip.success { background: #e8f5e9; color: #2e7d32; }
    .chip.warn { background: #fff3e0; color: #e65100; }
    .loading-container { display: flex; justify-content: center; padding: 60px; }
    .empty { text-align: center; padding: 60px; color: #999; }
    .empty mat-icon { font-size: 48px; width: 48px; height: 48px; }
  `]
})
export class UserManagementComponent {
  private adminService = inject(AdminService);
  users: UserManagementData[] = [];
  loading = true;
  search = '';
  roleFilter = '';
  displayedColumns = ['name', 'email', 'role', 'status', 'loyalty', 'bookings', 'spent', 'joined', 'actions'];

  constructor() { this.loadUsers(); }

  loadUsers() {
    this.loading = true;
    this.adminService.getUsers(this.search || undefined, this.roleFilter || undefined).subscribe(r => {
      this.users = r; this.loading = false;
    });
  }

  toggleStatus(user: UserManagementData) {
    this.adminService.updateUserStatus(user.id, !user.accountEnabled).subscribe(r => this.loadUsers());
  }
}
