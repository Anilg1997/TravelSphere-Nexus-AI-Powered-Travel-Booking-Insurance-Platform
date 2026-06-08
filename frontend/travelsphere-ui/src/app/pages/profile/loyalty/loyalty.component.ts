import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgIf } from '@angular/common';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-loyalty',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule, NgIf],
  template: `
    <div class="page-container" style="max-width:600px">
      <h1 class="section-title">Loyalty Points</h1>
      <mat-card style="text-align:center;padding:40px;background:linear-gradient(135deg,#667eea,#764ba2);color:white;margin-top:16px">
        <mat-icon style="font-size:64px;width:64px;height:64px">stars</mat-icon>
        <h2 style="margin:16px 0">Your Points Balance</h2>
        <div style="font-size:3rem;font-weight:800">{{ points }}</div>
        <p>Points earned from bookings & referrals</p>
      </mat-card>
      <mat-card style="margin-top:16px;padding:24px">
        <h3>How to Earn Points</h3>
        <ul style="line-height:2">
          <li>✨ Book flights — 100 points per booking</li>
          <li>🏨 Book hotels — 75 points per booking</li>
          <li>🎁 Refer friends — 500 points per referral</li>
          <li>🛡️ Buy insurance — 50 points per policy</li>
        </ul>
      </mat-card>
      <div style="text-align:center;margin-top:16px">
        <button mat-raised-button color="primary" routerLink="/referrals"><mat-icon>share</mat-icon> Refer & Earn</button>
      </div>
    </div>
  `
})
export class LoyaltyComponent {
  private userService = inject(UserService);
  points = 0;

  constructor() { this.userService.getLoyaltyPoints().subscribe((r: any) => this.points = r?.points || 1250); }
}
