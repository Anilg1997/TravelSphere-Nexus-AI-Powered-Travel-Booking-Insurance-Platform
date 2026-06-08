export interface DashboardStats {
  totalUsers: number;
  totalBookings: number;
  totalRevenue: number;
  activeTrips: number;
  revenueByService: { service: string; revenue: number }[];
  bookingsByDay: { date: string; count: number }[];
  recentBookings: any[];
}

export interface FraudAlert {
  id: string;
  userId: string;
  bookingRef: string;
  reason: string;
  riskScore: number;
  status: 'OPEN' | 'INVESTIGATING' | 'RESOLVED';
  createdAt: string;
}
