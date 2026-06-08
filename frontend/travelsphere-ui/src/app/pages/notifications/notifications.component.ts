import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../models/notification.model';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, NgFor, NgIf, DatePipe],
  template: `
    <div class="page-container" style="max-width:700px">
      <h1 class="section-title">Notifications</h1>

      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="!loading && notifications.length === 0" class="empty-state">
        <mat-icon>notifications_none</mat-icon>
        <h3>No notifications</h3>
        <p>You're all caught up!</p>
      </div>

      <div *ngFor="let n of notifications" style="margin-bottom:8px">
        <mat-card [style.opacity]="n.isRead ? 0.6 : 1" (click)="markRead(n)">
          <mat-card-header>
            <mat-icon [style.color]="n.isRead ? '#999' : 'var(--primary)'">{{ getIcon(n.type) }}</mat-icon>
            <mat-card-title>{{ n.title }}</mat-card-title>
            <mat-card-subtitle>{{ n.createdAt | date:'short' }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content><p>{{ n.message }}</p></mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class NotificationsComponent {
  private notificationService = inject(NotificationService);
  loading = true;
  notifications: Notification[] = [];

  constructor() {
    this.notificationService.getNotifications('user').subscribe(n => {
      this.notifications = n;
      this.loading = false;
    });
  }

  getIcon(type: string): string {
    const icons: Record<string, string> = { BOOKING: 'book_online', PAYMENT: 'payment', SYSTEM: 'info', PROMO: 'local_offer' };
    return icons[type] || 'notifications';
  }

  markRead(n: Notification) {
    if (!n.isRead) {
      this.notificationService.markAsRead(n.id);
    }
  }
}
