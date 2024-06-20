"use client";

import { useLocalAxios } from "@/app/api/axios";
import { NetworkingProfilePreviewAdminSummary } from "@/types/networking/Networking";
import { Pagination } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiCheck } from "react-icons/hi";
import { twMerge } from "tailwind-merge";

interface ProfileSelectionDrawerProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number) => void;
}
const ProfileSelectionDrawer = ({
  isOpen,
  toggleDrawer,
  onClose,
}: ProfileSelectionDrawerProps) => {
  const [profiles, setProfiles] =
    useState<NetworkingProfilePreviewAdminSummary[]>();
  const localAxios = useLocalAxios();
  const baseUrl = "http://localhost:8080";

  useEffect(() => {
    if (isOpen) {
      baseSetting();
    }
  }, [isOpen]);
  const baseSetting = async () => {
    const response = await localAxios.get(`${baseUrl}/admin/profile/preview`);
    setProfiles(response.data);
  };
  const handleOnClick = (id: number) => {
    if (confirm("정말 선택하시겠습니까?")) {
      onClose(id);
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
              <h2 className="mb-4 text-2xl font-bold">네트워킹 인재 선택</h2>
            </div>

            <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  {/* <th scope="col" className="px-6 py-3">
                    <HiCheck />
                  </th> */}
                  <th scope="col" className="px-6 py-3">
                    인재 이름
                  </th>
                  <th scope="col" className="px-6 py-3">
                    현재 직장
                  </th>
                  <th scope="col" className="px-6 py-3">
                    학력
                  </th>
                  <th scope="col" className="px-6 py-3">
                    전문 분야
                  </th>
                  <th scope="col" className="px-6 py-3">
                    선택
                  </th>
                </tr>
              </thead>
              <tbody>
                {profiles?.map((p, index) => (
                  <tr
                    key={index}
                    className="border-b bg-white dark:border-gray-700 dark:bg-gray-800"
                  >
                    {/* <td className="px-6 py-4">
                        <input type="checkbox" />
                      </td> */}
                    <th
                      scope="row"
                      className="whitespace-nowrap px-6 py-4 font-medium text-gray-900 dark:text-white"
                    >
                      {p.name}
                    </th>
                    <td className="px-6 py-4">{p.companyName}</td>
                    <td className="px-6 py-4">
                      {p.schoolName} - {p.schoolMajor} - {p.schoolDegree}
                    </td>
                    <td className="px-6 py-4">
                      {p.techDetailName ? p.techDetailName : "입력 값 없음"}
                    </td>
                    <td className="px-6 py-4">
                      <button
                        className="mb-2 rounded-lg bg-slate-300 px-5 py-2.5 text-center text-xs font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                        type="button"
                        onClick={() => handleOnClick(p.profileId)}
                      >
                        선택
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </>
  );
};

export default ProfileSelectionDrawer;
