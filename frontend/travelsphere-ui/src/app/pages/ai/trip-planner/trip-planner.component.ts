import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterLink } from '@angular/router';
import { NgIf } from '@angular/common';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-trip-planner',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatProgressSpinnerModule, NgIf],
  template: `
    <div class="page-container" style="max-width:700px">
      <a routerLink="/ai/chat" mat-button><mat-icon>arrow_back</mat-icon> Back to AI Chat</a>

      <h1 class="section-title" style="margin-top:16px">Plan Your Trip</h1>
      <p class="section-subtitle">Let AI create a personalized itinerary for you</p>

      <div class="form-section">
        <form [formGroup]="planForm" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline"><mat-label>Destination</mat-label><input matInput formControlName="destination" placeholder="e.g., Goa, Manali, Switzerland" /></mat-form-field>
          <div class="form-row">
            <mat-form-field appearance="outline"><mat-label>Duration (days)</mat-label><input matInput type="number" formControlName="durationDays" min="1" max="30" /></mat-form-field>
            <mat-form-field appearance="outline"><mat-label>Travelers</mat-label><input matInput type="number" formControlName="travelers" min="1" /></mat-form-field>
          </div>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Budget (₹)</mat-label><input matInput type="number" formControlName="budget" min="1" /></mat-form-field>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Preferences (optional)</mat-label><textarea matInput formControlName="preferences" rows="3" placeholder="e.g., adventure, beach, culture, food, family-friendly"></textarea></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="planForm.invalid || loading">
            <mat-icon>auto_awesome</mat-icon> {{ loading ? 'Planning...' : 'Generate Itinerary' }}
          </button>
        </form>
      </div>

      <mat-card *ngIf="itinerary" style="margin-top:24px;padding:24px">
        <h2 style="margin:0 0 16px;color:var(--primary)">Your {{ planForm.value.durationDays }}-Day Trip to {{ planForm.value.destination }} 🎉</h2>
        <div style="white-space:pre-wrap;line-height:1.6">{{ itinerary }}</div>
      </mat-card>
    </div>
  `
})
export class TripPlannerComponent {
  private fb = inject(FormBuilder);

  loading = false;
  itinerary = '';

  planForm = this.fb.nonNullable.group({
    destination: ['', Validators.required],
    durationDays: [5, [Validators.required, Validators.min(1)]],
    travelers: [2, [Validators.required, Validators.min(1)]],
    budget: [50000, [Validators.required, Validators.min(1)]],
    preferences: [''],
  });

  onSubmit() {
    if (this.planForm.invalid) return;
    this.loading = true;

    const { destination, durationDays, travelers, budget, preferences } = this.planForm.value;

    // Simulate AI itinerary generation (would call backend API)
    setTimeout(() => {
      this.itinerary = `🌟 **${destination} Travel Itinerary**\n\n${'━'.repeat(40)}\n\n` +
        `**Trip Details:**\n• Duration: ${durationDays} Days\n• Travelers: ${travelers}\n• Budget: ₹${budget?.toLocaleString()}\n• Preferences: ${preferences || 'General'}\n\n` +
        `**Day 1 - Arrival & Exploration**\n• Check into your accommodation\n• Explore local markets and cuisine\n• Evening walking tour\n\n` +
        `**Day 2 - Sightseeing**\n• Visit major attractions\n• Lunch at a recommended restaurant\n• Cultural experience activity\n\n` +
        `**Day 3 - Adventure & Relaxation**\n• Morning adventure activity\n• Afternoon at leisure\n• Sunset views\n\n` +
        `**Budget Breakdown:**\n• Accommodation: ₹${((budget || 50000) * 0.4).toLocaleString()}\n• Food: ₹${((budget || 50000) * 0.2).toLocaleString()}\n• Activities: ₹${((budget || 50000) * 0.25).toLocaleString()}\n• Transport: ₹${((budget || 50000) * 0.15).toLocaleString()}\n\n` +
        `💡 **Pro Tips:**\n• Book accommodations in advance\n• Carry local currency\n• Get travel insurance for peace of mind\n• Try local street food\n• Keep emergency numbers handy\n\n` +
        `Want me to customize this further or help you book flights and hotels?`;
      this.loading = false;
    }, 1500);
  }
}
