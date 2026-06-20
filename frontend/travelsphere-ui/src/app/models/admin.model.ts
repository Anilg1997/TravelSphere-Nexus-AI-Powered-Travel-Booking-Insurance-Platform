export interface DashboardStats {
  totalUsers: number;
  totalBookings: number;
  totalRevenue: number;
  activeBookings: number;
  openFraudAlerts: number;
  openSupportTickets: number;
  todayBookings: number;
  revenueToday: number;
  totalFlights: number;
  totalHotels: number;
  totalPackages: number;
}

export interface AnalyticsData {
  revenue: {
    totalRevenue: number;
    monthlyRevenue: number;
    dailyAverage: number;
    projectedMonthly: number;
    revenueByDay: { label: string; value: number }[];
    revenueByService: { label: string; value: number }[];
  };
  users: {
    totalUsers: number;
    newUsersToday: number;
    newUsersThisMonth: number;
    activeUsers: number;
    growthRate: number;
    userGrowthByDay: { label: string; value: number }[];
  };
  bookings: {
    totalBookings: number;
    bookingsToday: number;
    bookingsThisMonth: number;
    conversionRate: number;
    cancelledBookings: number;
    cancellationRate: number;
    bookingsByDay: { label: string; value: number }[];
    bookingsByService: { label: string; value: number }[];
  };
  serviceDistribution: Record<string, number>;
}

export interface FraudAlert {
  id: string;
  userId: string;
  alertType: string;
  description: string;
  severity: string;
  status: string;
  referenceId: string;
  createdAt: string;
  resolvedAt?: string;
}

export interface UserManagementData {
  id: string;
  fullName: string;
  email: string;
  phone: string;
  role: string;
  emailVerified: boolean;
  accountLocked: boolean;
  accountEnabled: boolean;
  loyaltyTier: string;
  totalBookings: number;
  totalSpent: number;
  createdAt: string;
  lastLoginAt: string;
}

export interface BookingManagementData {
  id: string;
  bookingRef: string;
  userId: string;
  userName: string;
  userEmail: string;
  serviceType: string;
  serviceName: string;
  amount: number;
  status: string;
  paymentStatus: string;
  travelDate: string;
  createdAt: string;
}

export interface SupportTicket {
  id: string;
  userId: string;
  subject: string;
  description: string;
  category: string;
  priority: string;
  status: string;
  assignedTo: string;
  resolution: string;
  createdAt: string;
  updatedAt: string;
  resolvedAt: string;
}

export interface SystemHealth {
  overallStatus: string;
  uptime: number;
  services: Record<string, { name: string; status: string; responseTimeMs: number; instanceCount: number }>;
  database: { status: string; activeConnections: number; totalConnections: number; averageQueryTimeMs: number };
  cache: { status: string; hitRate: number; memoryUsage: number; keysCount: number };
  messaging: { status: string; messagesPerSecond: number; consumerLag: number; queueDepth: number };
  recentAlerts: { type: string; message: string; severity: string; timestamp: string }[];
}
