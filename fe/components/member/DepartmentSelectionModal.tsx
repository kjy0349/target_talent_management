"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  DepartmentAdminFull,
  MemberAdminFull,
  MemberAdminSummary,
} from "@/types/admin/Member";
import { MemberSearchResult } from "@/types/member/member";
import { Avatar, Button, Modal, Pagination, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";

interface DepartmentSelectionModalProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number, name: string) => void;
  size: string;
  title: string;
}
const DepartmentSelectionModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  size,
  title,
}: DepartmentSelectionModalProps) => {
  const [departments, setDepartments] = useState<DepartmentAdminFull[]>();
  const localAxios = useLocalAxios();

  useEffect(() => {
    if (isOpen) {
      const baseSetting = async () => {
        searchDepartments();
      };
      baseSetting();
    }
  }, [isOpen]);

  const handleOnClick = (id: number, name: string) => {
    if (confirm("정말 선택하시겠습니까?")) {
      onClose(id, name);
      toggleDrawer();
    }
  };

  const searchDepartments = async () => {
    const response =
      await localAxios.get<DepartmentAdminFull[]>(`/admin/department`);
    setDepartments(response.data);
  };

  const formatDate = (dateArray: Date) => {
    const date = new Date(dateArray);
    return `${date.getFullYear()}년 ${date.getMonth()}월 ${date.getDay()}일 ${date.getHours()}시`;
  };

  return (
    <>
      <Modal show={isOpen} onClose={toggleDrawer} size={size}>
        <Modal.Header>{title}</Modal.Header>
        <Modal.Body>
          <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
            <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                {/* <th scope="col" className="px-6 py-3">
                      <HiCheck />
                    </th> */}

                <th scope="col" className="px-6 py-3">
                  사업부 명
                </th>
                <th scope="col" className="px-6 py-3">
                  사업부 설명
                </th>
                <th scope="col" className="px-6 py-3">
                  부서장
                </th>
                <th scope="col" className="px-6 py-3">
                  선택
                </th>
              </tr>
            </thead>
            <tbody>
              {departments?.map((d, index) => (
                <>
                  <tr
                    key={d.departmentId}
                    className="border-b bg-white dark:border-gray-700 dark:bg-gray-800"
                  >
                    {/* <td className="px-6 py-4">
                          <input type="checkbox" />
                        </td> */}

                    <td className="px-6 py-4">
                      {d.name != null ? d.name : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {d.description != null ? d.description : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {d.managerName != null ? d.managerName : "해당 없음"}
                    </td>

                    <td className="px-6 py-4">
                      <button
                        className="mb-2 rounded-lg bg-slate-300 px-5 py-2.5 text-center text-xs font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                        type="button"
                        onClick={() => handleOnClick(d.departmentId, d.name)}
                      >
                        선택
                      </button>
                    </td>
                  </tr>
                </>
              ))}
            </tbody>
          </table>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default DepartmentSelectionModal;
