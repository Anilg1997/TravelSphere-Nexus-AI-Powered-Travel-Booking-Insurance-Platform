import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Notification } from '../models/notification.model';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/notifications`;

  private unreadCount = new BehaviorSubject<number>(0);
  unreadCount$ = this.unreadCount.asObservable();

  getNotifications(userId: string): Observable<Notification[]> {
    return this.http.get<ApiResponse<Notification[]>>(`${this.baseUrl}`, {
      headers: { 'X-User-Id': userId }
    }).pipe(
      map(r => r.data || []),
      tap(notifs => this.unreadCount.next(notifs.filter(n => !n.isRead).length))
    );
  }

  markAsRead(id: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${id}/read`, {});
  }
}
