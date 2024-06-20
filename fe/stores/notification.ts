import { NotificationFull } from "@/types/notification/Notification";
import { create } from "zustand";

interface NotificationState {
  notificationList: NotificationFull[];
  isFetched: boolean;
  setNotificationList: (notifications: NotificationFull[]) => void;
  clearNotificationList: () => void;
  readNotification: (notificationId: number) => void;
  afterFetched: () => void;
  getNotificationList: () => NotificationFull[];
}

const useNotificationStore = create<NotificationState>((set, get) => ({
  notificationList: [], // 초기 상태 설정
  isFetched: false,

  afterFetched: () => {
    set({ isFetched: true });
  },

  setNotificationList: (notifications: NotificationFull[]) => {
    set({ notificationList: notifications });
  },

  clearNotificationList: () => {
    set({ notificationList: [] });
  },

  getNotificationList: () => {
    const { notificationList } = get();
    return notificationList;
  },

  readNotification: (notificationId: number) => {
    const { notificationList } = get();
    const updatedNotifications = notificationList.filter(
      (notification) => notification.id !== notificationId,
    );
    set({ notificationList: updatedNotifications });
  },
}));

export { useNotificationStore };
