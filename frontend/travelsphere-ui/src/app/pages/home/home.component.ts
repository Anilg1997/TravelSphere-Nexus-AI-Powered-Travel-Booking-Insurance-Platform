import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, MatButtonModule, MatIconModule, MatCardModule, MatFormFieldModule, MatInputModule, NgFor],
  template: `
    <!-- Hero Section -->
    <section class="hero-section">
      <h1>Explore the World with TravelSphere</h1>
      <p>Book flights, hotels, cars, and holiday packages — all powered by AI</p>
      <div class="search-bar">
        <mat-form-field appearance="fill">
          <mat-label>Where do you want to go?</mat-label>
          <input matInput #searchInput placeholder="City, destination, or activity..." (keyup.enter)="searchGlobal(searchInput.value)" />
          <mat-icon matIconPrefix>search</mat-icon>
        </mat-form-field>
        <button mat-raised-button color="accent" class="cta-button" (click)="searchGlobal(searchInput.value)">
          <mat-icon>search</mat-icon> Search
        </button>
      </div>
    </section>

    <div class="page-container">
      <!-- Quick Services -->
      <h2 class="section-title">Book Your Trip</h2>
      <p class="section-subtitle">Everything you need for your journey</p>
      <div class="responsive-grid">
        <mat-card class="feature-card" *ngFor="let service of services" [routerLink]="service.link">
          <mat-card-header>
            <mat-icon class="card-icon">{{ service.icon }}</mat-icon>
            <mat-card-title>{{ service.title }}</mat-card-title>
            <mat-card-subtitle>{{ service.subtitle }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>{{ service.description }}</p>
          </mat-card-content>
        </mat-card>
      </div>

      <!-- Stats -->
      <div class="stats-grid">
        <mat-card class="stat-card" *ngFor="let stat of stats">
          <div class="stat-value">{{ stat.value }}{{ stat.suffix }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </mat-card>
      </div>

      <!-- AI Travel Assistant -->
      <mat-card class="ai-cta-card">
        <mat-card-content>
          <div class="ai-cta-content">
            <div class="ai-cta-text">
              <h2>🤖 AI Travel Assistant</h2>
              <p>Let our AI plan your perfect trip — get personalized recommendations, itineraries, and real-time travel advice.</p>
              <button mat-raised-button color="accent" class="cta-button" routerLink="/ai/chat">
                <mat-icon>chat</mat-icon> Chat with AI
              </button>
              <button mat-raised-button class="cta-button" style="margin-left:12px;background:white;color:var(--primary)" routerLink="/ai/plan-trip">
                <mat-icon>edit</mat-icon> Plan a Trip
              </button>
            </div>
            <div class="ai-cta-visual">
              <span style="font-size:120px">🤖</span>
            </div>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .ai-cta-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 24px !important; margin-top: 48px; }
    .ai-cta-content { display: flex; align-items: center; gap: 32px; padding: 24px; }
    .ai-cta-text { flex: 1; }
    .ai-cta-text h2 { margin: 0 0 12px; font-size: 1.8rem; }
    .ai-cta-text p { margin: 0 0 24px; line-height: 1.6; opacity: 0.9; }
    .ai-cta-visual { flex-shrink: 0; }
    @media (max-width: 768px) {
      .ai-cta-content { flex-direction: column; text-align: center; }
      .ai-cta-visual { display: none; }
    }
  `]
})
export class HomeComponent {
  services = [
    { icon: 'flight', title: 'Flights', subtitle: 'Search & Book', description: 'Find the best flight deals across 500+ airlines worldwide.', link: '/flights' },
    { icon: 'hotel', title: 'Hotels', subtitle: 'Stay Anywhere', description: 'Book hotels, resorts, and homestays at the best prices.', link: '/hotels' },
    { icon: 'directions_car', title: 'Car Rental', subtitle: 'Drive Free', description: 'Ride in style with our premium car rental services.', link: '/cars' },
    { icon: 'train', title: 'Transport', subtitle: 'Bus & Train', description: 'Book bus and train tickets for intercity travel.', link: '/transport' },
    { icon: 'umbrella', title: 'Insurance', subtitle: 'Travel Safe', description: 'Protect your trip with comprehensive travel insurance.', link: '/insurance' },
    { icon: 'card_giftcard', title: 'Packages', subtitle: 'Holiday Deals', description: 'All-in-one holiday packages at unbeatable prices.', link: '/packages' },
  ];

  stats = [
    { value: '500+', suffix: '', label: 'Airlines Partnered' },
    { value: '50K', suffix: '+', label: 'Happy Travelers' },
    { value: '100', suffix: '+', label: 'Countries Covered' },
    { value: '24/7', suffix: '', label: 'Customer Support' },
  ];

  private router = inject(Router);

  searchGlobal(query: string) {
    if (query.trim()) {
      this.router.navigate(['/search'], { queryParams: { q: query } });
    }
  }
}
