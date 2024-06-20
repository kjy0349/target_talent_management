"use client";

import {
  Button,
  Checkbox,
  Dropdown,
  Label,
  Pagination,
  Table,
  theme,
  Select,
  Badge,
  Toast,
} from "flowbite-react";

import { HiDotsHorizontal } from "react-icons/hi";
import { useState, useEffect } from "react";
import { twMerge } from "tailwind-merge";
import techmapProjectRegister from "./techmapProjectRegisterForm";
import TargetModal from "./TargetModal";
import DeleteModal from "./DeleteModal";
import ExcelModal from "./ExcelModal";
import ManagerModal from "./ManagerModal";
import TargetYearModal from "./TargetYearModal";
import { useLocalAxios } from "@/app/api/axios";
import {
  techmapProjects,
  techmapProjectRequest,
  targetYear,
} from "@/types/techmap/techmap-project";
import Link from "next/link";
import {
  CheckboxState,
  Department,
  PageState,
  techmapMoveData,
} from "@/types/techmap/common";
import { useAuthStore } from "@/stores/auth";

const techStatus = [
  { id: "null", name: "기술구분" },
  { id: "NEW", name: "신규" },
  { id: "EXISTING", name: "기존" },
];
const relativeLevel = [
  { id: "null", name: "당사수준" },
  { id: "OUTNUMBERED", name: "열세" },
  { id: "NORMAL", name: "동등" },
  { id: "SUPERIOR", name: "우세" },
];
const targetStatus = [
  { id: "null", name: "타겟분야" },
  { id: "true", name: "예" },
  { id: "false", name: "아니요" },
];

const viewList = [10, 15, 20];

