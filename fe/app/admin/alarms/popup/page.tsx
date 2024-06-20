"use client";

import { Dropdown, Table, theme } from "flowbite-react";
import { Pagination } from "flowbite-react";
import { twMerge } from "tailwind-merge";
import { useEffect, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { HiDotsHorizontal } from "react-icons/hi";
import PopupModal from "@/components/popup/PopupModal";

interface Popup {
  id: number;
  isUsed: boolean;
  content: string;
  startDate: string;
  endDate: string;
  authLevel: number;
}

const AuthLevels = [
  { label: "관리자", value: 1 },
  { label: "운영진", value: 2 },
  { label: "채용부서장", value: 3 },
  { label: "채용담당자", value: 4 },
  { label: "어시스턴트", value: 5 },
];

export default function AdminPopupPage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [popupList, setPopupList] = useState<Popup[]>([]);
  const localAxios = useLocalAxios();
  const [openModal, setOpenModal] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [selectedId, setSelectedId] = useState(0);
  const [isUpdate, setIsUpdate] = useState(false);
  const fetchData = async () => {
    await localAxios
      .post("/admin/popup/list", {
        pageNumber: currentPage - 1,
        size: size,
      })
      .then((Response) => {
        setPopupList(Response.data.popupMessages);
        setTotalPages(Response.data.totalPages);
        setTotalElements(Response.data.totalElements);
      });
  };

  const handleCreatePopup = () => {
    setIsEdit(false);
    setOpenModal(true);
  };

  const handleEdit = (popupId: number) => {
    setIsEdit(true);
    setSelectedId(popupId);
    setOpenModal(true);
  };

  const handleDelete = async (popupId: number) => {
    await localAxios.delete(`/admin/popup/${popupId}`).then((Response) => {
      if (Response.status === 200) {
        alert("삭제 성공");
      } else {
        alert("서버 오류");
      }
      setIsUpdate(!isUpdate);
    });
  };

  const onSubmit = () => {
    setIsUpdate(!isUpdate);
  };

  useEffect(() => {
    fetchData();
  }, [currentPage, size, isUpdate]);

  return (
    <div className="flex min-h-[calc(100vh-64px)] flex-col space-y-5 bg-white p-10">
      <div className="flex space-x-5">
        <div className="relative mt-10 w-full overflow-x-auto">
          <h3 className="mb-4 text-center text-3xl font-bold">
            팝업 메시지 현황
          </h3>
          <div className="flex flex-row  items-center justify-between">
            <p className="mr-2">
              {1 + (currentPage - 1) * size}-{currentPage * size} /{" "}
              {totalElements}
            </p>
            <div className="flex flex-row content-center justify-between">
              <button
                className="rounded-md bg-primary p-2 px-4 font-bold text-white"
                onClick={handleCreatePopup}
              >
                신규 등록
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className="flex space-x-5">
        <div className="relative w-full overflow-x-auto">
          <Table
            className="text-wrap text-center text-base text-gray-500 shadow-sm dark:text-gray-400"
            hoverable
          >
            <Table.Head className="bg-gray-50 text-base text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <Table.HeadCell scope="col" className="w-[350px] p-2">
                팝업 메시지
              </Table.HeadCell>
              <Table.HeadCell scope="col">활성여부</Table.HeadCell>
              <Table.HeadCell scope="col">시작일</Table.HeadCell>
              <Table.HeadCell scope="col">종료일</Table.HeadCell>
              <Table.HeadCell scope="col">최소 권한</Table.HeadCell>
              <Table.HeadCell scope="col">
                {/* 대상 권한 레벨 */}
              </Table.HeadCell>
            </Table.Head>
            <Table.Body>
              {popupList.map((popup) => (
                <Table.Row key={popup.id}>
                  <Table.Cell className="w-[30vw] whitespace-pre-wrap">
                    {popup.content}
                  </Table.Cell>
                  <Table.Cell>{popup.isUsed ? "Y" : "N"}</Table.Cell>
                  <Table.Cell>
                    {new Date(popup.startDate).toLocaleDateString()}
                  </Table.Cell>
                  <Table.Cell>
                    {new Date(popup.endDate).toLocaleDateString()}
                  </Table.Cell>
                  <Table.Cell>
                    {
                      AuthLevels.find((item) => item.value === popup.authLevel)
                        ?.label
                    }
                  </Table.Cell>
                  <Table.Cell>
                    <Dropdown
                      inline
                      label={<HiDotsHorizontal className="size-5" />}
                      theme={{
                        arrowIcon: "hidden",
                        floating: {
                          base: twMerge(theme.dropdown.floating.base, "w-40"),
                        },
                      }}
                    >
                      <Dropdown.Item onClick={() => handleEdit(popup.id)}>
                        설정 변경
                      </Dropdown.Item>
                      <Dropdown.Item
                        className="text-red-500"
                        onClick={() => handleDelete(popup.id)}
                      >
                        삭제
                      </Dropdown.Item>
                    </Dropdown>
                  </Table.Cell>
                </Table.Row>
              ))}
            </Table.Body>
          </Table>
          {popupList.length === 0 && (
            <p className="m-10 text-center text-xl">등록한 팝업이 없습니다.</p>
          )}
          <div className="pagenation-part mt-2 flex w-full justify-center">
            <Pagination
              currentPage={currentPage}
              nextLabel=""
              onPageChange={(page) => setCurrentPage(page)}
              previousLabel=""
              showIcons
              totalPages={totalPages}
            />
          </div>
        </div>
      </div>
      <PopupModal
        showModal={openModal}
        closeModal={() => setOpenModal(false)}
        isEdit={isEdit}
        popupId={isEdit ? selectedId : undefined}
        submit={onSubmit}
      />
    </div>
  );
}
