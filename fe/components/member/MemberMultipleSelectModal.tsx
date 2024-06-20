"use client";
import React, { useEffect, useState } from "react";
import { HiCheck, HiSearch } from "react-icons/hi";
import { Avatar, Badge, Pagination } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { MemberSearchResult } from "@/types/member/member";
import { MemberAdminFull } from "@/types/admin/Member";

interface MemberMultipleSelectModalProps {
  isOpen: boolean;
  title: string;
  toggleDrawer: () => void;
  onClose: (ids: number[]) => void;
  openers?: number[];
}

const MemberMultipleSelectModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  title,
  openers,
}: MemberMultipleSelectModalProps) => {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [selected, setSelected] = useState<number[]>([]);
  const [searchCondition, setSearchCondition] = useState<{
    keyword: string;
  }>({
    keyword: "",
  });
  const [maxCount, setMaxCount] = useState<number>(0);
  const [members, setMembers] = useState<MemberAdminFull[]>([]);

  const handleKeywordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchCondition({
      keyword: e.target.value,
    });
  };

  const searchMembers = async (page: number) => {
    const response = await localAxios.post<MemberSearchResult>(
      `/admin/member/search?page=${page - 1}&&size=4`,
      {
        keyword: searchCondition.keyword,
        withDelete: false,
        memberIds: openers,
      },
    );

    setMembers(response.data.memberAdminFullDtos);

    setMaxCount(response.data.count);
  };

  useEffect(() => {
    searchMembers(currentPage);
  }, [currentPage]);

  const handleSelectId = (id: number) => {
    if (selected.includes(id)) {
      const nextSelected = selected.filter((sid) => sid !== id);
      setSelected(nextSelected);
    } else {
      const nextSelected = [...selected, id];
      setSelected(nextSelected);
    }
  };

  const formatDate = (date: Date) => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}-${newDate.getMonth() + 1
      }-${newDate.getDate()}`;
  };

  const handleCloseMultipleSectionModal = () => {
    setSelected([]);
    toggleDrawer();
  };
  const handleCheckBoxChanged = (
    e: React.ChangeEvent<HTMLInputElement>,
    id: number,
  ) => {
    if (e.target.checked) {
      setSelected([...selected, id]);
    } else {
      const nextSelected = selected.filter((sid) => sid != id);
      setSelected(nextSelected);
    }
  };

  return (
    <>
      {isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div className="fixed inset-0 bg-gray-900 opacity-50"></div>
          <div className="relative mx-auto w-11/12 max-w-6xl bg-white shadow-lg">
            <div className="flex items-center justify-between border-b p-4">
              <h2 className="text-xl font-semibold">{title}</h2>
              <button
                className="text-gray-500 hover:text-gray-700"
                onClick={toggleDrawer}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
            <div className="max-h-[80vh] overflow-y-auto p-4">
              <div className="mb-5 flex items-center justify-center">
                <div className="relative">
                  <input
                    type="search"
                    id="member-search"
                    className="w-96 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="직원 이름을 입력하세요"
                    value={searchCondition.keyword}
                    onChange={handleKeywordChange}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                        searchMembers(1);
                      }
                    }}
                  />
                  <button
                    type="button"
                    className="absolute right-0 top-0 rounded-r-lg border border-blue-700 bg-blue-700 p-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300"
                    onClick={() => searchMembers(1)}
                  >
                    <HiSearch className="h-5 w-5" />
                  </button>
                </div>
              </div>
              <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
                <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  <tr>
                    <th scope="col" className="px-2 py-3">
                      <HiCheck />
                    </th>
                    <th scope="col" className="px-2 py-3">
                      직원 사진
                    </th>
                    <th scope="col" className="px-2 py-3">
                      직원 이름
                    </th>
                    <th scope="col" className="px-2 py-3">
                      사업부 명
                    </th>
                    <th scope="col" className="px-2 py-3">
                      최근 접속 일
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {members?.map((m, index) => (
                    <tr
                      key={m.id}
                      className="border-b bg-white dark:border-gray-700 dark:bg-gray-800"
                    >
                      <td>
                        <input
                          type="checkbox"
                          checked={selected.includes(m.id)}
                          onChange={(e) => handleCheckBoxChanged(e, m.id)}
                        />
                      </td>
                      <td
                        scope="row"
                        className="flex justify-start whitespace-nowrap px-2 py-4 font-medium text-gray-900 dark:text-white"
                      >
                        <Avatar
                          img={m.profileImage}
                          size={"lg"}
                          className="p-1"
                        />
                      </td>
                      <td className="px-2 py-4">
                        <span className="flex items-center gap-1">
                          {m.name != null ? m.name : "해당 없음"}{" "}
                          <Badge className="w-fit">({m.knoxId})</Badge>
                        </span>
                      </td>
                      <td className="px-2 py-4">
                        {m.departmentName != null
                          ? m.departmentName
                          : "해당 없음"}
                      </td>
                      <td className="px-2 py-4">
                        {m.lastAccessDate != null
                          ? formatDate(m.lastAccessDate)
                          : "해당 없음"}
                      </td>

                      {/* <td className="px-2 py-4">
                        <button
                          className={`mb-2 rounded-lg px-5 py-2.5 text-center text-xs font-medium text-white ${
                            selected.includes(m.id)
                              ? "bg-blue-600 hover:bg-blue-700"
                              : "bg-slate-300 hover:bg-slate-400"
                          }`}
                          type="button"
                          onClick={() => handleSelectId(m.id)}
                        >
                          {selected.includes(m.id) ? "선택 해제" : "선택"}
                        </button>
                      </td> */}
                    </tr>
                  ))}
                </tbody>
              </table>
              {members.length == 0 && (
                <div className="flex w-full justify-center">
                  더 이상 등록 가능한 사용자가 없습니다.
                </div>
              )}
              <div className="relative mx-auto w-full p-5">
                <Pagination
                  currentPage={currentPage}
                  totalPages={maxCount}
                  layout="pagination"
                  onPageChange={(page) => setCurrentPage(page)}
                  className="mx-auto w-fit"
                  previousLabel="이전"
                  nextLabel="다음"
                />
              </div>
            </div>
            <div className="flex justify-end space-x-4 p-4">
              <button
                className="rounded-lg bg-slate-300 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                onClick={handleCloseMultipleSectionModal}
              >
                취소
              </button>
              <button
                className="rounded-lg bg-blue-700 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300"
                onClick={() => {
                  const nextSelected = [...selected];
                  onClose(nextSelected);

                  setSelected([]);
                  setSearchCondition({
                    ...searchCondition,
                    keyword: "",
                  });
                }}
              >
                선택 완료
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default MemberMultipleSelectModal;
