import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-transport-booking',
  standalone: true,
  imports: [RouterLink, MatIconModule, MatButtonModule],
  template: `
    <div class="page-container">
      <a routerLink="/transport" mat-button><mat-icon>arrow_back</mat-icon> Back</a>
      <div class="empty-state">
        <mat-icon>construction</mat-icon>
        <h3>Transport Booking</h3>
        <p>This feature is coming soon. Please book via the search page.</p>
      </div>
    </div>
  `
})
export class TransportBookingComponent {}
