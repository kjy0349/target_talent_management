"use client";

import {
  Button,
  Label,
  Pagination,
  Radio,
  Table,
  TextInput,
  Select,
} from "flowbite-react";

import { useState, useEffect, ChangeEvent, KeyboardEvent, useRef } from "react";
import {
  HiArrowLeft,
  HiChevronLeft,
  HiChevronRight,
  HiPlus,
  HiSearch,
} from "react-icons/hi";
import ProjectListRow from "../../../components/project/ProjectListRow";
import { ProjectAdminModal } from "../../../components/admin/ProjectAdminModal";
import { ProjectModal } from "../../../components/project/ProjectModal";
import {
  ProjectSummary,
  ProjectListFull,
  ProjectSearchCondition,
  ProjectStaticsFull,
  ProjectFilterFull,
} from "@/types/admin/Project";
import { DepartmentSummary } from "@/types/admin/Member";
import { useLocalAxios } from "@/app/api/axios";
import next from "next";
import { ProjectUpdateModal } from "@/components/project/ProjectUpdateModal";
import { useAuthStore } from "@/stores/auth";
import Spinner from "@/components/common/Spinner";

export default function ProjectTable() {
  const [currentPage, setCurrentPage] = useState(1);
  const [maxCount, setMaxCount] = useState(0);
  const [maxPage, setMaxPage] = useState(0);
  const [projects, setProjects] = useState<ProjectSummary[]>();
  const [targetYears, setTargetYears] = useState<number[]>([]);
  const [departments, setDepartments] = useState<DepartmentSummary[]>([]);
  const localAxios = useLocalAxios();
  const [keyword, setKeyword] = useState<string>("");
  const [size, setSize] = useState<number>(10);
  const [statics, setStatics] = useState<ProjectStaticsFull>();
  const [isMounted, setIsMounted] = useState(false);

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
  const [showProjectModal, setShowProjectModal] = useState(false);
  const [showProjectAddModal, setShowProjectAddModal] = useState(false);
  const [modalProjectId, setModalProjectId] = useState(0);
  const auth = useAuthStore();
  const handleOpenProjectShowModal = (id: number) => {
    setShowProjectModal(true);
    setModalProjectId(id);
  };

  const handleCloseProjectShowModal = () => {
    setShowProjectModal(false);
    setModalProjectId(0);
    isCalled.current = false;
    getProjectFilter();
  };

  const handleOpenProjectAddModal = () => {
    setShowProjectAddModal(true);
  };

  const handleCloseProjectAddModal = () => {
    setShowProjectAddModal(false);
    setModalProjectId(0);
    isCalled.current = false;
    getProjectFilter();
  };
  useEffect(() => {
    getProjectFilter();
  }, []);
  const [projectFull, setProjectFull] = useState<ProjectListFull>();

  const [projectFilter, setProjectFilter] = useState<ProjectFilterFull>();

  const getProjectFilter = async () => {
    const response = await localAxios.get(`/project/filters`);
    setProjectFilter(response.data);
    setStatics(response.data.projectStaticsFullDto);
  };

  const isCalled = useRef<boolean>(false);

  useEffect(() => {
    getProjects(currentPage, searchCondition, size);
  }, [projectFilter]);

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
    setTargetYears(response.data.targetYears);
    setDepartments(response.data.departmentAdminSummaryDtos);
    if (!isCalled.current) {
      setMaxCount(response.data.totalCount);
      setMaxPage(response.data.totalPages);
    }

    setProjectFull(response.data);

    isCalled.current = true;
  };

  const onDeleteParent = () => {
    isCalled.current = false;
    getProjectFilter();
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

  useEffect(() => {
    if (auth && auth.authLevel && isCalled.current) {
      setIsMounted(true);
    }
  }, [auth, projects]);

  const handlePageDown = () => {
    if (currentPage == 1) {
      alert("이전 페이지가 없습니다.");
    } else {
      getProjects(currentPage - 1, searchCondition, size);
      setCurrentPage(currentPage - 1);
    }
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
      {isMounted ? (
        <section className="py-3">
          <div className="mx-auto flex w-full">
            <div className="ml-2 h-screen min-h-screen w-1/5 overflow-auto bg-white p-10 shadow-md sm:rounded-lg">
              <div className="dark:border-gray-700">
                <div className="flex flex-row items-center justify-between space-x-3">
                  <h5 className="mb-4 text-2xl font-bold dark:text-white">
                    맞춤 필터
                  </h5>
                </div>
                {auth.authLevel && auth.authLevel < 2 && (
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">공개 구분</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`isPrivate-All`}
                          checked={searchCondition.isPrivate === undefined}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              isPrivate: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`isPrivate-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`isPrivate-Open`}
                          checked={searchCondition.isPrivate === false}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              isPrivate: false,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`isPrivate-Open`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          공개 ({statics?.isPrivateStatics["false"] || 0})
                        </label>
                      </div>
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`isPrivate-Closed`}
                          checked={searchCondition.isPrivate === true}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              isPrivate: true,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`isPrivate-Closed`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          비공개 ({statics?.isPrivateStatics["true"] || 0})
                        </label>
                      </div>
                    </div>
                  </div>
                )}
                <div className="flex flex-col gap-2">
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">프로젝트 담당</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`member-All`}
                          checked={searchCondition.memberId === undefined}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              memberId: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`member-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      {projectFilter?.memberList?.map((member) => (
                        <div
                          key={member.memberId}
                          className="flex items-center"
                        >
                          <input
                            type="checkbox"
                            id={`member-${member.memberId}`}
                            checked={
                              searchCondition.memberId === member.memberId
                            }
                            onChange={(e) => {
                              const nextCondition = {
                                ...searchCondition,
                                memberId: e.target.checked
                                  ? member.memberId
                                  : undefined,
                              };
                              getProjects(currentPage, nextCondition, size);
                              setSearchCondition(nextCondition);
                            }}
                            className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                          />
                          <label
                            htmlFor={`member-${member.memberId}`}
                            className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                          >
                            {member.name} (
                            {statics?.projectMemberStatics[member.memberId] ||
                              0}
                            )
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">타겟 연도</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`year-All`}
                          value={undefined}
                          checked={searchCondition.targetYear === undefined}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              targetYear: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`year-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      {projectFilter?.targetYears.map((year, index) => (
                        <div key={index} className="flex items-center">
                          <input
                            type="checkbox"
                            id={`year-${year}`}
                            value={year}
                            checked={searchCondition.targetYear === year}
                            onChange={(e) => {
                              const nextCondition = {
                                ...searchCondition,
                                targetYear: e.target.checked
                                  ? Number(e.target.value)
                                  : undefined,
                              };
                              getProjects(currentPage, nextCondition, size);
                              setSearchCondition(nextCondition);
                            }}
                            className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                          />
                          <label
                            htmlFor={`year-${year}`}
                            className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                          >
                            {year} ({statics?.targetYearStatics[year] || 0})
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">사업부</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`department-All`}
                          value={undefined}
                          checked={searchCondition.departmentId === undefined}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              departmentId: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`department-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      {projectFilter?.departmentAdminSummaryDtos &&
                        projectFilter.departmentAdminSummaryDtos.map(
                          (d) =>
                            d && (
                              <div
                                key={d.departmentId}
                                className="flex items-center"
                              >
                                <input
                                  type="checkbox"
                                  id={`department-${d.departmentId}`}
                                  value={d.departmentId}
                                  checked={
                                    searchCondition.departmentId ===
                                    d.departmentId
                                  }
                                  onChange={(e) => {
                                    const nextCondition = {
                                      ...searchCondition,
                                      departmentId: e.target.checked
                                        ? Number(e.target.value)
                                        : undefined,
                                    };
                                    getProjects(
                                      currentPage,
                                      nextCondition,
                                      size,
                                    );
                                    setSearchCondition(nextCondition);
                                  }}
                                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                                />
                                <label
                                  htmlFor={`department-${d.departmentId}`}
                                  className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                                >
                                  {d.name} (
                                  {statics?.targetDepartmentStatics[
                                    d.departmentId
                                  ] || 0}
                                  )
                                </label>
                              </div>
                            ),
                        )}
                    </div>
                  </div>
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">타겟 직급</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`jobrank-all`}
                          checked={
                            searchCondition.targetJobRankId === undefined
                          }
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              targetJobRankId: e.target.checked
                                ? undefined
                                : undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`jobrank-all`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      {projectFilter?.jobRankAdminSummaryDtos?.map((jr) => (
                        <div key={jr.id} className="flex items-center">
                          <input
                            type="checkbox"
                            id={`jobrank-${jr.id}`}
                            checked={searchCondition.targetJobRankId === jr.id}
                            onChange={(e) => {
                              const nextCondition = {
                                ...searchCondition,
                                targetJobRankId: e.target.checked
                                  ? jr.id
                                  : undefined,
                              };
                              getProjects(currentPage, nextCondition, size);
                              setSearchCondition(nextCondition);
                            }}
                            className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                          />
                          <label
                            htmlFor={`jobrank-${jr.id}`}
                            className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                          >
                            {jr.description} (
                            {statics?.targetJobRanksStatics[jr.id] || 0})
                          </label>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">프로젝트</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`projectType-All`}
                          checked={
                            searchCondition.projectType?.toString() ===
                            undefined
                          }
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              projectType: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`projectType-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>

                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`projectType-indept`}
                          checked={
                            searchCondition.projectType?.toString() ===
                            "IN_DEPT"
                          }
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              projectType: e.target.checked
                                ? "IN_DEPT"
                                : undefined,
                            };

                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`projectType-indept`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          사업부 ({statics?.projectTypeStatics["IN_DEPT"] || 0})
                        </label>
                      </div>
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`projectType-allcompany`}
                          checked={
                            searchCondition.projectType?.toString() ===
                            "ALL_COMPANY"
                          }
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              projectType: e.target.checked
                                ? "ALL_COMPANY"
                                : undefined,
                            };

                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`projectType-allcompany`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전사 (
                          {statics?.projectTypeStatics["ALL_COMPANY"] || 0})
                        </label>
                      </div>
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`projectType-both`}
                          checked={
                            searchCondition.projectType?.toString() === "BOTH"
                          }
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              projectType: e.target.checked
                                ? "BOTH"
                                : undefined,
                            };

                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`projectType-both`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전사/사업부 (
                          {statics?.projectTypeStatics["BOTH"] || 0})
                        </label>
                      </div>
                    </div>
                  </div>
                  <div className="p-1">
                    <h5 className="mb-2 text-lg font-semibold">타겟 국가</h5>
                    <div className="flex flex-col space-y-2">
                      <div className="flex items-center">
                        <input
                          type="checkbox"
                          id={`targetCountry-All`}
                          checked={searchCondition.targetCountry === undefined}
                          onChange={(e) => {
                            const nextCondition = {
                              ...searchCondition,
                              targetCountry: undefined,
                            };
                            getProjects(currentPage, nextCondition, size);
                            setSearchCondition(nextCondition);
                          }}
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                        />
                        <label
                          htmlFor={`targetCountry-All`}
                          className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                        >
                          전체 보기 ({maxCount})
                        </label>
                      </div>
                      {Object.entries(projectFilter?.targetCountries || {}).map(
                        ([key, value]) => (
                          <div key={value} className="flex items-center">
                            <input
                              type="checkbox"
                              id={`targetCountry-${value}`}
                              checked={searchCondition.targetCountry === value}
                              onChange={(e) => {
                                const nextCondition = {
                                  ...searchCondition,
                                  targetCountry: e.target.checked
                                    ? value
                                    : undefined,
                                };
                                getProjects(currentPage, nextCondition, size);
                                setSearchCondition(nextCondition);
                              }}
                              className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:ring-offset-gray-800 dark:focus:ring-blue-600"
                            />
                            <label
                              htmlFor={`targetCountry-${value}`}
                              className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                            >
                              {value === "" ? "미정" : value} (
                              {statics?.targetCountryStatics[value] || 0})
                            </label>
                          </div>
                        ),
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="ml-2 min-h-screen w-4/5 overflow-auto bg-white p-10  shadow-md sm:rounded-lg">
              <div className="mx-4 border-b dark:border-gray-700">
                <div className="flex flex-row items-center justify-between space-x-3">
                  <h5 className="mb-4 text-3xl font-bold dark:text-white">
                    전체 프로젝트
                  </h5>
                  {/* <h5 className="mb-4 text-xl font-bold dark:text-white">
                  전체 프로젝트
                  <span className="mx-1">{maxCount}개</span>
                </h5> */}
                  <button
                    className="relative right-0 flex items-center gap-2 rounded-md bg-primary p-2 px-4 text-sm text-white"
                    onClick={() => handleOpenProjectAddModal()}
                  >
                    <HiPlus className="inline-block" />
                    프로젝트 추가하기
                  </button>
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
                          placeholder="검색어를 입력하세요"
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
                      <label htmlFor={"order"} className="mr-2 text-xs">
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
                      {/* <Pagination
                currentPage={currentPage}
                nextLabel=""
                onPageChange={(page) => {
                  getProjects(page, searchCondition);
                  setCurrentPage(page);
                }}
                previousLabel=""
                showIcons
                totalPages={maxCount}
                
              /> */}
                      <HiChevronRight
                        className="cursor-pointer text-2xl"
                        onClick={handlePageUp}
                      />
                    </nav>
                  </div>
                </div>
              </div>

              <div className=" h-[500px] overflow-x-auto overflow-y-scroll">
                <table className=" w-full border-collapse  text-left text-sm dark:text-gray-400 rtl:text-right">
                  <thead className="bg-gray-100 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                    <th scope="col" className="whitespace-nowrap px-2 py-3">
                      <></>
                    </th>
                    <th scope="col" className="px-2 py-3">
                      명칭
                    </th>
                    <th scope="col" className="px-2 py-3">
                      연도
                    </th>
                    <th scope="col" className="px-2 py-3">
                      사업부명
                    </th>
                    <th scope="col" className=" px-2 py-3">
                      타겟직급
                    </th>
                    <th scope="col" className=" px-2 py-3">
                      프로젝트 구분
                    </th>
                    <th scope="col" className=" px-2 py-3">
                      인원
                    </th>
                    <th scope="col" className=" px-2 py-3">
                      담당자
                    </th>
                    <th scope="col" className=" px-2 py-3">
                      생성일
                    </th>
                    <th></th>
                  </thead>
                  {/* row start */}
                  <tbody className="overflow-y-scroll ">
                    {projects &&
                      projects.length > 0 &&
                      projects?.map((p) => (
                        <ProjectListRow
                          key={p.id}
                          project={p}
                          authLevel={auth.authLevel}
                          onDeleteParent={onDeleteParent}
                          openModal={handleOpenProjectShowModal}
                          closeModal={handleCloseProjectShowModal}
                        />
                      ))}
                  </tbody>
                  {/* row end */}
                </table>
                {projects?.length === 0 && (
                  <div className="my-20 w-full text-center">
                    프로젝트가 없습니다.
                  </div>
                )}
              </div>
            </div>
          </div>
          <ProjectModal
            showModal={showProjectAddModal}
            closeModal={handleCloseProjectAddModal}
            size={"3xl"}
          />
          {modalProjectId != 0 && (
            <ProjectUpdateModal
              projectId={modalProjectId}
              showModal={showProjectModal}
              closeModal={handleCloseProjectShowModal}
              // resetProject={getProjects}
              size={"3xl"}
            />
          )}
        </section>
      ) : (
        <div>
          <Spinner size={60} color="#ff6347" />
        </div>
      )}
    </>
  );
}
