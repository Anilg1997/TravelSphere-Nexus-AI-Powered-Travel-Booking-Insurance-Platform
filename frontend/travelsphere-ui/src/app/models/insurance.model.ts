export interface PolicyType {
  id: string;
  name: string;
  description: string;
  basePremium: number;
  coverageAmount: number;
  durationDays: number;
}

export interface PremiumCalculateRequest {
  policyTypeId: string;
  destination: string;
  durationDays: number;
  travelerAge: number;
}

export interface PremiumResult {
  calculatedPremium: number;
  destinationMultiplier: number;
  ageMultiplier: number;
  durationMultiplier: number;
}

export interface PurchaseRequest {
  policyTypeId: string;
  destination: string;
  durationDays: number;
  travelerAge: number;
  startDate: string;
}

export interface InsuranceClaim {
  id: string;
  policyId: string;
  description: string;
  amount: number;
  status: string;
  filedAt: string;
}
