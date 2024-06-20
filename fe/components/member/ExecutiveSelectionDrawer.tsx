"use client";

import { useLocalAxios } from "@/app/api/axios";
import { ExecutiveAdminSummary } from "@/types/admin/Member";
import { Avatar, Pagination } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiCheck } from "react-icons/hi";
import { twMerge } from "tailwind-merge";

interface ExecutiveSelectionDrawerProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number, name: string) => void;
}
const ExecutiveSelectionDrawer = ({
  isOpen,
  toggleDrawer,
  onClose,
}: ExecutiveSelectionDrawerProps) => {
  const [executives, setExecutives] = useState<ExecutiveAdminSummary[]>();
  const localAxios = useLocalAxios();
  const baseUrl = "http://localhost:8080";

  useEffect(() => {
    if (isOpen) {
      baseSetting();
    }
  }, [isOpen]);
  const baseSetting = async () => {
    const response = await localAxios.get(`${baseUrl}/admin/executive`);
    setExecutives(response.data);
  };
  const handleOnClick = (id: number, name: string) => {
    if (confirm("정말 선택하시겠습니까?")) {
      onClose(id, name);
      toggleDrawer();
    }
  };
  return (
    <>
      {isOpen && (
        <div
          className="fixed bottom-0 left-0 right-0 z-40 h-96 w-full transform-none overflow-y-scroll bg-white p-2 transition-transform dark:bg-gray-800"
          tabIndex={-1}
          aria-labelledby="drawer-bottom-label"
        >
          <div className="relative mt-4 overflow-x-auto rounded-md border-2 p-3">
            <button
              type="button"
              onClick={() => {
                toggleDrawer();
              }}
              className="absolute end-2.5 top-2.5 inline-flex h-8 w-8 items-center justify-center rounded-lg bg-transparent text-sm text-gray-400 hover:bg-gray-200 hover:text-gray-900 dark:hover:bg-gray-600 dark:hover:text-white"
            >
              <svg
                className="h-3 w-3"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 14 14"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
                />
              </svg>
              <span className="sr-only">Close menu</span>
            </button>
            <div className="flex">
              <h2 className="mb-4 text-2xl font-bold">직급4 선택</h2>
            </div>

            <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" className="px-6 py-3">
                    직급4 이름
                  </th>
                  <th scope="col" className="px-6 py-3">
                    부서
                  </th>
                  <th scope="col" className="px-6 py-3">
                    직급
                  </th>
                  <th scope="col" className="px-6 py-3">
                    이메일
                  </th>
                  <th scope="col" className="px-6 py-3">
                    선택
                  </th>
                </tr>
              </thead>
              <tbody>
                {executives?.map((executive, index) => (
                  <>
                    <tr
                      key={index}
                      className="border-b bg-white dark:border-gray-700 dark:bg-gray-800"
                    >
                      <td className="px-6 py-4">
                        {executive.name != null ? executive.name : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        {executive.department != null
                          ? executive.department
                          : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        {executive.jobRank != null
                          ? executive.jobRank
                          : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        {executive.email != null
                          ? executive.email
                          : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        <button
                          className="mb-2 rounded-lg bg-slate-300 px-5 py-2.5 text-center text-xs font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                          type="button"
                          onClick={() =>
                            handleOnClick(executive.executiveId, executive.name)
                          }
                        >
                          선택
                        </button>
                      </td>
                    </tr>
                  </>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </>
  );
};

export default ExecutiveSelectionDrawer;
