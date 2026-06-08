import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-bookings',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatTabsModule, NgFor],
  template: `
    <div class="page-container" style="max-width:800px">
      <h1 class="section-title">My Bookings</h1>
      <p class="section-subtitle">View and manage all your travel bookings</p>

      <mat-tab-group>
        <mat-tab label="All">
          <div class="empty-state"><mat-icon>book_online</mat-icon><h3>No bookings yet</h3><p>Start exploring and book your first trip!</p><button mat-raised-button color="primary" routerLink="/flights">Book a Flight</button></div>
        </mat-tab>
        <mat-tab label="Flights">
          <div class="empty-state"><mat-icon>flight</mat-icon><p>No flight bookings</p></div>
        </mat-tab>
        <mat-tab label="Hotels">
          <div class="empty-state"><mat-icon>hotel</mat-icon><p>No hotel bookings</p></div>
        </mat-tab>
        <mat-tab label="Packages">
          <div class="empty-state"><mat-icon>card_giftcard</mat-icon><p>No package bookings</p></div>
        </mat-tab>
      </mat-tab-group>
    </div>
  `
})
export class BookingsComponent {}
