import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NgFor, NgIf, AsyncPipe, TitleCasePipe } from '@angular/common';
import { SearchService, SearchResult } from '../../services/search.service';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatIconModule, MatButtonModule, MatChipsModule, MatProgressSpinnerModule, NgFor, NgIf, AsyncPipe, TitleCasePipe],
  template: `
    <div class="page-container">
      <h1 class="section-title">Search Results</h1>
      <p class="section-subtitle" *ngIf="query">Showing results for "{{ query }}"</p>

      <div *ngIf="loading" style="text-align:center;padding:60px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="!loading && results.length === 0" class="empty-state">
        <mat-icon>search_off</mat-icon>
        <h3>No results found</h3>
        <p>Try a different search term or browse our services.</p>
      </div>

      <div class="responsive-grid" *ngIf="!loading">
        <mat-card *ngFor="let r of results" class="feature-card" [routerLink]="getRoute(r)">
          <mat-card-header>
            <mat-icon class="card-icon">{{ getIcon(r.entityType) }}</mat-icon>
            <mat-card-title>{{ r.title }}</mat-card-title>
            <mat-card-subtitle>{{ r.entityType | titlecase }} · {{ r.city }}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <p>{{ r.description }}</p>
            <div *ngIf="r.price"><strong>₹{{ r.price }}</strong></div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `
})
export class SearchComponent {
  private route = inject(ActivatedRoute);
  private searchService = inject(SearchService);

  query = '';
  results: SearchResult[] = [];
  loading = false;

  constructor() {
    this.route.queryParams.subscribe(p => {
      this.query = p['q'] || p['query'] || '';
      if (this.query) this.search();
    });
  }

  search() {
    this.loading = true;
    this.searchService.search(this.query).subscribe(r => {
      this.results = r;
      this.loading = false;
    });
  }

  getIcon(type: string): string {
    const icons: Record<string, string> = { flight: 'flight', hotel: 'hotel', car: 'directions_car', package: 'card_giftcard', insurance: 'umbrella', transport: 'train' };
    return icons[type?.toLowerCase()] || 'search';
  }

  getRoute(r: SearchResult): string {
    const routes: Record<string, string> = { flight: '/flights/', hotel: '/hotels/', car: '/cars/', package: '/packages/', insurance: '/insurance/', transport: '/transport/' };
    return (routes[r.entityType?.toLowerCase()] || '/') + r.entityId;
  }
}
