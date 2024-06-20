import { useLocalAxios } from "@/app/api/axios";
import { Button, Label, Modal, Radio, Select, Table, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { ProjectSearchCondition, ProjectSummary, ProjectListFull } from "@/types/admin/Project";

import { DepartmentAdminSummary } from "@/types/admin/Member";
import { HiChevronLeft, HiChevronRight, HiSearch } from "react-icons/hi";
import ProjectListRowSimple from "./ProjectListRowSimple";

export default function AddToProjectModal({
  showModal,
  closeModal,
  addToProject,
}: {
  showModal: boolean;
  closeModal: () => void;
  addToProject: (
    projectId: number,
  ) => void;
}) {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState(1);
  const [size, setSize] = useState(10);
  const [maxCount, setMaxCount] = useState(10);
  const [maxPage, setMaxPage] = useState(10);
  const [projects, setProjects] = useState<ProjectSummary[]>([]);
  const [keyword, setKeyword] = useState<string>();

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

  const getProjects = async (
    page: number,
    searchCondition: ProjectSearchCondition,
    size: number,
  ) => {
    setProjects([]);

    const response = await localAxios.post<ProjectListFull>(
      `/project/search?page=${page - 1}&&size=${size}`,
      searchCondition,
    );
    setProjects(response.data.projectSummaryDtos);

    setMaxCount(response.data.totalCount);
    setMaxPage(response.data.totalPages);
  };

  useEffect(() => {
    getProjects(currentPage, searchCondition, size);
  }, [currentPage, searchCondition, size]);


  const handleOnChangeKeyword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setKeyword(e.target.value);
  };

  const handleOnKeyDownEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const nextCondition = {
        ...searchCondition,
        keyword: keyword,
      };
      getProjects(currentPage, nextCondition, size);
    }
  };

  const handlePageUp = () => {
    if (currentPage + 1 > maxPage) {
      alert(" 최대 사이즈를 넘었습니다.");
    } else {
      getProjects(currentPage + 1, searchCondition, size);
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePageDown = () => {
    if (currentPage == 1) {
      alert("이전 페이지가 없습니다.");
    } else {
      getProjects(currentPage - 1, searchCondition, size);
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal}>
      <Modal.Header>프로젝트에 추가</Modal.Header>
      <Modal.Body>
        <div className="mx-4 border-b dark:border-gray-700">
          <div className="mx-4 border-b dark:border-gray-700">
            <div className="flex flex-row items-center justify-between space-x-3">
              <h5 className="mb-4 text-xl font-bold dark:text-white">
                전체 프로젝트
              </h5>
            </div>
            <div className="flex flex-row items-center justify-between py-3 md:space-x-4">
              <div className="flex w-2/5 flex-row items-center">
                <form className="flex-1 items-center">
                  <div className="relative w-full">
                    <TextInput
                      icon={() => (
                        <HiSearch className="size-4 text-gray-500 dark:text-gray-400" />
                      )}
                      id="default-search"
                      name="default-search"
                      placeholder="검색어"
                      required
                      type="search"
                      className="h-10"
                      value={keyword}
                      onChange={handleOnChangeKeyword}
                      onKeyDown={handleOnKeyDownEnter}
                    />
                    <Button
                      className="absolute inset-y-0 right-0 h-[42px] rounded-l-none rounded-r-lg bg-primary"
                      onClick={() => {
                        const nextCondition = {
                          ...searchCondition,
                          keyword: keyword,
                        };
                        getProjects(currentPage, nextCondition, size);
                        setSearchCondition(nextCondition);
                      }}
                    >
                      검색
                    </Button>
                  </div>
                </form>
              </div>
              <div className="flex w-1/2 items-center justify-end">
                <div className="mt-3 flex items-center px-2 lg:block">
                  <Select
                    id="order"
                    name=""
                    value={size}
                    onChange={(e) => {
                      setSize(Number(e.target.value));
                      getProjects(
                        currentPage,
                        searchCondition,
                        Number(e.target.value),
                      );
                    }}
                  >
                    <option className="text-xs" value={10}>
                      10개씩 보기
                    </option>

                    <option className="text-xs" value={20}>
                      20개씩 보기
                    </option>
                  </Select>
                </div>

                <div className="mt-3 flex items-center px-2 ">
                  <label htmlFor={"order"} className="mr-2 whitespace-nowrap text-xs">
                    정렬 기준:
                  </label>
                  <Select
                    id="order"
                    name=""
                    value={searchCondition.orderBy}
                    onChange={(e) => {
                      const nextCondition = {
                        ...searchCondition,
                        orderBy: e.target.value,
                      };
                      getProjects(currentPage, nextCondition, size);
                      setSearchCondition(nextCondition);
                    }}
                  >
                    <option className="text-xs" value={"PJT_NEW"}>
                      PJT 생성 최신 순
                    </option>

                    <option className="text-xs" value={"PJT_OLD"}>
                      PJT 생성 오래된 순
                    </option>
                    <option className="text-xs" value={"POOL_DESC"}>
                      Pool 많은 순
                    </option>
                    <option className="text-xs" value={"POOL_ASC"}>
                      Pool 적은 순
                    </option>
                    <option className="text-xs" value={"NAME_ASC"}>
                      PJT 이름 오름차순
                    </option>
                    <option className="text-xs" value={"NAME_DESC"}>
                      PJT 이름 내림차순
                    </option>
                  </Select>
                </div>
                <nav
                  className="mt-3 flex flex-row items-center justify-between p-2"
                  aria-label="Table navigation"
                >
                  <HiChevronLeft
                    className="cursor-pointer text-2xl"
                    onClick={handlePageDown}
                  />
                  <span className="text-sm font-normal text-gray-500 dark:text-gray-400">
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {(currentPage - 1) * 10 + 1}
                    </span>
                    &nbsp;-&nbsp;
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {currentPage * 10 ? currentPage * size : maxCount}
                    </span>
                  </span>
                  <HiChevronRight
                    className="cursor-pointer text-2xl"
                    onClick={handlePageUp}
                  />
                </nav>
              </div>
            </div>
          </div>
          <div className="w-full overflow-y-scroll">
            <table className=" w-full border-collapse  text-left text-sm dark:text-gray-400 rtl:text-right">
              <thead className="bg-gray-100 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                <th scope="col" className="px-4 py-3">
                  명칭
                </th>
                <th scope="col" className="px-4 py-3">
                  연도
                </th>
                <th scope="col" className="whitespace-nowrap px-4 py-3">
                  사업부명
                </th>
                <th scope="col" className="whitespace-nowrap px-4 py-3">
                  구분
                </th>
                <th scope="col" className="whitespace-nowrap px-4 py-3">
                  인원
                </th>
                <th scope="col" className="whitespace-nowrap px-4 py-3">
                  담당자
                </th>
                <th scope="col" className="whitespace-nowrap px-4 py-3">
                </th>
                <th></th>
              </thead>
              <tbody className="overflow-y-scroll ">
                {projects &&
                  projects.length > 0 &&
                  projects?.map((p) => (
                    <ProjectListRowSimple
                      key={p.id}
                      project={p}
                      onClick={() => addToProject(p.id)}
                      clickable={true}
                    />
                  ))}
              </tbody>
            </table>
            {projects?.length === 0 && (
              <div className="my-20 w-full text-center">
                프로젝트가 없습니다.
              </div>
            )}
          </div>
        </div>
      </Modal.Body>
    </Modal >
  );
}
