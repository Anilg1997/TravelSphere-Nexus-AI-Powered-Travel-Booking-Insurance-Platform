import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf } from '@angular/common';
import { DocumentService } from '../../services/document.service';

@Component({
  selector: 'app-documents',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, MatSnackBarModule, NgIf],
  template: `
    <div class="page-container" style="max-width:600px">
      <h1 class="section-title">Documents</h1>
      <p class="section-subtitle">Generate and view travel documents</p>

      <div class="form-section">
        <form [formGroup]="docForm" (ngSubmit)="onSubmit()">
          <mat-form-field class="full-width" appearance="outline">
            <mat-label>Document Type</mat-label>
            <mat-select formControlName="type">
              <mat-option value="INVOICE">Invoice</mat-option>
              <mat-option value="CONFIRMATION">Booking Confirmation</mat-option>
              <mat-option value="VOUCHER">Travel Voucher</mat-option>
              <mat-option value="INSURANCE">Insurance Certificate</mat-option>
            </mat-select>
          </mat-form-field>
          <mat-form-field class="full-width" appearance="outline"><mat-label>Booking Reference</mat-label><input matInput formControlName="bookingRef" placeholder="e.g., TS-FLT-123456" /></mat-form-field>
          <button mat-raised-button color="primary" class="cta-button full-width" type="submit" [disabled]="docForm.invalid || loading">
            <mat-icon>description</mat-icon> {{ loading ? 'Generating...' : 'Generate Document' }}
          </button>
        </form>
      </div>
    </div>
  `
})
export class DocumentsComponent {
  private fb = inject(FormBuilder);
  private documentService = inject(DocumentService);
  private snackBar = inject(MatSnackBar);
  loading = false;

  docForm = this.fb.nonNullable.group({
    type: ['CONFIRMATION', Validators.required],
    bookingRef: ['', Validators.required],
  });

  onSubmit() {
    if (this.docForm.invalid) return;
    this.loading = true;
    this.documentService.generate(this.docForm.getRawValue() as any).subscribe({
      next: () => { this.snackBar.open('Document generated!', 'Close', { duration: 3000 }); this.loading = false; },
      error: () => { this.loading = false; this.snackBar.open('Generation failed.', 'Close', { duration: 3000 }); },
    });
  }
}
