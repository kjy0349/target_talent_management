"use client";
import Link from "next/link";
import Image from "next/image";
import { HiBell, HiEye } from "react-icons/hi";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";

import * as Stomp from "@stomp/stompjs";
import { useAuthStore } from "@/stores/auth";

import { useLocalAxios } from "@/app/api/axios";
import { useRouter } from "next/navigation";
import { NotificationFull } from "@/types/notification/Notification";
import { Badge, Button, Card, Popover, Dropdown } from "flowbite-react";
import { useNotificationStore } from "@/stores/notification";
import NetworkingNotificationModal from "../networking/NetworkingNotificationModal";

export default function NotificationBellDropdown() {
  const router = useRouter();
  const [notificationCount, setNotificationCount] = useState<number>(0);
  const auth = useAuthStore();
  const notificationStore = useNotificationStore();
  const stompClient = useRef<Stomp.Client | null>(null);
  const [connected, setConnected] = useState(false);
  // const notificationList = useRef<NotificationResponse[]>([]);

  const localAxios = useLocalAxios();
  //   const baseUrl: string = import.meta.env.VITE_API_BASE_URL;
  //   const send = () => {
  //     const dest = `/send/notification`;

  //     stompClient.current?.publish({
  //       destination: dest,
  //       body: JSON.stringify({
  //         content: "test",
  //         notificationType:"SYSTEM",
  //         notificationDataType:"NONE",
  //         isRead: false,
  //         notificationType: "confirm",
  //         productId: 4,
  //       }),
  //     });
  //   };

  useEffect(() => {
    if (auth.id) {
      console.log("auth-id", auth);
      baseSetting();
    }
  }, [auth]);

  const baseSetting = async () => {
    try {
      const notificationResponse =
        await localAxios.get<NotificationFull[]>("/notification");
      // console.log("prev,", notificationResponse.data);
      notificationStore.setNotificationList(notificationResponse.data);
      stompSetting();
    } catch (error) {
      console.error(error);
    }
  };

  const [notifications, setNotifications] = useState<NotificationFull[]>([]);

  useEffect(() => {
    const nextValue = notificationStore.getNotificationList();
    setNotifications(nextValue);
  }, [notificationStore.getNotificationList().length]);

  const returnGetBody = () => {
    return notifications.length ? (
      notifications.slice(0, 5).map((n, index) => (
        <div
          key={n.id}
          className="flex border-y px-4 py-3 hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-600"
        >
          <Link href="/alarm">
            <div className="w-full pl-3">
              <div className="mb-1.5 text-sm font-normal text-gray-500 dark:text-gray-400">
                {n.content}
              </div>
              <div className="text-primary-700 dark:text-primary-400 text-xs font-medium">
                {n.senderName ? n.senderName : "관리자"}
              </div>
              <div className="text-primary-700 dark:text-primary-400 text-xs font-medium">
                {parseLocalDateTime(n.createdAt)}
              </div>
            </div>
          </Link>
        </div>
      ))
    ) : (
      <div className="flex justify-center text-center">
        <Badge className="w-fit">알림이 없습니다.</Badge>
      </div>
    );
  };

  const stompSetting = () => {
    const memberId = auth.id;
    const socket = new SockJS(`http://k10s102.p.ssafy.io:8080/ws`);
    stompClient.current = new Stomp.Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => {
        console.log(new Date(), str);
      },
      onConnect: (frame) => {
        // console.log("connected");
        const dest = `/all-notification/${memberId}`;
        // console.log("Subscribing to:", dest);
        stompClient.current?.subscribe(dest, async (response) => {
          // console.log("Message received:", response);
          const res = await JSON.parse(response.body);
          const cameNotification = res as NotificationFull;
          if (
            !notificationStore
              .getNotificationList()
              .map((n) => n.id)
              .includes(cameNotification.id)
          ) {
            const nextNotification = [
              res,
              ...notificationStore.getNotificationList(),
            ];
            notificationStore.setNotificationList(nextNotification);
          }
        });
      },
      onDisconnect: (frame) => {
        console.log("disconnected");
      },
      onWebSocketError: (error) => {
        console.error("WebSocket error:", error);
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame);
      },
    });
    stompClient.current?.activate();
  };

  const [isPopoverOpen, setIsPopOverOpen] = useState<boolean>();
  const togglePopover = () => {
    setIsPopOverOpen(!isPopoverOpen);
  };
  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth() + 1}.${newDate.getDate()}`;
  };

  return (
    <Dropdown
      className="max-h-[300px] overflow-x-scroll rounded"
      arrowIcon={false}
      inline
      label={
        <span className="relative mx-2 rounded-lg p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-900 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white">
          <span className="sr-only">Notifications</span>
          <HiBell className="h-6 w-6" />
          {notificationStore.getNotificationList().length > 0 && (
            <span className="absolute -right-1 -top-1 inline-flex items-center justify-center rounded-full bg-red-600 px-2 py-1 text-xs font-bold leading-none text-white">
              {notificationStore.notificationList.length}
            </span>
          )}
        </span>
      }
      theme={{ content: "py-0" }}
    >
      <div className="min-w-[400px] max-w-[600px]">
        <div className="block rounded-t-xl bg-gray-50 px-4 py-2 text-center text-base font-medium text-gray-700 dark:bg-gray-700 dark:text-gray-400">
          알림 현황
        </div>
        <div
          className="flex w-full justify-center border-y px-4 py-3 hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-600"
          onClick={() => router.push("/alarm")}
        >
          <HiEye className="h-5 w-5 " />
          <span>View All</span>
        </div>
        <div>{returnGetBody()}</div>
      </div>
    </Dropdown>
  );
}
