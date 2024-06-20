import { useLocalAxios } from "@/app/api/axios";
import { DepartmentAdminSummary } from "@/types/admin/Member";
import {
  ProjectUpdate,
  ProjectMemberSummary,
  ProjectCreate,
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
import { useEffect, useRef, useState } from "react";
import { AutoCompleteInput } from "../common/AutoCompleteInput";
import { HiLockClosed, HiLockOpen, HiX } from "react-icons/hi";
import { useAuthStore } from "@/stores/auth";
import { AxiosError } from "axios";

interface ProjectArray {
  projectMembers: number[];
  targetJobRanks: number[];
}

interface AutoCompleteResponse {
  id: number;
  data: string;
}

export function ProjectModal({
  showModal,
  closeModal,
  size,
}: {
  showModal: boolean;
  closeModal: () => void;
  size: string;
}) {
  const localAxios = useLocalAxios();

  const [isAll, setIsAll] = useState<string>("false");
  const [isDept, setisDept] = useState<string>("true");

  // TODO : 생성자 id = responsibleMemberID, projectProfiles에 생성자 id 추가
  const [formData, setFormData] = useState<ProjectCreate>({
    title: "",
    targetMemberCount: 10,
    targetYear: new Date().getFullYear(),
    isPrivate: false,
    projectType: "IN_DEPT",
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

  const auth = useAuthStore();

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

  const baseSetting = async () => {
    const departmentResponse =
      await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);
    let nextDepartment: DepartmentAdminSummary[] = [];
    if (auth.authLevel) {
      nextDepartment = departmentResponse.data;
      if (auth.authLevel > 2) {
        nextDepartment = departmentResponse.data.filter(
          (d) => d.name == auth.departmentName,
        );
      }
    }
    setDepartments(nextDepartment);
  };

  const addProject = async () => {
    if (!isAll && !isDept) {
      alert("전사 또는 사업부 프로젝트여야 합니다");
      return;
    }
    const data = {
      ...formData,
    };

    const response = await localAxios.post(`/project`, data);
    if (response.status === 200) {
      alert("등록되었습니다.");
      closeModal();
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>프로젝트 생성</Modal.Header>
      <Modal.Body>
        {/* 프로젝트 제목 입력 */}
        <div className="flex w-full flex-row flex-wrap gap-4 text-sm ">
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
                  목표연도
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
                  className="w-[125px] rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
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
                            : "ALL_COMPANY"
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
                            : "ALL_COMPANY"
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

              <Button className="h-11 bg-blue-500" onClick={addProject}>
                등록하기
              </Button>
            </div>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
}
