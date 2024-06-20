import { useLocalAxios } from "@/app/api/axios";
import { DepartmentAdminSummary } from "@/types/admin/Member";
import {
  ProjectUpdate,
  ProjectMemberSummary,
  ProjectCreate,
  ProjectFull,
} from "@/types/admin/Project";

import {
  Button,
  Checkbox,
  Modal,
  ToggleSwitch,
  Label,
  Textarea,
  Select,
} from "flowbite-react";
import { FormEvent, useEffect, useRef, useState } from "react";
import { AutoCompleteInput } from "../common/AutoCompleteInput";
import { HiLockClosed, HiLockOpen, HiX } from "react-icons/hi";

interface ProjectArray {
  projectMembers: number[];
  targetJobRanks: number[];
}

interface AutoCompleteResponse {
  id: number;
  data: string;
}

export function ProjectUpdateModal({
  showModal,
  closeModal,
  size,
  projectId,
}: {
  showModal: boolean;
  closeModal: () => void;
  size: string;
  projectId: number;
}) {
  const [isMounted, setIsMounted] = useState<boolean>(false);
  const isCalled = useRef<boolean>(false);
  const localAxios = useLocalAxios();

  const [isAll, setIsAll] = useState<string>("false");
  const [isDept, setisDept] = useState<string>("true");

  // TODO : 생성자 id = responsibleMemberID, projectProfiles에 생성자 id 추가
  const [formData, setFormData] = useState<ProjectCreate>({
    title: "",
    targetMemberCount: 10,
    targetYear: new Date().getFullYear(),
    isPrivate: false,
    projectType: "",
    targetCountry: "",
    responsibleMemberId: 1,
    projectMembers: [],
    projectProfiles: [],
    targetJobRanks: [],
    targetDepartmentId: 0,
    description: "",
  });

  const [targetJobRanks, setTargetJobRanks] = useState<AutoCompleteResponse[]>(
    [],
  );

  const [countries, setCountries] = useState<AutoCompleteResponse[]>([]);
  const [depts, setDepts] = useState<AutoCompleteResponse[]>([]);

  const handleRemoveFilter = (id: number, index: number) => {
    const nextTargetJobRanks = targetJobRanks.filter((item) => item.id != id);
    const nextFormDataTargetJobRanks = nextTargetJobRanks.map(
      (item) => item.id,
    );
    setFormData({
      ...formData,
      targetJobRanks: nextFormDataTargetJobRanks,
    });
    setTargetJobRanks(nextTargetJobRanks);
  };

  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>([]);

  const handleInputChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
    >,
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAddFilterAuto = (item: AutoCompleteResponse) => {
    const nextTargetJobRanks = [...targetJobRanks, item];
    const nextFormDataTargetJobRanks = nextTargetJobRanks.map(
      (item) => item.id,
    );
    setFormData({
      ...formData,
      targetJobRanks: nextFormDataTargetJobRanks,
    });
    setTargetJobRanks(nextTargetJobRanks);
  };

  useEffect(() => {
    localAxios
      .get("/keyword", {
        params: {
          type: "COUNTRY",
          query: "",
        },
      })
      .then((response) => {
        setCountries(response.data);
      })
      .catch((err) => {
        console.error(err);
      });
    localAxios
      .get("/keyword", {
        params: {
          type: "DEPARTMENT",
          query: "",
        },
      })
      .then((response) => {
        setDepts(response.data);
      })
      .catch((err) => {
        console.error(err);
      });
    baseSetting();
  }, []);

  useEffect(() => {
    if (projectId) {
      baseSetting();
    }
  }, [projectId]);

  const baseSetting = async () => {
    const departmentResponse =
      await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);
    setDepartments(departmentResponse.data);
    const response = await localAxios.get<ProjectFull>(`/project/${projectId}`);

    if (response.data) {
      setFormData({
        title: response.data.title != undefined ? response.data.title : "",
        description: response.data.description ? response.data.description : "",
        targetMemberCount:
          response.data.targetMemberCount != undefined
            ? response.data.targetMemberCount
            : 0,
        targetYear:
          response.data.targetYear != undefined ? response.data.targetYear : 0,
        isPrivate:
          response.data.isPrivate != undefined
            ? response.data.isPrivate
            : false,
        projectType:
          response.data.projectType != undefined
            ? response.data.projectType.toString()
            : "",
        targetCountry:
          response.data.targetCountry != undefined
            ? response.data.targetCountry
            : "",
        responsibleMemberId:
          response.data.responsibleMemberId != undefined
            ? response.data.responsibleMemberId
            : 0,
        projectMembers: [],
        projectProfiles: [],
        targetJobRanks: response.data.targetJobRanksIds
          ? response.data.targetJobRanksIds
          : [],
        targetDepartmentId: response.data.projectDepartment?.departmentId,
      });

      if (response.data.projectType) {
        if (response.data.projectType.toString() === "BOTH") {
          setIsAll("true");
          setisDept("true");
        }
        if (response.data.projectType.toString() === "ALL_COMPANY") {
          setIsAll("true");
        } else if (response.data.projectType.toString() === "IN_DEPT") {
          setisDept("true");
        }
      }
      if (response.data.targetJobRanks && response.data.targetJobRanksIds) {
        const array: AutoCompleteResponse[] = response.data.targetJobRanks.map(
          (jobRank, index) => ({
            id: response.data.targetJobRanksIds[index],
            data: jobRank,
          }),
        );
        setTargetJobRanks(array);
      }
      isCalled.current = true;
    }
  };

  const putProject = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (!isAll && !isDept) {
      alert("전사 또는 사업부 프로젝트여야 합니다");
      return;
    }
    const data = {
      ...formData,
    };
    const response = await localAxios.put(`/project/${projectId}`, data);
    if (response.status === 200) {
      alert("수정되었습니다.");
      closeModal();
    } else {
      alert("잠시 후 다시 시도해주세요.");
    }
  };

  return isCalled.current ? (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>프로젝트 수정 </Modal.Header>
      <Modal.Body>
        {/* 프로젝트 제목 입력 */}
        <div className="flex w-full flex-row flex-wrap gap-4 ">
          <div className="flex w-full flex-col">
            <div className="mb-4 flex w-full items-center justify-between">
              <div className="flex w-5/6 items-center">
                <label
                  htmlFor="title"
                  className="mb-2 block w-10 font-bold text-gray-700"
                >
                  이름
                </label>
                <input
                  type="text"
                  id="title"
                  className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                  name="title"
                  value={formData.title}
                  placeholder="프로젝트명을 입력하세요."
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="flex gap-1">
                {formData.isPrivate ? <HiLockClosed /> : <HiLockOpen />}

                <Label htmlFor="isPrivate">비공개</Label>
                <Checkbox
                  id="isPrivate"
                  name="isPrivate"
                  checked={formData.isPrivate}
                  onChange={() =>
                    setFormData({ ...formData, isPrivate: !formData.isPrivate })
                  }
                />
              </div>
            </div>
            <div className="flex w-full items-center">
              <label
                htmlFor="title"
                className="mb-2 block w-10 font-bold text-gray-700"
              >
                설명
              </label>
              <Textarea
                id="description"
                className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                name="description"
                value={formData.description}
                placeholder="프로젝트 설명을 입력하세요."
                onChange={handleInputChange}
                required
              />
            </div>

            {/* 프로젝트 타겟 멤버 수 입력 */}
            <div className="mt-4 flex w-full gap-2">
              <div className="my-2 mb-4 w-1/5 py-2">
                <label
                  htmlFor="targetYear"
                  className="mb-4 block font-bold text-gray-700"
                >
                  목표년
                </label>
                <input
                  id="targetYear"
                  className="w-full rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                  name="targetYear"
                  type="text"
                  value={formData.targetYear}
                  onChange={handleInputChange}
                  required
                />
              </div>
              <div className="my-2 mb-4 w-1/5 px-2 py-2">
                <label
                  htmlFor="targetDepartmentId"
                  className="mb-4 block font-bold text-gray-700"
                >
                  사업부
                </label>
                <select
                  id="targetDepartmentId"
                  name="targetDepartmentId"
                  className="w-full rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                  value={formData.targetDepartmentId}
                  onChange={handleInputChange}
                  required
                >
                  <option value="">선택하세요</option>
                  {departments.map((department) => (
                    <option
                      key={department.departmentId}
                      value={department.departmentId}
                    >
                      {department.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mb-4 w-3/5">
                <label className="mt-4 block font-bold text-gray-700">
                  타겟 직급
                </label>
                <AutoCompleteInput
                  identifier="JOBRANK"
                  onChange={(item) => {
                    if (
                      item &&
                      !targetJobRanks.map((i) => i.id).includes(item.id)
                    ) {
                      handleAddFilterAuto(item);
                    }
                  }}
                  className="w-full"
                />
                <div className="flex min-h-12 w-full flex-wrap items-center gap-2 rounded-md border border-slate-200 p-2">
                  {targetJobRanks.map((item, index) => (
                    <span
                      key={item.id}
                      className={`rounded bg-blue-50 px-2.5 py-0.5 text-xs font-medium text-blue-500`}
                    >
                      {item.data}
                      <button
                        type="button"
                        onClick={() => handleRemoveFilter(item.id, index)}
                        className="ms-1 inline-flex items-center rounded-full p-0.5 text-xs"
                      >
                        <HiX className="size-3" />
                      </button>
                    </span>
                  ))}
                </div>
              </div>
            </div>

            {/* 프로젝트 타겟 연도 선택 */}

            {/* 프로젝트 공개 여부 선택 */}
            <div className="mb-4 flex w-full flex-wrap gap-4">
              <div className="flex items-center gap-2">
                <Label htmlFor="isAll">전사 중점 프로젝트</Label>
                <Select
                  id="isAll"
                  name="isAll"
                  value={isAll}
                  onChange={(e) => {
                    setIsAll(e.target.value);
                    setFormData({
                      ...formData,
                      projectType:
                        e.target.value == "true"
                          ? isDept == "true"
                            ? "BOTH"
                            : "ALL_COMANY"
                          : "IN_DEPT",
                    });
                  }}
                >
                  <option value={"true"}>예</option>
                  <option value={"false"}>아니오</option>
                </Select>
              </div>
              <div className="flex items-center gap-2">
                <Label htmlFor="isDept">사업부 프로젝트</Label>
                <Select
                  id="isDept"
                  name="isDept"
                  value={isDept}
                  onChange={(e) => {
                    setisDept(e.target.value);
                    setFormData({
                      ...formData,
                      projectType:
                        isAll == "true"
                          ? e.target.value == "true"
                            ? "BOTH"
                            : "ALL_COMANY"
                          : "IN_DEPT",
                    });
                  }}
                >
                  <option value={"true"}>예</option>
                  <option value={"false"}>아니오</option>
                </Select>
                <div className="flex items-center gap-2">
                  <Label htmlFor="targetCountry">목표 국가</Label>
                  <Select
                    id="targetCountry"
                    name="targetCountry"
                    // className=" rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                    value={formData.targetCountry}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">선택하세요</option>
                    {countries.map((country) => (
                      <option key={country.id} value={country.data}>
                        {country.data}
                      </option>
                    ))}
                  </Select>
                </div>
              </div>
            </div>

            {/* 프로젝트 타겟 국가 입력 */}

            {/* 프로젝트 부서 선택 */}

            {/* 프로젝트 멤버 선택 */}
            {/* <div className="mb-4 w-2/5">
              <label className="mb-2 block font-bold text-gray-700">
                프로젝트 멤버
              </label>
              <AutoCompleteInput
                identifier="MEMBER"
                onChange={(item) => {
                  if (
                    item &&
                    !arrayfilters.projectMembers.includes(item.data)
                  ) {
                    handleAddFilterAuto("projectMembers", item.data);
                  }
                }}
                className="w-full"
              />
              <div className="flex min-h-12 w-full flex-wrap items-center gap-2 rounded-md border border-slate-200 p-2">
                {arrayfilters.projectMembers.map((value, index) => (
                  <span
                    key={index}
                    className="rounded bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-500"
                  >
                    {value}
                    <button
                      type="button"
                      onClick={() =>
                        handleRemoveFilter("projectMembers", index)
                      }
                      className="ms-1 inline-flex items-center rounded-full p-0.5 text-xs"
                    >
                      <HiX className="size-3" />
                    </button>
                  </span>
                ))}
              </div>
            </div> */}
            <div className="flex w-full items-center justify-between">
              <div className="mb-4 flex w-2/5 items-center">
                <Label htmlFor="targetMemberCount" className="w-24">
                  목표 인원
                </Label>
                <input
                  type="number"
                  id="targetMemberCount"
                  className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
                  name="targetMemberCount"
                  value={formData.targetMemberCount}
                  placeholder="목표 인원수를 입력하세요."
                  onChange={handleInputChange}
                />
              </div>
              {/* 타겟 직급 선택 */}

              <Button className="h-11 bg-blue-500" onClick={putProject}>
                수정하기
              </Button>
            </div>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  ) : (
    <div className="flex h-screen w-full items-center justify-center">
      <div role="status">
        <svg
          aria-hidden="true"
          className="h-24 w-24 animate-spin fill-blue-600 text-gray-200 dark:text-gray-600"
          viewBox="0 0 100 101"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M100 50.5908직급100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
            fill="currentColor"
          />
          <path
            d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
            fill="currentFill"
          />
        </svg>
        <span className="sr-only">Loading...</span>
      </div>
    </div>
  );
}
