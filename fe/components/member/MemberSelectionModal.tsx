"use client";

import { useLocalAxios } from "@/app/api/axios";
import { MemberAdminFull, MemberAdminSummary } from "@/types/admin/Member";
import { MemberSearchResult } from "@/types/member/member";
import { Avatar, Button, Modal, Pagination, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";

interface MemberSelectionModalProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number, name: string) => void;
  size: string;
  title: string;
}
const MemberSelectionModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  size,
  title,
}: MemberSelectionModalProps) => {
  const [members, setMembers] = useState<MemberAdminFull[]>();
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [maxCount, setMaxCount] = useState<number>(1);
  const [keyword, setKeyword] = useState<string>("");

  useEffect(() => {
    if (isOpen) {
      const baseSetting = async () => {
        searchMember(keyword, currentPage);
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

  const searchMember = async (keyword: string, page: number) => {
    const response = await localAxios.post<MemberSearchResult>(
      `/member/search?page=${page - 1}`,
      {
        keyword: keyword,
        withDelete: false,
      },
    );
    setMembers(response.data.memberAdminFullDtos);
    setMaxCount(response.data.count);
  };

  const handleKeywordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(e.target.value);
  };

  const formatDate = (dateArray: Date) => {
    const date = new Date(dateArray);
    return `${date.getFullYear()}년 ${date.getMonth()}월 ${date.getDay()}일 ${date.getHours()}시`;
  };

  const hanldeOnCurrentPageChange = (page: number) => {
    setCurrentPage(page);
    searchMember(keyword, page);
  };
  return (
    <>
      <Modal show={isOpen} onClose={toggleDrawer} size={size}>
        <Modal.Header>{title}</Modal.Header>
        <Modal.Body>
          <div className="mb-5 flex w-full justify-center">
            <TextInput
              icon={() => (
                <HiSearch className="h-4 w-4 text-gray-500 dark:text-gray-400" />
              )}
              id="default-search"
              name="keyword"
              placeholder="사용자 이름을 입력하세요"
              required
              type="search"
              className="h-10 w-2/3"
              value={keyword}
              onChange={handleKeywordChange}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  searchMember(keyword, currentPage);
                }
              }}
            />
            <Button
              className="h-10 w-24 rounded-l-none rounded-r-lg bg-primary"
              onClick={() => searchMember(keyword, currentPage)}
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
                  직원 이름
                </th>
                <th scope="col" className="px-6 py-3">
                  사업부 명
                </th>
                <th scope="col" className="px-6 py-3">
                  부서명
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

                    <td className="px-6 py-4">
                      {m.name != null ? m.name : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {m.departmentName != null
                        ? m.departmentName
                        : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {m.teamName != null ? m.teamName : "해당 없음"}
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

          <div className="relative mx-auto w-full p-5">
            <Pagination
              currentPage={currentPage}
              totalPages={maxCount}
              layout="pagination"
              onPageChange={hanldeOnCurrentPageChange}
              className="mx-auto w-fit"
              previousLabel="이전"
              nextLabel="다음"
            />
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default MemberSelectionModal;
