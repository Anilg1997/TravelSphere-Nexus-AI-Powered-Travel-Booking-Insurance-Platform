import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Document, DocumentRequest } from '../models/document.model';

@Injectable({ providedIn: 'root' })
export class DocumentService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/documents`;

  generate(request: DocumentRequest): Observable<Document> {
    return this.http.post<ApiResponse<Document>>(`${this.baseUrl}/generate`, request)
      .pipe(map(r => r.data!));
  }

  getDocument(id: string): Observable<Document> {
    return this.http.get<ApiResponse<Document>>(`${this.baseUrl}/${id}`).pipe(map(r => r.data!));
  }
}
