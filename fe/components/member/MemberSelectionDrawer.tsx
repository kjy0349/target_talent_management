"use client";

import { useLocalAxios } from "@/app/api/axios";
import { MemberAdminSummary } from "@/types/admin/Member";
import { Avatar, Button, Pagination, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";

interface MemberSelectionDrawerProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number, name: string) => void;
}
const MemberSelectionDrawer = ({
  isOpen,
  toggleDrawer,
  onClose,
}: MemberSelectionDrawerProps) => {
  const [members, setMembers] = useState<MemberAdminSummary[]>();
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [maxCount, setMaxCount] = useState<number>(1);
  const [keyword, setKeyword] = useState<string>("");

  useEffect(() => {
    if (isOpen) {
      baseSetting();
    }
  }, [isOpen]);
  const baseSetting = async () => {
    searchMember();
    const countResponse = await localAxios.get(`/admin/member/count`);
    setMaxCount(countResponse.data);
  };
  const handleOnClick = (id: number, name: string) => {
    if (confirm("정말 선택하시겠습니까?")) {
      onClose(id, name);
      toggleDrawer();
    }
  };

  const searchMember = async () => {
    const response = await localAxios.post(
      `/admin/member/search?page=${currentPage - 1}`,
      {
        keyword: keyword,
        withDelete: false,
      },
    );
    // console.log(response);
    setMembers(response.data);
  };
  useEffect(() => {
    searchMember();
  }, [currentPage]);

  const handleKeywordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(e.target.value);
  };

  const formatDate = (dateArray: Date) => {
    const date = new Date(dateArray);
    return `${date.getFullYear()}년 ${date.getMonth()}월 ${date.getDay()}일 ${date.getHours()}시`;
  };
  return (
    <>
      {isOpen && (
        <div
          className="fixed bottom-0 left-0 right-0 z-40 h-5/6 w-full transform-none overflow-y-scroll bg-white p-2 transition-transform dark:bg-gray-800"
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
              <h2 className="mb-4 text-2xl font-bold">멤버 선택</h2>
            </div>
            <div className="relative mb-5 w-96">
              <TextInput
                icon={() => (
                  <HiSearch className="h-4 w-4 text-gray-500 dark:text-gray-400" />
                )}
                id="default-search"
                name="keyword"
                placeholder="프로젝트 명을 입력하세요"
                required
                type="search"
                className="h-10"
                value={keyword}
                onChange={handleKeywordChange}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    e.preventDefault();
                    searchMember();
                  }
                }}
              />
              <Button
                className="h- absolute inset-y-0 right-0 rounded-l-none rounded-r-lg bg-primary"
                onClick={() => searchMember()}
              >
                검색
              </Button>
            </div>
            <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
              <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  {/* <th scope="col" className="px-6 py-3">
                      <HiCheck />
                    </th> */}
                  <th scope="col" className="px-6 py-3">
                    직원 사진
                  </th>
                  <th scope="col" className="px-6 py-3">
                    직원 이름
                  </th>
                  <th scope="col" className="px-6 py-3">
                    사업부 명
                  </th>
                  <th scope="col" className="px-6 py-3">
                    최근 접속 일
                  </th>
                  <th scope="col" className="px-6 py-3">
                    선택
                  </th>
                </tr>
              </thead>
              <tbody>
                {members?.map((m, index) => (
                  <>
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
                        <Avatar img={m.profileImage} size={"lg"} />
                      </th>
                      <td className="px-6 py-4">
                        {m.name != null ? m.name : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        {m.departmentName != null
                          ? m.departmentName
                          : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        {m.lastAccessDate != null
                          ? formatDate(m.lastAccessDate)
                          : "해당 없음"}
                      </td>
                      <td className="px-6 py-4">
                        <button
                          className="mb-2 rounded-lg bg-slate-300 px-5 py-2.5 text-center text-xs font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                          type="button"
                          onClick={() => handleOnClick(m.id, m.name)}
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
          <div className="relative mx-auto w-full p-5">
            <Pagination
              currentPage={currentPage}
              totalPages={
                Math.ceil(maxCount / 4) == 0 ? 1 : Math.ceil(maxCount / 4)
              }
              layout="pagination"
              onPageChange={(page) => setCurrentPage(page)}
              className="mx-auto w-fit"
              previousLabel="이전"
              nextLabel="다음"
            />
          </div>
        </div>
      )}
    </>
  );
};

export default MemberSelectionDrawer;
