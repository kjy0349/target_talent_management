"use client";
import { useLocalAxios } from "@/app/api/axios";
import {
  DepartmentAdminSummary,
  JobRankAdminSummary,
  MemberAdminSummary,
} from "@/types/admin/Member";
import {
  ProjectAdminUpdate,
  ProjectAdminDepartmentSummary,
  ProjectAdminMemberSummary,
} from "@/types/admin/Project";
import { Button, Modal } from "flowbite-react";
import { useEffect, useState } from "react";

export function ProjectAdminModal({
  showModal,
  closeModal,
  size,
  projectId,
  isUpdate,
  resetProject,
}: {
  showModal: boolean;
  closeModal: () => void;
  size: string;
  projectId: number;
  isUpdate: boolean;
  resetProject: () => void;
}) {
  const localAxios = useLocalAxios();
  const baseUrl = "http://localhost:8080";

  const [formData, setFormData] = useState<ProjectAdminUpdate>({
    title: "",
    targetMemberCount: 0,
    targetYear: 0,
    isPrivate: false,
    projectType: "",
    targetCountry: "",
    responsibleMemberId: 0,
    projectMembers: [],
    projectProfiles: [],
    targetJobRanks: [],
    targetDepartmentId: 0,
  });

  const [members, setMembers] = useState<MemberAdminSummary[]>([]);
  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>([]);
  const [jobRanks, setJobRanks] = useState<JobRankAdminSummary[]>([]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleCheckboxChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    item: number,
    arrayName: "projectMembers" | "projectProfiles" | "targetJobRanks",
  ) => {
    const isChecked = e.target.checked;
    if (isChecked) {
      setFormData({
        ...formData,
        [arrayName]: [...(formData[arrayName] || []), item],
      });
    } else {
      setFormData({
        ...formData,
        [arrayName]: (formData[arrayName] || []).filter((i) => i !== item),
      });
    }
  };
  useEffect(() => {
  }, [formData]);
  useEffect(() => {
    if (isUpdate && projectId) {
      baseSetting();
    }
  }, [projectId]);

  const baseSetting = async () => {
    const response = await localAxios.get(
      `${baseUrl}/admin/project/${projectId}`,
    );
    const memberResponse = await localAxios.get<MemberAdminSummary[]>(
      `${baseUrl}/admin/member`,
    );
    const departmentResponse = await localAxios.get<DepartmentAdminSummary[]>(
      `${baseUrl}/admin/department`,
    );
    const jobRankResponse = await localAxios.get<JobRankAdminSummary[]>(
      `${baseUrl}/admin/jobrank`,
    );

    setFormData({
      title: response.data.title,
      targetMemberCount: response.data.targetMemberCount,
      targetYear: response.data.targetYear,
      isPrivate: response.data.isPrivate,
      projectType: response.data.projectType,
      targetCountry: response.data.targetCountry,
      responsibleMemberId: response.data.responsibleMemberId,
      projectMembers: response.data.projectMembers.map(
        (member: ProjectAdminMemberSummary) => member.memberId,
      ),
      projectProfiles: response.data.projectProfiles?.map(
        (profile: any) => profile.id,
      ),
      targetJobRanks: response.data.targetJobRanks.map(
        (jobRank: string) =>
          jobRankResponse.data.find(
            (jr: JobRankAdminSummary) => jr.description === jobRank,
          )?.id || 0,
      ),
      targetDepartmentId: response.data.projectDepartment?.departmentId,
    });

    setMembers(memberResponse.data);
    setDepartments(departmentResponse.data);
    setJobRanks(jobRankResponse.data);
  };

  const putProject = async () => {
    if (confirm("수정하시겠습니까?")) {
      const response = await localAxios.put(
        `${baseUrl}/admin/project/${projectId}`,
        formData,
      );
      if (response.status === 200) {
        alert("정보를 수정했습니다.");
        resetProject();
        closeModal();
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>프로젝트 설정 수정</Modal.Header>
      <Modal.Body>
        {/* 프로젝트 제목 입력 */}
        <div className="mb-4">
          <label htmlFor="title" className="mb-2 block font-bold text-gray-700">
            프로젝트명
          </label>
          <input
            type="text"
            id="title"
            name="title"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.title}
            onChange={handleInputChange}
          />
        </div>

        {/* 프로젝트 타겟 멤버 수 입력 */}
        <div className="mb-4">
          <label
            htmlFor="targetMemberCount"
            className="mb-2 block font-bold text-gray-700"
          >
            타겟 멤버 수
          </label>
          <input
            type="number"
            id="targetMemberCount"
            name="targetMemberCount"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.targetMemberCount}
            onChange={handleInputChange}
          />
        </div>

        {/* 프로젝트 타겟 연도 선택 */}
        <div className="mb-4">
          <label
            htmlFor="targetYear"
            className="mb-2 block font-bold text-gray-700"
          >
            타겟 연도
          </label>

          <input
            id="targetYear"
            name="targetYear"
            type="text"
            value={formData.targetYear}
            onChange={handleInputChange}
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
          />
        </div>

        {/* 프로젝트 공개 여부 선택 */}
        <div className="mb-4">
          <label
            htmlFor="isPrivate"
            className="mb-2 block font-bold text-gray-700"
          >
            공개 여부
          </label>
          <select
            id="isPrivate"
            name="isPrivate"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.isPrivate ? "true" : "false"}
            onChange={handleInputChange}
          >
            <option value="false">공개</option>
            <option value="true">비공개</option>
          </select>
        </div>

        {/* 프로젝트 타입 선택 */}
        <div className="mb-4">
          <label
            htmlFor="projectType"
            className="mb-2 block font-bold text-gray-700"
          >
            프로젝트 타입
          </label>
          <select
            id="projectType"
            name="projectType"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.projectType}
            onChange={handleInputChange}
          >
            <option value="">선택하세요</option>
            <option value="IN_DEPT">사업부</option>
            <option value="ALL_COMPANY">전사</option>
          </select>
        </div>

        {/* 프로젝트 타겟 국가 입력 */}
        <div className="mb-4">
          <label
            htmlFor="targetCountry"
            className="mb-2 block font-bold text-gray-700"
          >
            타겟 국가
          </label>
          <input
            type="text"
            id="targetCountry"
            name="targetCountry"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.targetCountry}
            onChange={handleInputChange}
          />
        </div>

        {/* 프로젝트 책임자 선택 */}
        <div className="mb-4">
          <label
            htmlFor="responsibleMemberId"
            className="mb-2 block font-bold text-gray-700"
          >
            책임자
          </label>
          <select
            id="responsibleMemberId"
            name="responsibleMemberId"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.responsibleMemberId}
            onChange={handleInputChange}
          >
            <option value="">책임자</option>
            {members.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </div>

        {/* 프로젝트 멤버 선택 */}
        <div className="mb-4 h-28 overflow-scroll overflow-x-hidden">
          <label className="mb-2 block font-bold text-gray-700">
            프로젝트 멤버
          </label>
          {members.map((member) => (
            <div key={member.id} className="flex items-center">
              <input
                type="checkbox"
                id={`member-${member.id}`}
                className="mr-2 h-4 w-4"
                checked={formData.projectMembers?.includes(member.id)}
                onChange={(e) =>
                  handleCheckboxChange(e, member.id, "projectMembers")
                }
              />
              <label htmlFor={`member-${member.id}`} className="text-gray-700">
                {member.name}
              </label>
            </div>
          ))}
        </div>

        {/* 타겟 직급 선택 */}
        <div className="mb-4 h-32 overflow-scroll overflow-x-hidden">
          <label className="mb-2 block font-bold text-gray-700">
            타겟 직급
          </label>
          <div className="flex flex-wrap gap-2">
            {jobRanks.map((jobRank) => (
              <div key={jobRank.id} className="flex items-center">
                <input
                  type="checkbox"
                  id={`jobRank-${jobRank.id}`}
                  className="mr-2 size-4 border border-slate-300 shadow-sm"
                  checked={formData.targetJobRanks?.includes(jobRank.id)}
                  onChange={(e) =>
                    handleCheckboxChange(e, jobRank.id, "targetJobRanks")
                  }
                />
                <label
                  htmlFor={`jobRank-${jobRank.id}`}
                  className="text-gray-700"
                >
                  {jobRank.description}
                </label>
              </div>
            ))}
          </div>
        </div>

        {/* 프로젝트 부서 선택 */}
        <div className="mb-4">
          <label
            htmlFor="targetDepartmentId"
            className="mb-2 block font-bold text-gray-700"
          >
            프로젝트 부서
          </label>
          <select
            id="targetDepartmentId"
            name="targetDepartmentId"
            className="focus:shadow-outline w-full appearance-none rounded border px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            value={formData.targetDepartmentId}
            onChange={handleInputChange}
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

        <div className="flex justify-end">
          {/* <Button className="bg-red-500" onClick={closeModal}>
            닫기
          </Button> */}
          <Button className="ml-2 bg-blue-500" onClick={putProject}>
            수정
          </Button>
        </div>
      </Modal.Body>
    </Modal>
  );
}
