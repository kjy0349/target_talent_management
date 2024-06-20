"use client";

import { useLocalAxios } from "@/app/api/axios";
import { NotificationFull } from "@/types/notification/Notification";
import { Button, Modal, ModalBody, ModalHeader } from "flowbite-react";
import { useEffect, useState } from "react";

interface NetworkingNotificationModalProps {
  memberId: number;
  closeModal: () => void;
  showModal: boolean;
}

const NetworkingNotificationModal = ({
  showModal,
  closeModal,
}: NetworkingNotificationModalProps) => {
  const [notifications, setNotifications] = useState<NotificationFull[]>([]);
  const localAxios = useLocalAxios();
  useEffect(() => {
    baseSetting();
  }, []);
  const baseSetting = async () => {
    const notificationResponse = await localAxios.get<NotificationFull[]>(
      `/notification/networking`,
    );
    setNotifications(notificationResponse.data);
  };
  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth() + 1}.${newDate.getDate()}`;
  };

  const handleApproveNotification = async (
    notificationId: number,
    networkingId: number,
  ) => {
    if (confirm("승인하시겠습니까?")) {
      const approveResponse = await localAxios.post(
        `/networking/${networkingId}/approve?notificationId=${notificationId}`,
      );
      if (approveResponse.status === 200) {
        alert("승인 되었습니다.");
        baseSetting();
      }
    }
  };

  return (
    <div>
      <Modal show={showModal} onClose={closeModal} size={"4xl"}>
        <Modal.Header> 네트워킹 관련 알람 조회 </Modal.Header>
        <Modal.Body>
          {notifications.length ? (
            notifications.map((n) => (
              <div
                key={n.id}
                className="flex border-y px-4 py-3 hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-600"
              >
                <div className="flex w-full items-center justify-between pl-3">
                  <div className="mb-1.5 w-3/6 text-sm font-normal text-gray-500 dark:text-gray-400">
                    {n.content}
                  </div>
                  <div className=" text-primary-700 dark:text-primary-400 w-1/6 text-xs font-medium">
                    {n.senderName ? n.senderName : "관리자"}
                  </div>
                  <div className=" text-primary-700 dark:text-primar y-400 w-1/6 text-xs font-medium">
                    {parseLocalDateTime(n.createdAt)}
                  </div>

                  <Button
                    className="w-1/6"
                    onClick={() =>
                      handleApproveNotification(
                        n.id,
                        n.notificationData[0].dataId,
                      )
                    }
                  >
                    승인
                  </Button>
                </div>
              </div>
            ))
          ) : (
            <div>네트워킹 관련 알람이 없습니다.</div>
          )}
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default NetworkingNotificationModal;
