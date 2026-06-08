import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgFor, NgIf } from '@angular/common';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-referrals',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatSnackBarModule, NgFor, NgIf],
  template: `
    <div class="page-container" style="max-width:600px">
      <a routerLink="/loyalty" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <h1 class="section-title" style="margin-top:16px">Refer & Earn</h1>
      <p class="section-subtitle">Share TravelSphere with friends and earn 500 points each!</p>

      <mat-card style="padding:24px;text-align:center;margin-bottom:24px">
        <mat-icon style="font-size:48px;width:48px;height:48px;color:var(--primary)">share</mat-icon>
        <h2>Your Referral Link</h2>
        <mat-card style="background:#f5f7fa;padding:16px;margin:16px 0;word-break:break-all">
          <code>https://travelsphere.com/ref/{{ referralCode }}</code>
        </mat-card>
        <button mat-raised-button color="primary" class="cta-button" (click)="copyLink()">
          <mat-icon>content_copy</mat-icon> Copy Link
        </button>
      </mat-card>

      <h3>Your Referrals ({{ referrals.length }})</h3>
      <div *ngIf="referrals.length === 0" class="empty-state">
        <mat-icon>group_add</mat-icon>
        <p>No referrals yet. Share your link to start earning!</p>
      </div>
    </div>
  `
})
export class ReferralsComponent {
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);
  referralCode = 'TSPHERE' + Math.random().toString(36).substring(2, 6).toUpperCase();
  referrals: any[] = [];

  constructor() { this.userService.getReferrals().subscribe(r => this.referrals = r); }

  copyLink() {
    navigator.clipboard.writeText(`https://travelsphere.com/ref/${this.referralCode}`);
    this.snackBar.open('Link copied!', 'Close', { duration: 2000 });
  }
}
