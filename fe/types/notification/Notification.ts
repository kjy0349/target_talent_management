import { MemberAdminFull } from "../admin/Member";

export interface NotificationFull {
  id: number;
  content: string;
  notificationType: string;
  notificationDataType: string;
  notificationData: any[];
  isRead: boolean;
  member: MemberAdminFull;
  createdAt: Date;
  senderName: string;
}

export interface NotificationData {
  id: number;
  tableId: number;
}

export interface SystemNotificationCreate {
  title: string;
  content: string;
  calculateWeek: number;
  period: number;
}
export interface SystemNotificationUpdate {
  title: string;
  content: string;
  calculateWeek: number;
  period: number;
  isActive: boolean;
}

export interface SystemNotificationFull {
  title: string;
  id: number;
  content: string;
  idx: number;
  lastSendedAt: Date;
  calculateWeek: number;
  period: number;
  isActive: boolean;
}
