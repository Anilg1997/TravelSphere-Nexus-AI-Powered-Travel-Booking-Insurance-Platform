export interface Notification {
  id: string;
  userId: string;
  type: string;
  channel: string;
  title: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export interface SendNotificationRequest {
  userId: string;
  type: string;
  channel: string;
  title: string;
  message: string;
}
