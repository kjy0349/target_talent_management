"use client";
import { useLocalAxios } from "@/app/api/axios";
import {
  AuthorityAdminSummary,
  DepartmentAdminSummary,
  MemberAdminCreate,
  MemberAdminSummary,
  RoleAdminSummary,
  TeamAdminSummary,
} from "@/types/admin/Member";
import { Button, Modal } from "flowbite-react";
import { useEffect, useState } from "react";
import ProfileImageUpload from "./ProfileImageUploaded";
import { AxiosError } from "axios";

export function MemberCreateModal({
  showModal,
  closeModal,
  size,
  rebase,
}: {
  showModal: boolean;
  closeModal: () => void;
  size: string;
  rebase: (page: number) => void;
}) {
  const [member, setMember] = useState<MemberAdminSummary>();
  const localAxios = useLocalAxios();

  const [authorities, setAuthorities] = useState<AuthorityAdminSummary[]>();
  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>();
  const [teams, setTeams] = useState<TeamAdminSummary[]>();
  const [roles, setRoles] = useState<RoleAdminSummary[]>();
  const [formData, setFormData] = useState<MemberAdminCreate>({
    name: "",
    password: "",
    departmentName: "",
    profileImage: "",
    knoxId: "",
    telephone: "",
    departmentId: 0,
    teamId: 0,
    roleId: 0,
    authorityId: 0,
  });
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  const handleImageUpload = (imageUrl: string) => {
    setFormData({ ...formData, profileImage: imageUrl });
  };
  useEffect(() => {
    const getSetting = async () => {
      const responseAuthority =
        await localAxios.get<AuthorityAdminSummary[]>(`/admin/authority`);

      setAuthorities(responseAuthority.data);
      const responseDepartment =
        await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);
      setDepartments(responseDepartment.data);
      const responseTeam =
        await localAxios.get<TeamAdminSummary[]>(`/admin/team`);
      setTeams(responseTeam.data);
      const responseRole =
        await localAxios.get<RoleAdminSummary[]>(`/admin/role`);
      setRoles(responseRole.data);
    };
    getSetting();
  }, []);

  const checkedArray = [
    <svg
      key={0}
      className="me-2 h-3.5 w-3.5 flex-shrink-0 text-gray-500 dark:text-gray-400"
      aria-hidden="true"
      xmlns="http://www.w3.org/2000/svg"
      fill="currentColor"
      viewBox="0 0 20 20"
    >
      <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
    </svg>,
    <svg
      key={1}
      className="me-2 h-3.5 w-3.5 flex-shrink-0 text-green-500 dark:text-green-400"
      aria-hidden="true"
      xmlns="http://www.w3.org/2000/svg"
      fill="currentColor"
      viewBox="0 0 20 20"
    >
      <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
    </svg>,
  ];
  const getChecked = (status: boolean) => {
    return checkedArray[Number(status)];
  };
  const postMember = async () => {
    if (confirm("등록하시겠습니까?")) {
      try {
        const response = await localAxios.post(`/admin/member`, formData);
        if (response.status == 200) {
          alert("등록에 성공하였습니다.");
          rebase(0);
          setFormData({
            name: "",
            password: "",
            departmentName: "",
            profileImage: "",
            knoxId: "",
            telephone: "",
            departmentId: 0,
            teamId: 0,
            roleId: 0,
            authorityId: 0,
          });

          closeModal();
        }
      } catch (e) {
        const err = e as AxiosError;

        if (err.response?.status == 406) {
          alert("중복된 knox ID 입니다.");
        }
      }
    }
  };
  return (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>멤버 등록</Modal.Header>
      <Modal.Body>
        <div className="relative z-0 mb-5 flex w-full justify-between gap-5">
          <div className="aspect-square w-1/3">
            <div>
              <ProfileImageUpload
                className="h-64 w-64"
                onImageUpload={handleImageUpload}
              />
            </div>
          </div>

          <div className="relative flex w-2/3 flex-col gap-2">
            <div className="relative">
              <input
                type="text"
                name="name"
                id="floating_name"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                value={formData.name}
                onChange={handleInputChange}
              />
              <label
                htmlFor="floating_name"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                이름
              </label>
            </div>
            <div className="relative">
              <input
                type="text"
                name="knoxId"
                id="floating_knoxId"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                value={formData.knoxId}
                onChange={handleInputChange}
              />
              <label
                htmlFor="floating_knoxId"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                Knox 아이디
              </label>
            </div>
            <div className="relative">
              <input
                type="password"
                name="password"
                id="floating_password"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                value={formData.password}
                onChange={handleInputChange}
              />
              <label
                htmlFor="floating_password"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                비밀번호
              </label>
            </div>
            <div className="relative">
              <input
                type="tel"
                name="telephone"
                id="floating_telephone"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                value={formData.telephone}
                onChange={handleInputChange}
              />
              <label
                htmlFor="floating_telephone"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                연락처
              </label>
            </div>
          </div>
        </div>
        <div className="flex justify-between gap-1">
          <div className="relative">
            <label
              htmlFor="authority"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              권한
            </label>
            <select
              id="authority"
              name="authorityId"
              className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
              value={formData.authorityId}
              onChange={handleInputChange}
            >
              <option value={undefined}>해당 없음</option>
              {authorities?.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.authName}
                </option>
              ))}
            </select>
          </div>

          <div className="relative">
            <label
              htmlFor="department"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              사업부
            </label>
            <select
              id="department"
              name="departmentId"
              className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
              value={formData.departmentId}
              onChange={handleInputChange}
            >
              <option value={undefined}>해당 없음</option>
              {departments?.map((d) => (
                <option key={d.departmentId} value={d.departmentId}>
                  {d.name}
                </option>
              ))}
            </select>
          </div>

          <div className="relative">
            <label
              htmlFor="team"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              부서
            </label>
            <select
              id="team"
              name="teamId"
              className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
              value={formData.teamId}
              onChange={handleInputChange}
            >
              <option value={undefined}>해당 없음</option>
              {teams?.map((t) => (
                <option key={t.teamId} value={t.teamId}>
                  {t.name}
                </option>
              ))}
            </select>
          </div>

          <div className="relative">
            <label
              htmlFor="role"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              담당 업무
            </label>
            <select
              id="role"
              name="roleId"
              className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
              value={formData.roleId}
              onChange={handleInputChange}
            >
              <option value={undefined}>해당 없음</option>
              {roles?.map((r) => (
                <option key={r.roleId} value={r.roleId}>
                  {r.description}
                </option>
              ))}
            </select>
          </div>
        </div>
        <h2 className="mb-2 mt-2 text-lg font-semibold text-gray-900 dark:text-white">
          세부 사항 입력 여부
        </h2>
        <ul className="max-w-md list-inside space-y-1 text-gray-500 dark:text-gray-400">
          <li className="flex items-center">
            {getChecked(formData.authorityId > 0)}
            권한 설정 완료 여부
          </li>
          <li className="flex items-center">
            {getChecked(formData.departmentId > 0)}
            사업부 설정 완료 여부
          </li>
          <li className="flex items-center">
            {getChecked(formData.teamId > 0)}부서 설정 완료 여부
          </li>
          <li className="flex items-center">
            {getChecked(formData.roleId > 0)}
            담당 업무 설정 완료 여부
          </li>
        </ul>
        <div className="flex flex-row justify-end gap-1">
          <Button className="bg-red-500" onClick={closeModal}>
            닫기
          </Button>
          <Button className="bg-blue-500" onClick={postMember}>
            등록하기
          </Button>
        </div>
      </Modal.Body>
      <Modal.Footer></Modal.Footer>
    </Modal>
  );
}
