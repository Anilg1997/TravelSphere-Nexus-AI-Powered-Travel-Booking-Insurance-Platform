import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-recommendations',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <div class="page-container">
      <a routerLink="/ai/chat" mat-button><mat-icon>arrow_back</mat-icon> Back</a>

      <h1 class="section-title" style="margin-top:16px">Personalized Recommendations</h1>
      <p class="section-subtitle">AI-powered travel suggestions just for you</p>

      <div class="responsive-grid">
        <mat-card class="feature-card">
          <mat-card-header>
            <mat-icon class="card-icon">explore</mat-icon>
            <mat-card-title>Top Destinations</mat-card-title>
            <mat-card-subtitle>Based on your preferences</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <ul style="padding-left:20px">
              <li>🌴 Goa - Beach paradise</li>
              <li>🏔️ Manali - Mountain retreat</li>
              <li>🏛️ Jaipur - Royal heritage</li>
              <li>🌊 Kerala - Backwater bliss</li>
              <li>🏖️ Andaman - Island escape</li>
            </ul>
          </mat-card-content>
        </mat-card>

        <mat-card class="feature-card">
          <mat-card-header>
            <mat-icon class="card-icon">local_offer</mat-icon>
            <mat-card-title>Best Deals</mat-card-title>
            <mat-card-subtitle>Current offers & discounts</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <ul style="padding-left:20px">
              <li>✈️ Flights up to 40% off</li>
              <li>🏨 Hotels starting ₹999/night</li>
              <li>🚗 Car rental 20% off first booking</li>
              <li>🛡️ Insurance at ₹199 only</li>
              <li>🎁 Package deals from ₹9,999</li>
            </ul>
          </mat-card-content>
        </mat-card>

        <mat-card class="feature-card">
          <mat-card-header>
            <mat-icon class="card-icon">trending_up</mat-icon>
            <mat-card-title>Trending Now</mat-card-title>
            <mat-card-subtitle>Popular among travelers</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <ul style="padding-left:20px">
              <li>🇯🇵 Japan cherry blossom season</li>
              <li>🇨🇭 Swiss Alps summer trips</li>
              <li>🇮🇩 Bali wellness retreats</li>
              <li>🇦🇪 Dubai shopping festival</li>
              <li>🇹🇭 Thailand beach holidays</li>
            </ul>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class RecommendationsComponent {}
