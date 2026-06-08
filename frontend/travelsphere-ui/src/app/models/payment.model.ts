export interface PaymentInitRequest {
  bookingRef: string;
  serviceType: string;
  amount: number;
  currency?: string;
  paymentMethod: string;
  promoCode?: string;
}

export interface PaymentResponse {
  paymentRef: string;
  bookingRef: string;
  serviceType: string;
  amount: number;
  currency: string;
  paymentMethod: string;
  status: string;
  transactionId: string;
  discountAmount: number;
  finalAmount: number;
  createdAt: string;
}

export interface WalletResponse {
  userId: string;
  balance: number;
  currency: string;
  isActive: boolean;
  lastUpdated: string;
}

export interface TopUpRequest {
  amount: number;
  currency?: string;
}

export interface RefundRequest {
  paymentRef: string;
  reason: string;
}

export interface RefundResponse {
  refundRef: string;
  paymentRef: string;
  amount: number;
  reason: string;
  status: string;
  createdAt: string;
}
