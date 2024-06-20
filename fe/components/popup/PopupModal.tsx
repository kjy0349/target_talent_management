'use client'

import { Button, Checkbox, Modal, Select, TextInput, Textarea } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { Popup as PopupRequest } from "rc-tooltip";
import { useEffect, useState } from "react";

interface PopupRequest {
  id: number,
  isUsed: boolean,
  content: string,
  startDate: Date,
  endDate: Date,
  authId: number,
}

export default function PopupModal({
  showModal,
  closeModal,
  isEdit,
  popupId,
  submit
}: {
  showModal: boolean;
  closeModal: () => void;
  isEdit: boolean,
  popupId?: number,
  submit: () => void,
}) {
  const localAxios = useLocalAxios();
  const [popup, setPopup] = useState<PopupRequest>({
    id: 0,
    isUsed: false,
    content: "",
    startDate: new Date(),
    endDate: new Date(),
    authId: 1,
  });

  const fetchData = async () => {
    await localAxios.get(`/admin/popup/${popupId}`)
      .then((Response) => {
        const fetchedPopup = Response.data;
        const authId = fetchedPopup.authLevel;
        const newPopup = {
          ...fetchedPopup,
          startDate: new Date(fetchedPopup.startDate),
          endDate: new Date(fetchedPopup.endDate),
          authId: authId,
        };

        delete newPopup.id;
        delete newPopup.authLevel;

        setPopup(newPopup);
      })
  }

  useEffect(() => {
    if (isEdit) {
      fetchData();
    }
  }, [isEdit])

  const submitModal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (isEdit) {
      await localAxios.put<PopupRequest>(`/admin/popup/${popupId}`, popup);
    } else {
      await localAxios.post<PopupRequest>(`/admin/popup`, popup);
    }
    closeModal();
    submit();
  };

  const formatDateForInput = (date?: Date) => {
    if (date && date instanceof Date) {
      return date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0") + "-" + String(date.getDate()).padStart(2, "0");
    }
  }

  const handleDateChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
    setPopup(prev => ({ ...prev, [key]: new Date(e.target.value) }));
  }

  const handleCheckboxChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
    setPopup(prev => ({ ...prev, [key]: e.target.checked }));
  }

  const handleTextChange = (key: string, e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setPopup(prev => ({ ...prev, [key]: e.target.value }));
  }

  const handleLevelChange = (key: string, e: React.ChangeEvent<HTMLSelectElement>) => {
    setPopup(prev => ({ ...prev, [key]: e.target.value }));
  }

  return (
    <Modal show={showModal} onClose={closeModal}>
      <Modal.Header>{isEdit ? "팝업 수정" : "팝업 등록"}</Modal.Header>
      <Modal.Body className="overflow-visible flex flex-col gap-4">

        <div className="flex items-center gap-4">
          <span className="font-bold">시작일</span>
          <TextInput
            className="basis-2/12"
            type="date"
            required
            value={formatDateForInput(popup.startDate)}
            onChange={(e) => { handleDateChange("startDate", e); }}
          />
          <span className="font-bold">종료일</span>
          <TextInput
            className="basis-2/12"
            type="date"
            required
            value={formatDateForInput(popup.endDate)}
            onChange={(e) => { handleDateChange("endDate", e); }}
          />
        </div>
        <div className="flex items-center gap-4">
          <label className="flex gap-2 items-center font-bold">
            활성화
            <Checkbox
              className="size-6"
              checked={popup.isUsed}
              onChange={(e) => { handleCheckboxChange("isUsed", e); }}
            />
          </label>
          <span className="font-bold">조회 가능 권한</span>
          <Select
            value={popup.authId}
            onChange={(e) => { handleLevelChange("authId", e); }}
          >
            <option value={1}>
              관리자
            </option>
            <option value={2}>
              운영진
            </option>
            <option value={3}>
              채용부서장
            </option>
            <option value={4}>
              채용담당자
            </option>
            <option value={5}>
              어시스턴트
            </option>
          </Select>
        </div>
        <div className="flex gap-4">
          <span className="font-bold whitespace-nowrap mt-2">내용</span>
          <Textarea
            value={popup.content}
            rows={6}
            onChange={(e) => { handleTextChange("content", e); }}
          />
        </div>

        {/* 내용, 시작일, 종료일, 권한 */}

      </Modal.Body>
      <Modal.Footer>
        <Button onClick={submitModal}>{isEdit ? "수정" : "등록"}</Button>
      </Modal.Footer>
    </Modal>
  )
}
