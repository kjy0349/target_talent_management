"use client";

import { theme } from "flowbite-react";
import { Pagination } from "flowbite-react";
import BoardListRow from "@/components/board/BoardListRow";
import { twMerge } from "tailwind-merge";
import { useState } from "react";
export default function AdminBoardPage() {
  const [currentPage, setCurrentPage] = useState(1);
  return (
    <div className="flex flex-col space-y-5 p-5">
      <h3 className="text-xl">공지 관리 현황</h3>
      <div className="flex space-x-5  ">
        <div className="relative w-full overflow-x-auto ">
          <table className="w-full overflow-scroll text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
            <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <tr className="">
                <th scope="col" className="px-6 py-1">
                  제목
                </th>
                <th scope="col" className="px-6 py-1">
                  작성자
                </th>
                <th scope="col" className="px-6 py-1">
                  조회수
                </th>
                <th scope="col" className="px-6 py-1">
                  작성일
                </th>
              </tr>
            </thead>
            <tbody>
              <BoardListRow />
              <BoardListRow />
              <BoardListRow />
              <BoardListRow />
              <BoardListRow />
              <BoardListRow />
              <BoardListRow />
            </tbody>
          </table>
          <div className="pagenation-part mt-2 flex w-full justify-center">
            <Pagination
              currentPage={currentPage}
              nextLabel=""
              onPageChange={(page) => setCurrentPage(page)}
              previousLabel=""
              showIcons
              totalPages={20}
              theme={{
                pages: {
                  base: twMerge(theme.pagination.pages.base, "mt-0"),
                  next: {
                    base: twMerge(
                      theme.pagination.pages.next.base,
                      "w-[2.5rem] px-1.5 py-1.5",
                    ),
                    icon: "h-6 w-6",
                  },
                  previous: {
                    base: twMerge(
                      theme.pagination.pages.previous.base,
                      "w-[2.5rem] px-1.5 py-1.5",
                    ),
                    icon: "h-6 w-6",
                  },
                  selector: {
                    base: twMerge(
                      theme.pagination.pages.selector.base,
                      "w-[2.25rem] py-2 text-sm focus:border-primary",
                    ),
                  },
                },
              }}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
