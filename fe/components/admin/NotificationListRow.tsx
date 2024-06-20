"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  SystemNotificationFull,
  SystemNotificationUpdate,
} from "@/types/notification/Notification";
import { AxiosError } from "axios";
import { Badge, Button } from "flowbite-react";
import { useState } from "react";
interface SystemNotificationListRowProps {
  systemNotification: SystemNotificationFull;
  key: number;
  handleOnChange: () => void;
}
const SystemNotificationListRow = ({
  systemNotification,
  handleOnChange,
}: SystemNotificationListRowProps) => {
  const localAxios = useLocalAxios();
  const [calculateWeek, setCalculateWeek] = useState<number>(
    systemNotification.calculateWeek,
  );
  const [period, setPeriod] = useState<number>(systemNotification.period);
  const [isActive, setIsActive] = useState<boolean>(
    systemNotification.isActive,
  );
  const deleteSystemNotification = async (id: number) => {
    if (confirm("삭제하시겠습니까?")) {
      try {
        const deleteResponse = await localAxios.delete(
          `/notification/system/${systemNotification.id}`,
        );

        if (deleteResponse.status == 200) {
          alert(" 삭제에 성공하였습니다 ");
          handleOnChange();
        }
      } catch (e) {
        const err = e as AxiosError;
        if (err.status == 404) {
          alert("이미 삭제된 데이터 입니다.");
        } else {
          alert("잠시 후 다시 시도해주세요.");
        }
      }
    }
  };

  const handleOnChangePeriod = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPeriod(Number(e.target.value));
    const nextUpdate = {
      ...systemNotification,
      period: Number(e.target.value),
    };
    updateNotification(nextUpdate);
  };
  const handleOnChangeCalculateWeek = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setCalculateWeek(Number(e.target.value));
    const nextUpdate = {
      ...systemNotification,
      calculateWeek: Number(e.target.value),
    };
    updateNotification(nextUpdate);
  };

  const handleOnChangeIsActive = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsActive(e.target.checked);
    const nextUpdate = {
      ...systemNotification,
      isActive: e.target.checked,
    };
    updateNotification(nextUpdate);
  };

  const updateNotification = async (
    nextRowUpdate: SystemNotificationUpdate,
  ) => {
    try {
      const updateResponse = await localAxios.put(
        `/notification/system/${systemNotification.id}`,
        nextRowUpdate,
      );

      if (updateResponse.status == 200) {
        handleOnChange();
      }
    } catch (e) {
      const err = e as AxiosError;
      if (err.status == 404) {
        alert("이미 삭제된 데이터 입니다.");
      } else {
        console.error(err);
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <tr className="border-b bg-white dark:border-gray-700 dark:bg-gray-800">
      <td
        scope="row"
        className="whitespace-nowrap px-4 py-4 font-medium text-gray-900 dark:text-white"
      >
        {systemNotification.title}
      </td>
      <td className="w-1/2 overflow-x-hidden px-4 py-4">
        {systemNotification.content}
      </td>
      <td className="px-4 py-4">전사</td>
      <td className="px-4 py-4">
        <input
          name="calculateWeek"
          type="number"
          className="w-16"
          value={calculateWeek}
          onChange={handleOnChangeCalculateWeek}
        />
      </td>
      <td className="px-4 py-4">
        <input
          type="number"
          name="period"
          className="w-16"
          onChange={handleOnChangePeriod}
          value={period}
        />
      </td>
      <td className="px-4 py-4">
        <label className="inline-flex cursor-pointer items-center">
          <input
            name="isActive"
            type="checkbox"
            checked={isActive}
            onChange={handleOnChangeIsActive}
            className="peer sr-only"
          />
          <div className="peer relative h-5 w-9 rounded-full bg-gray-200 after:absolute after:start-[2px] after:top-[2px] after:h-4 after:w-4 after:rounded-full after:border after:border-gray-300 after:bg-white after:transition-all after:content-[''] peer-checked:bg-blue-600 peer-checked:after:translate-x-full peer-checked:after:border-white peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:border-gray-600 dark:bg-gray-700 dark:peer-focus:ring-blue-800 rtl:peer-checked:after:-translate-x-full"></div>
          <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300"></span>
        </label>
      </td>
      <td className="relative px-2 py-4">
        {systemNotification.idx > 4 ? (
          <Button
            onClick={() => deleteSystemNotification(systemNotification.id)}
          >
            삭제
          </Button>
        ) : (
          <Button disabled={true}>불가</Button>
        )}
      </td>
    </tr>
  );
};

export default SystemNotificationListRow;
