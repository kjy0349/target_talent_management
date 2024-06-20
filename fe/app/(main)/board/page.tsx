"use client";
import { useState, useEffect } from "react";
import { Button, Pagination, Select } from "flowbite-react";
import BoardListRow from "@/components/board/BoardListRow";
import { useLocalAxios } from "@/app/api/axios";
import { Table } from "flowbite-react";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/stores/auth";

interface Board {
  id: number,
  title: string,
  writer: string,
  viewCount: number,
  createdAt: string,
  fileSource?: string,
}

export default function AdminBoardPage() {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState(1);
  const [boards, setBoards] = useState<Board[]>([]);
  const [maxPage, setMaxPage] = useState(1);
  const [size, setSize] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const router = useRouter();
  const authStore = useAuthStore();
  const auth = authStore.authLevel;

  const renderWrite = () => {
    if (auth && auth <= 2) return (
      <button
        className="rounded-md bg-primary p-2 px-4 text-white"
        onClick={() => router.push("/board/write")}
      >
        공지사항 등록
      </button>
    )
  }

  const fetchData = async () => {
    try {
      const response = await localAxios.post("/article/list", {
        boardId: 1,
        pageNumber: currentPage - 1,
        size: size,
      });
      setBoards(response.data.articles);
      setMaxPage(response.data.totalPages);
      setTotalElements(response.data.totalElements);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [currentPage]);

  useEffect(() => {
    setCurrentPage(1);
    fetchData();
  }, [size]);

  return (
    <div className="flex flex-col space-y-5 bg-white p-10">
      <div className="flex space-x-5">
        <div className="relative w-full overflow-x-auto p-10">
          <h3 className="mb-4 text-center text-3xl font-bold">공지사항</h3>
          <div className="mb-2 flex h-fit flex-row  items-center justify-between">
            <p className="mr-2">{1 + (currentPage - 1) * size}-{currentPage * size} / {totalElements}</p>
            <div className="flex flex-row items-center gap-4">
              <Select
                name=""
                value={size}
                className=""
                onChange={(e) => {
                  setSize(Number(e.target.value));
                }}
              >
                <option className="text-xs" value={10}>
                  10개씩 보기
                </option>
                <option className="text-xs" value={5}>
                  5개씩 보기
                </option>
              </Select>
              {renderWrite()}
            </div>
          </div>
          <Table className="text-base text-gray-500 shadow-sm dark:text-gray-400" hoverable>
            <Table.Head className="bg-gray-50 text-base text-gray-700 dark:bg-gray-700 dark:text-gray-400">
              <Table.HeadCell scope="col" className="px-6 py-3">
                제목
              </Table.HeadCell>
              <Table.HeadCell scope="col" className="px-6 py-3">
                작성자
              </Table.HeadCell>
              <Table.HeadCell scope="col" className="px-6 py-3">
                조회수
              </Table.HeadCell>
              <Table.HeadCell scope="col" className="px-6 py-3">
                등록일
              </Table.HeadCell>
              <Table.HeadCell scope="col" className="w-fit px-2">
                첨부
              </Table.HeadCell>
            </Table.Head>
            <Table.Body>
              {boards.map((article) => (
                <BoardListRow
                  key={article.id}
                  title={article.title}
                  author={article.writer}
                  views={article.viewCount}
                  createdAt={article.createdAt}
                  fileUrl={article.fileSource}
                  onClick={() => router.push(`/board/${article.id}`)}
                />
              ))}
            </Table.Body>
          </Table>
          {
            boards.length === 0 && (
              <p className="m-10 text-center text-xl">공지사항이 없습니다.</p>
            )
          }
          <div className="mt-4 w-full text-center">
            <Pagination
              currentPage={currentPage}
              onPageChange={(page) => setCurrentPage(page)}
              totalPages={maxPage}
              showIcons
              previousLabel="이전"
              nextLabel="다음"
              className=""
            />
          </div>
        </div>
      </div>
    </div>
  );
}
