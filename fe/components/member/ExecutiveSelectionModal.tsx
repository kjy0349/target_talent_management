"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  ExecutiveAdminSummary,
  ExecutiveSearchResult,
} from "@/types/admin/Member";

import { Avatar, Button, Modal, Pagination, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";

interface ExecutiveSelectionModalProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (id: number, name: string) => void;
  size: string;
  title: string;
}
const ExecutiveSelectionModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  size,
  title,
}: ExecutiveSelectionModalProps) => {
  const [executives, setExecutives] = useState<ExecutiveAdminSummary[]>();
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [maxCount, setMaxCount] = useState<number>(1);
  const [keyword, setKeyword] = useState<string>("");

  useEffect(() => {
    if (isOpen) {
      const baseSetting = async () => {
        searchExecutive(keyword, currentPage);
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

  const searchExecutive = async (keyword: string, page: number) => {
    const response = await localAxios.post<ExecutiveSearchResult>(
      `/networking/executive/search?page=${page - 1}`,
      {
        keyword: keyword,
        withDelete: false,
      },
    );
    setExecutives(response.data.executiveAdminSummaryDtos);
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
    searchExecutive(keyword, page);
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
              placeholder="직급4 이름을 입력하세요"
              required
              type="search"
              className="h-10 w-2/3"
              value={keyword}
              onChange={handleKeywordChange}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  e.preventDefault();
                  searchExecutive(keyword, currentPage);
                }
              }}
            />
            <Button
              className="h-10 w-24 rounded-l-none rounded-r-lg bg-primary"
              onClick={() => searchExecutive(keyword, currentPage)}
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
              {executives?.map((m, index) => (
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
                      {m.department != null ? m.department : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {m.jobRank != null ? m.jobRank : "해당 없음"}
                    </td>
                    <td className="px-6 py-4">
                      {m.email != null ? m.email : "해당 없음"}
                    </td>

                    <td className="px-6 py-4">
                      <button
                        className="mb-2 rounded-lg bg-slate-300 px-5 py-2.5 text-center text-xs font-medium text-white hover:bg-slate-400 focus:outline-none focus:ring-4"
                        type="button"
                        onClick={() => handleOnClick(m.executiveId, m.name)}
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

export default ExecutiveSelectionModal;