// 기술수준 우세
export default function techmapProjectTable({
  techmapId,
}: {
  techmapId: number;
}) {
  const localAxios = useLocalAxios();
  const [openTechRegisterModal, setOpenTechRegisterModal] = useState(false);
  const [openManagerModal, setOpenManagerModal] = useState(false);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const [openTargetModal, setOpenTargetModal] = useState(false);
  const [openYearModal, setOpenYearModal] = useState(false);
  const [openUploadModal, setOpenUploadModal] = useState(false);
  const [isRegister, setIsRegister] = useState(false);
  const [currentId, setCurrentId] = useState(0);
  const [checkListId, setCheckListId] = useState<CheckboxState>({});
  const [currentTechDetail, setCurrentTechDetail] = useState("");
  const [projectList, setProjectList] = useState<techmapProjectRequest>();
  const [projectData, setProjectData] = useState<techmapProjects>();
  const [deleteId, setDeleteId] = useState({});
  const [type, setType] = useState("");
  const [departmentList, setDepartmentList] = useState<Department[]>([]);
  const [moveData, setMoveData] = useState<techmapMoveData>();
  const [checkedAll, setCheckedAll] = useState(false);
  const [pageStateData, setPageStateData] = useState<PageState>({
    pageNumber: 0,
    size: 10,
    techmapId: techmapId,
  });
  const level = ["관리자", "운영진"];
  const [projectYear, setProjectYear] = useState<targetYear>();
  const userStore = useAuthStore();

  useEffect(() => {
    localAxios
      .get("/keyword", {
        params: {
          type: "DEPARTMENT",
        },
      })
      .then((response) => {
        setDepartmentList(response.data);
      })
      .catch((err) => {
        console.error(err);
      });
  }, []);

  // 페이지 리로드
  const fetch = async () => {
    try {
      const response = await localAxios.post(
        "/techmap-project/list",
        pageStateData,
      );
      if (response.status === 200) {
        setCheckedAll(false);
        setProjectList(response.data);
        const newState: CheckboxState = {};
        response.data.techmapProjects.map((data: techmapProjects) => {
          newState[data.techmapProjectId] = false; // 모든 키의 값을 true 또는 false로 설정
        });
        setCheckListId(newState);
        setProjectYear(response.data.targetYear[0]);
      }
    } catch (error) {
      console.error("로그인 에러", error);
    }
  };
  useEffect(() => {
    if (
      !openTechRegisterModal &&
      !openDeleteModal &&
      !openUploadModal &&
      !openTargetModal &&
      !openYearModal &&
      !openManagerModal
    ) {
      fetch();
    }
  }, [
    openTechRegisterModal,
    openDeleteModal,
    openUploadModal,
    pageStateData,
    openTargetModal,
    openYearModal,
    openManagerModal,
  ]);

  const handlePage = (event: any) => {
    if (typeof event === "number") {
      const value = event;
      setPageStateData((prevState) => ({
        ...prevState,
        ["pageNumber"]: value - 1,
      }));
    } else {
      const { name, value } = event.target;
      setPageStateData((prevState) => ({
        ...prevState,
        [name]: value === "null" ? null : value,
      }));
    }
  };

  const handleDeleteNewTech = (id: number, multi: boolean) => {
    if (multi) {
      if (hasCheckedItems()) {
        setDeleteId(checkListId);
        setOpenDeleteModal(true);
      } else {
        alert("선택한 항목이 없습니다");
      }
    } else {
      const newState: CheckboxState = {};
      newState[id] = true;
      setDeleteId(newState);
      setOpenDeleteModal(true);
    }
  };

  const handleEditNewTech = (data: techmapProjects) => {
    setProjectData(data);
    setIsRegister(false);
    setOpenTechRegisterModal(true);
  };
  const handleEditManager = (data: techmapProjects) => {
    setProjectData(data);
    setOpenManagerModal(true);
  };
  const handleAddNewTech = () => {
    setIsRegister(true);
    setOpenTechRegisterModal(true);
  };

  const handleOpenCompanyModal = (id: number, tech: string) => {
    setCurrentId(id);
    setCurrentTechDetail(tech);
    setType("COMPANY");
    setOpenTargetModal(true);
  };

  const handleOpenLabModal = (id: number, tech: string) => {
    setCurrentId(id);
    setCurrentTechDetail(tech);
    setType("LAB");
    setOpenTargetModal(true);
  };

  const handelExcelDownload = async () => {
    if (hasCheckedItems()) {
      const numberList: number[] = [];
      Object.keys(checkListId).forEach((key) => {
        if (checkListId[key]) numberList.push(Number(key));
      });
      const response = await localAxios.post(
        `/techmap-project/excel-download`,
        {
          techmapProjectIds: numberList,
        },
        { responseType: "blob" },
      );

      const contentDisposition = response.headers["Content-Disposition"];
      let filename = "download.xlsx"; // 기본 파일명
      if (contentDisposition) {
        const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        const matches = filenameRegex.exec(contentDisposition);
        if (matches != null && matches[1]) {
          filename = matches[1].replace(/['"]/g, "");
        }
      }

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename);
      document.body.appendChild(link);
      link.click();
    } else {
      alert("선택한 항목이 없습니다.");
    }
  };

  const handleCheckBox = (event: any) => {
    const { name, checked } = event.target;
    setCheckListId((prevState) => ({
      ...prevState,
      [name]: checked,
    }));
  };

  const handleAllChecked = (event: any) => {
    const isChecked = event.target.checked;
    setCheckedAll(isChecked);
    setCheckListId((prevState) => {
      const newState: CheckboxState = {};
      Object.keys(prevState).forEach((key) => {
        newState[key] = isChecked; // 모든 키의 값을 true 또는 false로 설정
      });
      setCheckListId(newState);
      return newState;
    });
  };

  const handleProjectYear = (e: any) => {
    const { value } = e.target;
    setProjectYear(JSON.parse(value));
  };

  const handleProjects = async (action: string) => {
    // 선택한 항목이 있는지 확인한다.
    if (hasCheckedItems()) {
      // 체크를 한 아이템들을 리스트에 담는다.
      const numberList: number[] = [];
      Object.keys(checkListId).forEach((key) => {
        if (checkListId[key]) numberList.push(Number(key));
      });

      // 전달하려는 곳의 연도 카운트가 1이면 해당 연도로 액션을 바로 수행한다.
      if (projectYear?.count === 1) {
        await autoMovetechmap(action, numberList, projectYear?.ids[0])
      } else {
        // 모달창을 띄우며 확인
        setMoveData({
          projectId: numberList,
          techmapId: techmapId,
          type: action,
          year: projectYear?.targetYear,
        });
        setOpenYearModal(true);
      }
    } else {
      alert("선택한 항목이 없습니다.");
    }
  };

  const autoMovetechmap = async (action: string, numberList: number[], targettechmapId: number) => {
    try {
      const response = await localAxios.post(`techmap-project/move`, {
        techmapId: targettechmapId,
        moveStatus: action,
        techmapProjectId: numberList,
      });
      if (response.status === 200) {
        alert("전달 완료");
      } else {
        alert("네트워크 오류")
      }
    } catch (error: any) {
      if (error?.response.status === 400) {
        alert("중복된 기술분야가 존재합니다.")
      }
      console.error(error);
    }
  }

  const hasCheckedItems = () => {
    return Object.values(checkListId).some((value) => value);
  };

  return (
    <div className="relative bg-white p-6 shadow-md sm:rounded-lg">
      <h5 className="pb-5 text-3xl font-semibold dark:text-white">
        tech 현황
      </h5>
      <Toast className="absolute right-0 top-0 max-w-fit">
        <div className="inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-cyan-100 text-cyan-500 dark:bg-cyan-800 dark:text-cyan-200">
          <svg
            className="size-5"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z" />
          </svg>
        </div>

        <div className="ml-3 text-sm font-normal">
          <p>신규 등록 및 엑셀 업로드에서 대분류/기술분야/세부기술 값이</p>
          <p>기존의 내용과 모두 동일한 경우, 업로드가 제한됩니다.</p>
        </div>
        <Toast.Toggle />
      </Toast>

      <div className="flex flex-col-reverse items-center justify-between py-3 md:flex-row md:space-x-3">
        <div className="flex items-center justify-center space-x-3">
          <p className="text-xl font-semibold">
            총 {projectList?.totalElements}개
          </p>
          <Select
            id="techStatus"
            name="techStatus"
            onChange={handlePage}
            sizing="sm"
          >
            {techStatus.map((level, index) => (
              <option key={index} value={level.id}>
                {level.name}
              </option>
            ))}
          </Select>
          <Select
            id="techCompanyRelativeLevel"
            name="techCompanyRelativeLevel"
            onChange={handlePage}
            sizing="sm"
          >
            {relativeLevel.map((level, index) => (
              <option key={index} value={level.id}>
                {level.name}
              </option>
            ))}
          </Select>
          <Select
            id="targetStatus"
            name="targetStatus"
            onChange={handlePage}
            sizing="sm"
          >
            {targetStatus.map((level, index) => (
              <option key={index} value={level.id}>
                {level.name}
              </option>
            ))}
          </Select>

          <Select id="size" name="size" sizing="sm" onChange={handlePage}>
            {viewList.map((view, index) => (
              <option key={index} value={view}>
                {view}개씩 보기
              </option>
            ))}
          </Select>
          {/*
            <div className="flex flex-row items-center justify-center space-x-2">
            <Label htmlFor="targetYear">발행연도</Label>
            <Select
              id="targetYear"
              name="targetYear"
              onChange={handlePage}
              sizing="sm"
            >
              <option key={-1} value={"null"}>
                전체
              </option>
              {projectList?.targetYear?.map((year, index) => (
                <option key={index} value={year.targetYear}>
                  {year.targetYear}
                </option>
              ))}
            </Select>
          </div> */}
          {level.includes(userStore.authority ?? "") ? (
            <div className="flex flex-row items-center justify-center space-x-2">
              <Label htmlFor="departmentId">사업부명</Label>
              <Select
                id="departmentId"
                name="departmentId"
                onChange={handlePage}
                sizing="sm"
              >
                <option key={-1} value={"null"}>
                  전체
                </option>
                {departmentList.map((department) => (
                  <option key={department.id} value={department.id}>
                    {department.data}
                  </option>
                ))}
              </Select>
            </div>
          ) : null}
        </div>

        <div className="flex space-x-2">
          <Button size="sm" onClick={handelExcelDownload}>
            엑셀 다운
          </Button>
          <input
            id="input-upload"
            type="file"
            className="hidden"
            accept=".xlsx"
          />
          <Button size="sm" onClick={() => setOpenUploadModal(true)}>
            엑셀양식 일괄 업로드
          </Button>
          <Button size="sm" onClick={handleAddNewTech}>
            신규
          </Button>
          <Button size="sm" onClick={() => handleDeleteNewTech(0, true)}>
            삭제
          </Button>
        </div>
      </div>
      {/* </div> */}

      <div className="overflow-x-auto">
        <Table
          theme={{
            root: {
              wrapper: "static",
            },
          }}
          className="min-w-full table-fixed text-center text-sm text-gray-500 dark:text-gray-400"
        >
          <Table.Head className="bg-gray-50 text-center text-base font-bold uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <Table.HeadCell scope="col" className="w-[30px] p-0">
              <div className="flex items-center justify-center">
                <Checkbox
                  id="checkbox-all"
                  name="checkbox-all"
                  checked={checkedAll}
                  onChange={handleAllChecked}
                />
                <Label htmlFor="checkbox-all" className="sr-only">
                  Check all
                </Label>
              </div>
            </Table.HeadCell>
            {/* column받아오기 */}

            <Table.HeadCell scope="col" className="w-[60px] p-2">
              대분류
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[80px] p-2">
              기술 분야
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[80px] p-2">
              세부기술
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[350px] p-2">
              기술개요 및 필요 역량
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[50px] p-2">
              기술
              <br />
              구분
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[60px] p-2">
              당사
              <br />
              수준
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[300px] p-2">
              판단 근거
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[50px] p-2">
              타겟
              <br />
              분야
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[50px] p-2">
              확보
              <br />
              목표
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[50px] p-2">
              인재
              <br />
              Pool
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[60px] p-2">
              techLab
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[60px] p-2">
              techCompany
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[70px] p-2">
              담당자
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="w-[60px]"></Table.HeadCell>
          </Table.Head>
          <Table.Body>
            {projectList?.techmapProjects.map((data, index) => (
              <Table.Row
                key={index}
                className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
              >
                <Table.Cell className="whitespace-nowrap p-0">
                  <div className="flex items-center justify-center">
                    <Checkbox
                      id="checkbox-table-search-1"
                      name={String(data.techmapProjectId)}
                      checked={checkListId[data.techmapProjectId] ?? false}
                      onChange={handleCheckBox}
                    />
                  </div>
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 text-xs font-medium text-gray-900 dark:text-white">
                  {data.mainCategory}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 text-xs font-medium text-gray-900 dark:text-white">
                  {data.subCategory}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 text-xs font-medium text-gray-900 dark:text-white">
                  {data.techDetail}
                </Table.Cell>
                <Table.Cell className="whitespace-break-spaces p-2 text-left text-xs font-medium text-gray-900 dark:text-white">
                  {data.description}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                  {data.techStatus === "NEW" ? <p>신규</p> : null}
                  {data.techStatus === "EXISTING" ? <p>기존</p> : null}
                </Table.Cell>
                <Table.Cell className="whitespace-nowrap p-2  text-gray-900 dark:text-white">
                  {data.techCompanyRelativeLevel === "SUPERIOR" ? (
                    <Badge
                      color={"green"}
                      className="inline-block items-center"
                    >
                      우세
                    </Badge>
                  ) : data.techCompanyRelativeLevel === "NORMAL" ? (
                    <Badge
                      color={"purple"}
                      className="inline-block items-center"
                    >
                      동등
                    </Badge>
                  ) : (
                    <Badge color={"red"} className="inline-block items-center">
                      열세
                    </Badge>
                  )}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 text-left text-xs font-medium text-gray-900 dark:text-white">
                  {data.relativeLevelReason}
                </Table.Cell>
                <Table.Cell className="justify-center whitespace-pre p-2 text-gray-900 dark:text-white">
                  {data.targetStatus === true ? (
                    <Badge
                      color={"green"}
                      className="inline-block items-center"
                    >
                      Y
                    </Badge>
                  ) : (
                    <Badge color={"red"} className="inline-block items-center">
                      N
                    </Badge>
                  )}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-2 text-xs font-medium text-gray-900 dark:text-white">
                  {data.targetStatus === true ? data.targetMemberCount : "-"}
                </Table.Cell>
                <Table.Cell className=" whitespace-normal p-2 dark:text-white">
                  {data.targetStatus === true ? (
                    <Link
                      href={
                        "/techmap/" +
                        techmapId +
                        "/project/" +
                        data.techmapProjectId
                      }
                    >
                      <p className="inline-block items-center text-xs font-bold  text-blue-700 underline hover:cursor-pointer">
                        {data.profileSize}
                      </p>
                    </Link>
                  ) : (
                    "-"
                  )}
                </Table.Cell>
                {/* techLab */}
                <Table.Cell className=" whitespace-normal p-2 dark:text-white">
                  {data.targetStatus === true ? (
                    <p
                      onClick={() =>
                        handleOpenLabModal(
                          data.techmapProjectId,
                          data.techDetail,
                        )
                      }
                      className="inline-block items-center text-xs font-bold text-blue-700 underline hover:cursor-pointer"
                    >
                      {data.techLabSize}
                    </p>
                  ) : (
                    "-"
                  )}
                </Table.Cell>
                {/* techCompany */}
                <Table.Cell className="whitespace-normal p-2 text-xs dark:text-white">
                  {data.targetStatus === true ? (
                    <p
                      onClick={() =>
                        handleOpenCompanyModal(
                          data.techmapProjectId,
                          data.techDetail,
                        )
                      }
                      className="inline-block items-center font-bold text-blue-700 underline hover:cursor-pointer"
                    >
                      {data.techCompanySize}
                    </p>
                  ) : (
                    "-"
                  )}
                </Table.Cell>
                <Table.Cell className="whitespace-pre-wrap p-2 text-xs font-medium text-gray-900 dark:text-white">
                  {data.managerName}
                </Table.Cell>
                <Table.Cell className="whitespace-normal p-0 text-center text-xs">
                  <div className="flex justify-center">
                    <Dropdown
                      inline
                      label={<HiDotsHorizontal className="size-5" />}
                      theme={{
                        arrowIcon: "hidden",
                        floating: {
                          base: twMerge(theme.dropdown.floating.base, "w-40"),
                        },
                      }}
                    >
                      <Dropdown.Item onClick={() => handleEditNewTech(data)}>
                        설정 변경
                      </Dropdown.Item>
                      <Dropdown.Item onClick={() => handleEditManager(data)}>
                        담당자 변경
                      </Dropdown.Item>
                      <Dropdown.Item
                        className="text-red-500"
                        onClick={() =>
                          handleDeleteNewTech(data.techmapProjectId, false)
                        }
                      >
                        삭제
                      </Dropdown.Item>
                    </Dropdown>
                  </div>
                </Table.Cell>
              </Table.Row>
            ))}
            <techmapProjectRegister
              isModalOpen={openTechRegisterModal}
              onClose={() => setOpenTechRegisterModal(false)}
              mode={isRegister}
              data={projectData}
              techmapId={techmapId}
            />
            <DeleteModal
              isModalOpen={openDeleteModal}
              onClose={() => setOpenDeleteModal(false)}
              id={deleteId}
              page={"project"}
            />
            <TargetModal
              isModalOpen={openTargetModal}
              onClose={() => setOpenTargetModal(false)}
              id={currentId}
              techDetail={currentTechDetail}
              type={type}
            />
            <ExcelModal
              isModalOpen={openUploadModal}
              onClose={() => setOpenUploadModal(false)}
              techmapId={techmapId}
              checkListId={checkListId ?? []}
            />
            <TargetYearModal
              isModalOpen={openYearModal}
              onClose={() => setOpenYearModal(false)}
              data={moveData}
            />
            <ManagerModal
              isModalOpen={openManagerModal}
              onClose={() => setOpenManagerModal(false)}
              id={projectData?.techmapProjectId ?? -1}
            />
          </Table.Body>
        </Table>
      </div>
      <nav
        className="flex flex-col items-start justify-center space-y-3 pb-2 pt-4 md:flex-row md:items-center md:space-y-0"
        aria-label="Table navigation"
      >
        <Pagination
          currentPage={pageStateData.pageNumber + 1}
          nextLabel=""
          onPageChange={handlePage}
          previousLabel=""
          showIcons
          totalPages={projectList?.totalPages ?? 2}
          theme={{
            pages: {
              base: twMerge(theme.pagination.pages.base, "mt-0"),
              next: {
                base: twMerge(theme.pagination.pages.next.base, "w-10 p-1.5"),
                icon: "size-6",
              },
              previous: {
                base: twMerge(
                  theme.pagination.pages.previous.base,
                  "w-10 p-1.5",
                ),
                icon: "size-6",
              },
              selector: {
                base: twMerge(
                  theme.pagination.pages.selector.base,
                  "w-9 py-2 text-sm",
                ),
              },
            },
          }}
        />
      </nav>
      <div className="flex flex-row items-center justify-end space-x-2">
        <Label htmlFor="project-year">선택항목을</Label>
        <Select
          id="project-year"
          name="project-year"
          sizing="sm"
          onChange={handleProjectYear}
        >
          {projectList?.targetYear?.map((year, index) => (
            <option
              key={index}
              value={JSON.stringify({
                targetYear: year.targetYear,
                count: year.count,
                ids: year.ids
              })}
            >
              {year.targetYear}
            </option>
          ))}
        </Select>
        <span>으로</span>
        <Button size="sm" onClick={() => handleProjects("DUPLICATE")}>
          복사
        </Button>
        <Button size="sm" onClick={() => handleProjects("MOVE")}>
          이동
        </Button>
      </div>
    </div>
  );
}
