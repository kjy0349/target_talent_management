"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  ProjectSummary,
  ProjectListFull,
  ProjectSearchCondition,
} from "@/types/admin/Project";
import {
  Button,
  Modal,
  Pagination,
  Select,
  TextInput,
  theme,
} from "flowbite-react";
import { useEffect, useState, ChangeEvent, KeyboardEvent } from "react";
import {
  HiCheck,
  HiChevronLeft,
  HiChevronRight,
  HiSearch,
} from "react-icons/hi";
import { twMerge } from "tailwind-merge";

interface ProjectSelectionModalProps {
  isOpen: boolean;
  toggleDrawer: () => void;
  onClose: (targetProjectId: number) => void;
  size: string;
  title: string;
  projectId?: number;
}

const ProjectSelectionModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  title,
  projectId,
}: ProjectSelectionModalProps) => {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [selected, setSelected] = useState<number>();
  const [searchCondition, setSearchCondition] =
    useState<ProjectSearchCondition>({
      countryName: undefined,
      departmentId: undefined,
      targetYear: undefined,
      orderBy: "PJT_NEW",
      keyword: "",
      memberId: undefined,
      isPrivate: undefined,
      projectType: undefined,
      targetCountry: undefined,
      targetJobRankId: undefined,
    });
  const [maxCount, setMaxCount] = useState<number>();
  const [maxPage, setMaxPage] = useState<number>();
  const [projects, setProjects] = useState<ProjectSummary[]>();
  const [size, setSize] = useState<number>(10);
  const [keyword, setKeyword] = useState<string>("");

  const searchProject = async (
    searchCondition: ProjectSearchCondition,
    page: number,
    size: number,
  ) => {
    setProjects([]);
    const response = await localAxios.post<ProjectListFull>(
      `/project/search?page=${page - 1}&size=${size}`,
      searchCondition,
    );

    const projectList = response.data.projectSummaryDtos;
    setProjects(projectList);

    setMaxCount(response.data.totalCount);
    setMaxPage(response.data.totalPages);
  };

  useEffect(() => {
    searchProject(searchCondition, currentPage, size);
  }, []);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    searchProject(searchCondition, page, size);
  };

  const handleSelectId = (id: number) => {
    setSelected(id);
  };
  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth() + 1}.${newDate.getUTCDate()}`;
  };

  const handleOnChangeKeyword = (e: ChangeEvent<HTMLInputElement>) => {
    setKeyword(e.target.value);
  };

  const handleOnKeyDownEnter = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const nextCondition = {
        ...searchCondition,
        keyword: keyword,
      };
      searchProject(nextCondition, currentPage, size);
      setSearchCondition(nextCondition);
    }
  };

  const onChangePageValue = (page: number) => {
    searchProject(searchCondition, page, size);
  };

  const getProjectTypeKor = (key: string) => {
    switch (key) {
      case "BOTH":
        return "전사/사업부";
      case "ALL_COMPANY":
        return "전사";
      default:
        return "사업부";
    }
  };

  return (
    <>
      {isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div className="fixed inset-0 bg-gray-900 opacity-50"></div>
          <div className="relative mx-auto w-11/12 max-w-6xl bg-white shadow-lg">
            <div className="flex items-center justify-between border-b p-4">
              <h2 className="text-xl font-semibold">
                {title}{" "}
                <span className="text-lg font-medium">총:{maxCount}개</span>
              </h2>
              <div className="flex items-center space-x-2">
                <Button
                  onClick={() => {
                    if (projectId == selected) {
                      alert("동일한 프로젝트에는 복사할 수 없습니다!.");
                      return;
                    }

                    if (selected) {
                      onClose(selected);
                      setSelected(undefined);
                      toggleDrawer();
                    } else {
                      alert("프로젝트를 선택해주세요!");
                    }
                  }}
                >
                  프로젝트 추가하기
                </Button>
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
            </div>
            <div className="max-h-[70vh] overflow-y-auto p-4">
              <div className="mb-4 flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <TextInput
                    icon={() => (
                      <HiSearch className="h-4 w-4 text-gray-500 dark:text-gray-400" />
                    )}
                    id="default-search"
                    name="keyword"
                    placeholder="프로젝트 명칭을 입력하세요"
                    required
                    type="search"
                    className="h-10 w-80"
                    value={keyword}
                    onChange={handleOnChangeKeyword}
                    onKeyDown={handleOnKeyDownEnter}
                  />
                  <Button
                    className="h-10 rounded-l-none rounded-r-lg bg-primary"
                    onClick={() => {
                      const nextCondition = {
                        ...searchCondition,
                        keyword: keyword,
                      };
                      searchProject(nextCondition, currentPage, size);
                      setSearchCondition(nextCondition);
                    }}
                  >
                    검색
                  </Button>
                </div>
                <div className="flex items-center space-x-2">
                  <Select
                    id="size"
                    value={size}
                    onChange={(e) => {
                      setSize(Number(e.target.value));
                      searchProject(
                        searchCondition,
                        currentPage,
                        Number(e.target.value),
                      );
                    }}
                  >
                    <option value={10}>10개씩 보기</option>
                    <option value={20}>20개씩 보기</option>
                  </Select>
                  <Select
                    id="order"
                    value={searchCondition.orderBy}
                    onChange={(e) => {
                      const nextCondition = {
                        ...searchCondition,
                        orderBy: e.target.value,
                      };
                      searchProject(nextCondition, currentPage, size);
                      setSearchCondition(nextCondition);
                    }}
                  >
                    <option value={"PJT_NEW"}>PJT 생성 최신 순</option>
                    <option value={"PJT_OLD"}>PJT 생성 오래된 순</option>
                    <option value={"POOL_DESC"}>Pool 많은 순</option>
                    <option value={"POOL_ASC"}>Pool 적은 순</option>
                    <option value={"NAME_ASC"}>PJT 이름 오름차순</option>
                    <option value={"NAME_DESC"}>PJT 이름 내림차순</option>
                  </Select>
                </div>
              </div>
              <table className="w-full text-left text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
                <thead className="bg-gray-100 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  <tr>
                    <th scope="col" className="px-4 py-3">
                      <HiCheck />
                    </th>
                    <th scope="col" className="px-4 py-3">
                      프로젝트 명칭
                    </th>
                    <th scope="col" className="px-4 py-3">
                      타겟 연도
                    </th>
                    <th scope="col" className="px-4 py-3">
                      사업부
                    </th>
                    <th scope="col" className="px-4 py-3">
                      타겟직급
                    </th>
                    <th scope="col" className="px-4 py-3">
                      프로젝트 구분
                    </th>
                    <th scope="col" className="px-4 py-3">
                      인원
                    </th>
                    <th scope="col" className="px-4 py-3">
                      담당자
                    </th>
                    <th scope="col" className="px-4 py-3">
                      생성일
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {projects?.map((p) => (
                    <tr
                      key={p.id}
                      className="border-b bg-white dark:border-gray-700 dark:bg-gray-800"
                    >
                      <td className="px-4 py-3">
                        <input
                          type="checkbox"
                          checked={selected === p.id}
                          onChange={() => handleSelectId(p.id)}
                        />
                      </td>
                      <td className="px-4 py-3">{p.title}</td>
                      <td className="px-4 py-3">{p.targetYear}</td>
                      <td className="px-4 py-3">{p.departmentName}</td>
                      <td className="px-4 py-3">
                        {p.targetJobRanks.map((data) => data + " ")}
                      </td>
                      <td className="px-4 py-3">
                        {getProjectTypeKor(p.projectType)}
                      </td>
                      <td className="px-4 py-3">{p.poolSize}</td>
                      <td className="px-4 py-3">{p.responsibleMemberName}</td>
                      <td className="px-4 py-3">
                        {parseLocalDateTime(p.createAt)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="border-t p-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <HiChevronLeft
                    className={twMerge(
                      "cursor-pointer text-2xl",
                      currentPage === 1 && "text-gray-300",
                    )}
                    onClick={() => {
                      if (currentPage > 1) {
                        handlePageChange(currentPage - 1);
                      }
                    }}
                  />
                  <span className="text-sm font-normal text-gray-500 dark:text-gray-400">
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {(currentPage - 1) * size + 1}
                    </span>
                    &nbsp;-&nbsp;
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {currentPage * size > maxCount!
                        ? maxCount
                        : currentPage * size}
                    </span>
                  </span>
                  <HiChevronRight
                    className={twMerge(
                      "cursor-pointer text-2xl",
                      currentPage === maxPage && "text-gray-300",
                    )}
                    onClick={() => {
                      if (currentPage < maxPage!) {
                        handlePageChange(currentPage + 1);
                      }
                    }}
                  />
                </div>
                <Pagination
                  currentPage={currentPage}
                  totalPages={maxPage || 1}
                  onPageChange={onChangePageValue}
                  className="mx-auto"
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};
export default ProjectSelectionModal;
