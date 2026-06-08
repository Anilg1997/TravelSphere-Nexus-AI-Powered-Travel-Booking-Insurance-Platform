import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';

export interface SearchResult {
  id: string;
  entityType: string;
  entityId: string;
  title: string;
  description: string;
  city?: string;
  country?: string;
  category?: string;
  rating?: number;
  price?: number;
  score: number;
}

@Injectable({ providedIn: 'root' })
export class SearchService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/search`;

  search(query: string, type?: string, city?: string): Observable<SearchResult[]> {
    let params = new HttpParams().set('q', query);
    if (type) params = params.set('type', type);
    if (city) params = params.set('city', city);
    return this.http.get<ApiResponse<SearchResult[]>>(`${this.baseUrl}`, { params })
      .pipe(map(r => r.data || []));
  }
}
