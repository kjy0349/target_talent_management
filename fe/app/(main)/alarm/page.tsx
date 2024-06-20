"use client";

import { useState, useEffect, ChangeEvent, KeyboardEvent } from "react";
import { HiSearch, HiChevronLeft, HiChevronRight } from "react-icons/hi";
import {
  TextInput,
  Button,
  Table,
  Pagination,
  Select,
  Badge,
} from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { useAuthStore } from "@/stores/auth";
import * as Stomp from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { NotificationFull } from "@/types/notification/Notification";
import { AxiosError } from "axios";
import { useNotificationStore } from "@/stores/notification";
import { NetworkingCreateModal } from "@/components/networking/NetworkingCreateModal";
import NetworkingNotificationModal from "@/components/networking/NetworkingNotificationModal";

export default function NotificationManagementPage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [maxCount, setMaxCount] = useState(0);
  const [maxPage, setMaxPage] = useState(0);
  const notificationStore = useNotificationStore();
  const [size, setSize] = useState(10);
  const [keyword, setKeyword] = useState("");
  const localAxios = useLocalAxios();
  const auth = useAuthStore();

  useEffect(() => {
    if (notificationStore.isFetched) {
      return;
    }
    baseSetting();
  }, [auth]);

  const baseSetting = async () => {
    const notificationResponse =
      await localAxios.get<NotificationFull[]>(`/notification`);
    // console.log(notificationResponse.data);
    notificationStore.afterFetched();
    notificationStore.setNotificationList(notificationResponse.data);
    setMaxCount(notificationResponse.data.length);
  };

  useEffect(() => {
    setMaxPage(Math.max(1, Math.ceil(maxCount / size)));
    // console.log(
    //   maxCount,
    //   size,
    //   Math.max(
    //     1,
    //     Math.ceil(maxCount / size) || 1,
    //     Math.min(
    //       currentPage * size,
    //       notificationStore.getNotificationList().length,
    //     ),
    //   ),
    // );
  }, [maxCount, size]);

  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth() + 1}.${newDate.getDate()}`;
  };

  const handleCheckNotification = async (id: number) => {
    try {
      const readResponse = await localAxios.put(`/notification/${id}`);
      notificationStore.readNotification(id);
    } catch (e) {
      const err = e as AxiosError;
      alert("잠시 후 다시 시도해주세요.");
    }
  };
  const [showNetworkingNotificationModal, setShowNetworkingNotificationModal] =
    useState<boolean>(false);

  const closeModal = () => {
    setShowNetworkingNotificationModal(false);
  };

  const openModal = () => {
    setShowNetworkingNotificationModal(true);
  };

  const handleApproveNotification = async (
    notificationId: number,
    networkingId: number,
  ) => {
    if (confirm("승인하시겠습니까?")) {
      // console.log(notificationId, networkingId);
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
    <div className="w-full m-5 shadow-md">
      <div className="m-4 bg-white p-10">
        <div className="flex items-center justify-between">
          <h5 className="mb-10 text-3xl font-bold">알림 관리</h5>
          <div className="mr-4 flex items-center justify-end gap-2 space-x-4">
            <div className="flex items-center gap-2 font-bold">
              정렬 기준:{" "}
              <Select
                id="size"
                name="size"
                value={size}
                onChange={(e) => setSize(Number(e.target.value))}
              >
                <option value={10}>10개씩 보기</option>
                <option value={20}>20개씩 보기</option>
              </Select>
            </div>
            <Button onClick={openModal}>네트워킹 관련 알림 보기</Button>
          </div>
        </div>

        <div className="mx-4 h-[600px] overflow-x-auto overflow-y-scroll">
          <Table striped className="">
            <Table.Head>
              <Table.HeadCell className="w-24">타입</Table.HeadCell>
              <Table.HeadCell className="w-96 overflow-hidden">
                내용
              </Table.HeadCell>
              <Table.HeadCell className="w-24">받는 사람</Table.HeadCell>
              <Table.HeadCell className="w-36">발송일</Table.HeadCell>
              <Table.HeadCell className="w-24">확인</Table.HeadCell>
            </Table.Head>
            <Table.Body>
              {notificationStore
                .getNotificationList()
                .slice(
                  (currentPage - 1) * size,
                  Math.min(
                    currentPage * size,
                    notificationStore.getNotificationList().length,
                  ),
                )
                .map((notification) => (
                  <Table.Row key={notification.id}>
                    <Table.Cell>
                      {notification.notificationType == "SYSTEM"
                        ? "시스템"
                        : "개인"}
                    </Table.Cell>
                    <Table.Cell>{notification.content}</Table.Cell>
                    <Table.Cell>{notification.senderName || "관리자"}</Table.Cell>
                    <Table.Cell>
                      {parseLocalDateTime(notification.createdAt)}
                    </Table.Cell>
                    <Table.Cell>
                      <Button
                        onClick={() => handleCheckNotification(notification.id)}
                      >
                        확인
                      </Button>
                    </Table.Cell>
                  </Table.Row>
                ))}
            </Table.Body>
          </Table>
          {notificationStore.getNotificationList().length === 0 && (
            <div className="my-8 flex justify-center">
              <Badge className="w-fit">알림이 없습니다.</Badge>
            </div>
          )}
        </div>

        {maxPage > 0 && (
          <div className="mx-4 my-8 flex justify-center">
            <Pagination
              currentPage={currentPage}
              onPageChange={(page) => setCurrentPage(page)}
              showIcons
              totalPages={maxPage}
            />
          </div>
        )}
        {auth && auth.id && (
          <NetworkingNotificationModal
            memberId={auth.id}
            showModal={showNetworkingNotificationModal}
            closeModal={closeModal}
          />
        )}
      </div>

    </div>
  );
}
