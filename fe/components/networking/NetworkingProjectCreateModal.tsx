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
import ProfileImageUpload from "../member/ProfileImageUploaded";
import {
  NetworkingCreate,
  NetworkingSearchCondition,
} from "@/types/networking/Networking";
import MemberSelectionDrawer from "../member/MemberSelectionDrawer";
import ExecutiveSelectionDrawer from "../member/ExecutiveSelectionDrawer";
import MemberSelectionModal from "../member/MemberSelectionModal";
import ExecutiveSelectionModal from "../member/ExecutiveSelectionModal";

export function NetworkingProjectCreateModal({
  showModal,
  closeModal,
  size,
  searchCondition,
  profileId,
}: {
  showModal: boolean;
  closeModal: () => void;
  size: string;
  searchCondition: NetworkingSearchCondition;
  profileId: number;
}) {
  const [member, setMember] = useState<MemberAdminSummary>();
  const localAxios = useLocalAxios();
  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);
  const [isExecutiveSelectionOpen, setIsExecutiveSelectionOpen] =
    useState(false);
  const [formData, setFormData] = useState<NetworkingCreate>({
    networkingId: 0,
    category: "",
    executiveId: 0,
    memberId: 0,
    memberName: "",
    executiveName: "",
  });
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const postNetworking = async () => {
    if (confirm("등록하시겠습니까?")) {
      const response = await localAxios.post(
        `/networking/profile/${profileId}`,
        formData,
      );
      if (response.status == 200) {
        alert("네트워킹 등록에 성공하였습니다.");
        setFormData({
          networkingId: 0,
          category: "",
          executiveId: 0,
          memberId: 0,
          memberName: "",
          executiveName: "",
        });
        closeModal();
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };
  const addNetworkingExecutive = (
    executiveId: number,
    executiveName: string,
  ) => {
    setFormData({
      ...formData,
      executiveId: executiveId,
      executiveName: executiveName,
    });
  };
  const addNetworkingMember = (memberId: number, memberName: string) => {
    setFormData({
      ...formData,
      memberId: memberId,
      memberName: memberName,
    });
  };

  const toggleExecutiveSelectionDrawer = () => {
    setIsExecutiveSelectionOpen(!isExecutiveSelectionOpen);
  };
  const toggleMemberSelectionDrawer = () => {
    setIsMemberSelectionOpen(!isMemberSelectionOpen);
  };

  const handleOnCloseModal = () => {
    setFormData({
      networkingId: 0,
      category: "",
      executiveId: 0,
      memberId: 0,
      memberName: "",
      executiveName: "",
    });
    closeModal();
  };
  return (
    <Modal show={showModal} onClose={handleOnCloseModal} size={size}>
      <Modal.Header>네트워킹 등록</Modal.Header>
      <Modal.Body>
        <div className="relative z-0 mb-5 flex w-full justify-between gap-5">
          <div className="relative flex w-full flex-col gap-2">
            <div className="relative">
              <input
                type="text"
                name="category"
                id="floating_name"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                value={formData.category}
                onChange={handleInputChange}
              />
              <label
                htmlFor="floating_name"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                전문 분야
              </label>
            </div>
            <div className="relative flex">
              <input
                type="text"
                name="executiveName"
                id="floating_knoxId"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                disabled={true}
                value={formData.executiveName}
              />
              <label
                htmlFor="floating_knoxId"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                직급4 정보
              </label>
              <Button
                onClick={toggleExecutiveSelectionDrawer}
                className="text-md  w-64 pt-1.5 font-semibold"
              >
                담당자 선택
              </Button>
            </div>
            <div className="relative flex">
              <input
                type="text"
                name="memberName"
                id="floating_password"
                className="peer block w-full appearance-none border-0 border-b-2 border-gray-300 bg-transparent px-0 py-2.5 pt-5 text-sm text-gray-900 focus:border-blue-600 focus:outline-none focus:ring-0 dark:border-gray-600 dark:text-white dark:focus:border-blue-500"
                required
                disabled={true}
                value={formData.memberName}
              />
              <label
                htmlFor="floating_password"
                className="absolute left-0 top-0 origin-[0] px-0 text-sm text-gray-500 transition-all duration-300 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-base peer-focus:top-0 peer-focus:text-xs peer-focus:font-medium peer-focus:text-blue-600 dark:text-gray-400 peer-focus:dark:text-blue-500"
              >
                채용 담당자
              </label>
              <Button
                onClick={toggleMemberSelectionDrawer}
                className="text-md w-64 pt-1.5 font-semibold"
              >
                네트워킹 담당자 선택
              </Button>
            </div>
          </div>
        </div>
        <MemberSelectionModal
          isOpen={isMemberSelectionOpen}
          toggleDrawer={toggleMemberSelectionDrawer}
          onClose={addNetworkingMember}
          size="3xl"
          title="네트워킹 담당자 선택"
        />
        <ExecutiveSelectionModal
          isOpen={isExecutiveSelectionOpen}
          toggleDrawer={toggleExecutiveSelectionDrawer}
          onClose={addNetworkingExecutive}
          size="3xl"
          title="네트워킹 직급4 선택"
        />

        <div className="flex flex-row justify-end gap-1">
          <Button
            className=" text-md font-semibold hover:cursor-pointer"
            onClick={postNetworking}
          >
            등록하기
          </Button>
        </div>
      </Modal.Body>
      <Modal.Footer></Modal.Footer>
    </Modal>
  );
}
