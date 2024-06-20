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

import { useState, useEffect } from "react";
import { HiPlus, HiSearch } from "react-icons/hi";
import { useRouter } from "next/navigation";
import ProjectListRow from "../../../components/project/ProjectListRow";
import { ProjectAdminModal } from "../../../components/admin/ProjectAdminModal";
import { ProjectModal } from "../../../components/project/ProjectModal";
import {
  ProjectAdminList,
  ProjectAdminSummary,
  ProjectSearchCondition,
} from "@/types/admin/Project";
import { DepartmentAdminSummary } from "@/types/admin/Member";
import { useLocalAxios } from "@/app/api/axios";

export default function ProjectTable() {
  const [currentPage, setCurrentPage] = useState(1);
  const [maxCount, setMaxCount] = useState(2);
  const [projects, setProjects] = useState<ProjectAdminSummary[]>();
  const [targetYears, setTargetYears] = useState<number[]>([]);
  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>([]);
  const localAxios = useLocalAxios();
  const [keyword, setKeyword] = useState<string>();
  const [searchCondition, setSearchCondition] =
    useState<ProjectSearchCondition>({
      countryName: undefined,
      departmentId: undefined,
      targetYear: undefined,
      isPoolsizeAsc: undefined,
      keyword: "",
    });
  const [showProjectModal, setShowProjectModal] = useState(false);
  const [showProjectAddModal, setShowProjectAddModal] = useState(false);

  const [modalProjectId, setModalProjectId] = useState(0);

  const handleOpenProjectShowModal = (id: number) => {
    setShowProjectModal(true);
    setModalProjectId(id);
  };

  const handleCloseProjectShowModal = () => {
    setShowProjectModal(false);
    setModalProjectId(0);
  };

  const handleOpenProjectAddModal = () => {
    setShowProjectAddModal(true);
  };

  const handleCloseProjectAddModal = () => {
    setShowProjectAddModal(false);
  };

  const getProjectCount = async () => {
    const response = await localAxios.get<number>(`/admin/project/count`);
    setMaxCount(response.data);
  };
  const getProjects = async () => {
    setProjects([]);
    const params = {
      page: currentPage - 1,
      countryName: searchCondition.countryName,
      departmentId: searchCondition.departmentId,
      targetYear: searchCondition.targetYear,
      isPoolsizeAsc: searchCondition.isPoolsizeAsc,
      keyword: searchCondition.keyword,
    };
    const response = await localAxios.get<ProjectAdminList>(
      `/admin/project?page=${currentPage - 1}`,
      { params },
    );
    setProjects(response.data.projectAdminSummaryDtos);
    setTargetYears(response.data.targetYears);
    setDepartments(response.data.departmentAdminSummaryDtos);
  };
  useEffect(() => {
    getProjectCount();
    getProjects();
  }, [currentPage, searchCondition]);

  const onDeleteParent = () => {
    getProjects();
  };

  return (
    <>
      <section className="py-3">
        <div className="mx-auto">
          <div className="relative mx-10 min-h-screen w-fit min-w-[70vw] overflow-auto bg-white p-10 shadow-md sm:rounded-lg">
            <div className="mx-4 border-b dark:border-gray-700">
              <div className="flex flex-row items-center justify-between space-x-3">
                <h5 className="mb-4 text-xl font-bold dark:text-white">
                  전체 프로젝트
                  <span className="mx-1">{maxCount}개</span>
                </h5>
                <button
                  className="relative right-0 flex items-center gap-2 rounded-md bg-primary p-2 px-4 text-sm text-white"
                  onClick={() => handleOpenProjectAddModal()}
                >
                  <HiPlus className="inline-block" />
                  프로젝트 추가하기
                </button>
              </div>
              <div className="flex flex-row items-center justify-between py-3 md:space-x-4">
                <div className="flex w-2/3 flex-row items-center">
                  <form className="w-full flex-1 items-center md:mr-4 md:max-w-sm">
                    <div className="relative w-80">
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
                      />
                      <Button
                        type="submit"
                        className="absolute inset-y-0 right-0 h-[42px] rounded-l-none rounded-r-lg bg-primary"
                        onClick={() =>
                          setSearchCondition({
                            ...searchCondition,
                            keyword: keyword ? keyword : "",
                          })
                        }
                      >
                        검색
                      </Button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
            <div className="mx-4 flex flex-wrap pb-3">
              <div className="flex flex-wrap">
                <div className="mr-4 mt-3 flex items-center">
                  <Radio
                    id="pool"
                    name="isPoolSizeAsc"
                    checked={searchCondition.isPoolsizeAsc === false}
                    onChange={() =>
                      setSearchCondition({
                        ...searchCondition,
                        isPoolsizeAsc: false,
                      })
                    }
                  />
                  <Label
                    htmlFor="pool"
                    className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                  >
                    Pool 많은순
                  </Label>
                </div>
                <div className="mr-4 mt-3 flex items-center">
                  <Radio
                    id="pool-reverse"
                    name="isPoolsizeAsc"
                    checked={searchCondition.isPoolsizeAsc === true}
                    onChange={() =>
                      setSearchCondition({
                        ...searchCondition,
                        isPoolsizeAsc: true,
                      })
                    }
                  />
                  <Label
                    htmlFor="pool-reverse"
                    className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                  >
                    Pool 적은순
                  </Label>
                </div>
                <div className="mr-2 mt-3 flex items-center">
                  <Select
                    name="targetYear"
                    value={searchCondition.targetYear || ""}
                    onChange={(e) =>
                      setSearchCondition({
                        ...searchCondition,
                        targetYear: e.target.value
                          ? Number(e.target.value)
                          : undefined,
                      })
                    }
                  >
                    <option className="text-xs" value={""}>
                      타겟연도
                    </option>
                    {targetYears.map((t, index) => (
                      <option key={index} className="text-xs" value={t}>
                        {t}
                      </option>
                    ))}
                  </Select>
                </div>
                <div className="mr-4 mt-3 flex items-center">
                  <Select
                    name="departmentId"
                    value={searchCondition.departmentId || ""}
                    onChange={(e) =>
                      setSearchCondition({
                        ...searchCondition,
                        departmentId: e.target.value
                          ? Number(e.target.value)
                          : undefined,
                      })
                    }
                  >
                    <option className="text-xs" value={""}>
                      타겟부서
                    </option>
                    {departments.map((d) => (
                      <option
                        key={d.departmentId}
                        className="text-xs"
                        value={d.departmentId}
                      >
                        {d.name}
                      </option>
                    ))}
                  </Select>
                </div>
              </div>
            </div>
            <div className="overflow-x-auto">
              <Table
                theme={{
                  root: {
                    wrapper: "static",
                  },
                }}
                className="w-full text-center text-sm text-gray-500 dark:text-gray-400"
              >
                <Table.Head className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    <></>
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    프로젝트명
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    인원
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    분류
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    보고자
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    보고일
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="px-4 py-3 text-slate-50"
                  />
                </Table.Head>
                {/* row start */}
                <Table.Body className="h-[50px]">
                  {projects?.map((p) => (
                    <ProjectListRow
                      key={p.id}
                      project={p}
                      onDeleteParent={onDeleteParent}
                      openModal={handleOpenProjectShowModal}
                      closeModal={handleCloseProjectShowModal}
                    />
                  ))}
                </Table.Body>
                {/* row end */}
              </Table>
            </div>
            <nav
              className="flex flex-col items-start justify-between p-4 md:flex-row md:items-center"
              aria-label="Table navigation"
            >
              <span className="text-sm font-normal text-gray-500 dark:text-gray-400">
                <span className="font-semibold text-gray-900 dark:text-white">
                  {(currentPage - 1) * 10 + 1}
                </span>
                &nbsp;-&nbsp;
                <span className="font-semibold text-gray-900 dark:text-white">
                  {maxCount > currentPage * 10 ? currentPage * 10 : maxCount}
                </span>
              </span>
              <Pagination
                currentPage={currentPage}
                nextLabel=""
                onPageChange={(page) => setCurrentPage(page)}
                previousLabel=""
                showIcons
                totalPages={Math.ceil(maxCount / 10)}
              />
            </nav>
          </div>
        </div>
        <ProjectModal
          showModal={showProjectAddModal}
          closeModal={handleCloseProjectAddModal}
          size={"3xl"}
        />

        <ProjectAdminModal
          projectId={modalProjectId}
          showModal={showProjectModal}
          closeModal={handleCloseProjectShowModal}
          resetProject={getProjects}
          size={"3xl"}
          isUpdate={true}
        />
      </section>
    </>
  );
}
