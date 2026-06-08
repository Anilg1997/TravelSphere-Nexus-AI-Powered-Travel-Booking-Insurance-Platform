import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { NgIf, CurrencyPipe, DecimalPipe } from '@angular/common';
import { PaymentService } from '../../../services/payment.service';
import { WalletResponse } from '../../../models/payment.model';

@Component({
  selector: 'app-wallet',
  standalone: true,
  imports: [ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatProgressSpinnerModule, MatSnackBarModule, NgIf, CurrencyPipe, DecimalPipe],
  template: `
    <div class="page-container">
      <div *ngIf="loading" style="text-align:center;padding:40px"><mat-spinner diameter="40" style="margin:0 auto"></mat-spinner></div>

      <div *ngIf="wallet && !loading" style="max-width:600px;margin:0 auto">
        <mat-card style="text-align:center;padding:40px;background:linear-gradient(135deg,#667eea,#764ba2);color:white">
          <mat-icon style="font-size:64px;width:64px;height:64px">account_balance_wallet</mat-icon>
          <h2 style="margin:16px 0">Wallet Balance</h2>
          <div style="font-size:3rem;font-weight:800">₹{{ wallet.balance | number }}</div>
          <p>{{ wallet.currency }} · {{ wallet.isActive ? 'Active' : 'Inactive' }}</p>
        </mat-card>

        <mat-card style="margin-top:24px;padding:24px">
          <h3>Top Up Wallet</h3>
          <form [formGroup]="topUpForm" (ngSubmit)="onTopUp()" style="display:flex;gap:16px;align-items:flex-start">
            <mat-form-field appearance="outline" style="flex:1">
              <mat-label>Amount (₹)</mat-label>
              <input matInput type="number" formControlName="amount" min="1" />
            </mat-form-field>
            <button mat-raised-button color="primary" type="submit" [disabled]="topUpForm.invalid || toppingUp" class="cta-button">
              {{ toppingUp ? 'Adding...' : 'Add Funds' }}
            </button>
          </form>
        </mat-card>
      </div>
    </div>
  `
})
export class WalletComponent {
  private paymentService = inject(PaymentService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  loading = true;
  toppingUp = false;
  wallet: WalletResponse | null = null;

  topUpForm = this.fb.nonNullable.group({ amount: [500, [Validators.required, Validators.min(1)]] });

  constructor() { this.loadWallet(); }

  loadWallet() { this.paymentService.getWalletBalance().subscribe(w => { this.wallet = w; this.loading = false; }); }

  onTopUp() {
    if (this.topUpForm.invalid) return;
    this.toppingUp = true;
    this.paymentService.topUpWallet(this.topUpForm.getRawValue()).subscribe(w => {
      this.wallet = w;
      this.toppingUp = false;
      this.snackBar.open('Wallet topped up!', 'Close', { duration: 3000 });
      this.topUpForm.patchValue({ amount: 500 });
    });
  }
}
