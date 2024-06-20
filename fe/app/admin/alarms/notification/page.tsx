"use client";

import { useLocalAxios } from "@/app/api/axios";
import SystemNotificationListRow from "@/components/admin/NotificationListRow";
import NotificationListRow from "@/components/admin/NotificationListRow";
import {
  SystemNotificationCreate,
  SystemNotificationFull,
} from "@/types/notification/Notification";
import { Button, Modal, Pagination, theme, Dropdown } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiCheck } from "react-icons/hi";
import { twMerge } from "tailwind-merge";

export default function AdminNotificationPage() {
  const [isOpen, setIsOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [showPersonalModal, setShowPersonalModal] = useState(false);
  const [isAddingRow, setIsAddingRow] = useState(false);
  const [notificationCreate, setNotificationCreate] =
    useState<SystemNotificationCreate>({
      title: "",
      content: "",
      calculateWeek: 1,
      period: 1,
    });
  const [notificationList, setNotificationList] =
    useState<SystemNotificationFull[]>();
  const localAxios = useLocalAxios();
  useEffect(() => {
    baseSetting();
  }, []);

  const baseSetting = async () => {
    const response =
      await localAxios.get<SystemNotificationFull[]>(`/notification/system`);
    setNotificationList(response.data);
  };

  const openPersonalModal = () => {
    setShowPersonalModal(true);
  };

  const closePersonalModal = () => {
    setShowPersonalModal(false);
  };

  const [showAddNotificationModal, setShowAddNotificationModal] =
    useState(false);
  const openAddNotificationModal = () => {
    setShowAddNotificationModal(true);
  };

  const closeAddNotificationModal = () => {
    setShowAddNotificationModal(false);
  };

  const handleAddNotification = async () => {
    try {
      await localAxios.post("/notification/system", notificationCreate);
      closeAddNotificationModal();
      baseSetting();
      setNotificationCreate({
        title: "",
        content: "",
        calculateWeek: 1,
        period: 1,
      });
    } catch (error) {
      alert("권한이 없습니다");
      console.error("Error adding notification:", error);
    }
  };

  return (
    <div className="flex flex-col space-y-5 p-5">
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <h3 className="w-48 text-xl">알림 관리 현황</h3>
          <div
            id="toast-warning"
            className="flex w-full max-w-2xl items-center rounded-lg bg-white p-4 text-gray-500 shadow dark:bg-gray-800 dark:text-gray-400"
            role="alert"
          >
            <div className="inline-flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-lg bg-orange-100 text-orange-500 dark:bg-orange-700 dark:text-orange-200">
              <svg
                className="h-5 w-5"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z" />
              </svg>
              <span className="sr-only">Warning icon</span>
            </div>
            <div className="ms-3 text-sm font-normal">
              전사적인 알림은 텍스트 형식의 전송은 가능하나 연산이 들어가는
              알림은 추가가 불가능 합니다.
            </div>
          </div>
        </div>

        <div className="z-1 mt-5 flex justify-end gap-2 text-center">
          <Button
            className=" rounded-lg px-3 py-2 text-center text-2xl font-medium text-white focus:outline-none focus:ring-4"
            type="button"
            onClick={openAddNotificationModal}
          >
            {isAddingRow ? "취소" : "전사 알림 추가"}
          </Button>
          <Button
            className=" rounded-lg px-3 py-2 text-center text-2xl font-medium text-white focus:outline-none focus:ring-4"
            type="button"
            onClick={openPersonalModal}
          >
            개인 알림 보기
          </Button>
        </div>
      </div>

      <div className="flex space-x-5">
        <div className="relative w-full overflow-x-auto">
          <div className="flex justify-between">{/* ... */}</div>
          <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
            <thead className="bg-gray-100 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-4 py-3">
                  알림 제목
                </th>
                <th scope="col" className="px-4 py-3">
                  내용
                </th>
                <th scope="col" className="px-4 py-3">
                  대상
                </th>
                <th scope="col" className="px-4 py-3">
                  기준 주 단위
                </th>
                <th scope="col" className="px-4 py-3">
                  송신 주기
                </th>
                <th scope="col" className="px-4 py-3">
                  활성화 여부
                </th>
                <th scope="col" className="px-4 py-3">
                  변경
                </th>
              </tr>
            </thead>
            <tbody>
              {notificationList?.map((n) => (
                <SystemNotificationListRow
                  key={n.id}
                  systemNotification={n}
                  handleOnChange={() => baseSetting()}
                />
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Personal Notification Modal */}
      <Modal show={showPersonalModal} onClose={closePersonalModal} size="6xl">
        <Modal.Header>개인 알림 관리</Modal.Header>
        <Modal.Body>
          <div className="max-h-[70vh] overflow-y-auto p-4">
            <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead className="bg-gray-100 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" className="px-6 py-3">
                    <HiCheck />
                  </th>
                  <th scope="col" className="px-6 py-3">
                    알림 제목
                  </th>
                  <th scope="col" className="px-6 py-3">
                    내용
                  </th>
                  <th scope="col" className="px-6 py-3">
                    받는 사람
                  </th>
                  <th scope="col" className="px-6 py-3">
                    발송일
                  </th>
                  <th scope="col" className="px-6 py-3">
                    변경
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr className="border-b bg-white dark:border-gray-700 dark:bg-gray-800">
                  <td className="px-6 py-4">
                    <input type="checkbox" />
                  </td>
                  <th
                    scope="row"
                    className="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                  >
                    개인 알림 1
                  </th>
                  <td className="px-6 py-4">개인 알림 내용 1</td>
                  <td className="px-6 py-4">김철수</td>
                  <td className="px-6 py-4">2023-06-02</td>
                  <td className="px-6 py-4">
                    <Button
                      className="mb-2 rounded-lg px-5 py-2.5 text-center text-xs font-medium text-white focus:outline-none focus:ring-4"
                      type="button"
                    >
                      변경
                    </Button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">{/* ... */}</div>
            <Pagination
              currentPage={currentPage}
              totalPages={20}
              onPageChange={(page) => setCurrentPage(page)}
              className="mx-auto"
            />
          </div>
        </Modal.Footer>
      </Modal>
      <Modal
        show={showAddNotificationModal}
        onClose={closeAddNotificationModal}
      >
        <Modal.Header>전사 알림 추가</Modal.Header>
        <Modal.Body>
          <div className="space-y-6">
            <div>
              <label
                htmlFor="title"
                className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
              >
                제목
              </label>
              <input
                type="text"
                id="title"
                value={notificationCreate.title}
                onChange={(e) =>
                  setNotificationCreate({
                    ...notificationCreate,
                    title: e.target.value,
                  })
                }
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label
                htmlFor="content"
                className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
              >
                내용
              </label>
              <textarea
                id="content"
                value={notificationCreate.content}
                onChange={(e) =>
                  setNotificationCreate({
                    ...notificationCreate,
                    content: e.target.value,
                  })
                }
                rows={4}
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                required
              ></textarea>
            </div>
            <div>
              <label
                htmlFor="calculateWeek"
                className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
              >
                기준 주 단위
              </label>
              <input
                type="number"
                id="calculateWeek"
                value={notificationCreate.calculateWeek}
                onChange={(e) =>
                  setNotificationCreate({
                    ...notificationCreate,
                    calculateWeek: Number(e.target.value),
                  })
                }
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <label
                htmlFor="period"
                className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
              >
                송신 주기
              </label>
              <input
                type="number"
                id="period"
                value={notificationCreate.period}
                onChange={(e) =>
                  setNotificationCreate({
                    ...notificationCreate,
                    period: Number(e.target.value),
                  })
                }
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                required
              />
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={handleAddNotification}>등록</Button>
          <Button color="gray" onClick={closeAddNotificationModal}>
            취소
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
