"use client";

import {
  NetworkingSummary,
  NetworkingUpdate,
  NetworkingSearchCondition,
} from "@/types/networking/Networking";
import DateComponent from "../utils/DateComponent";
import { useEffect, useRef, useState } from "react";
import ProfileSelectionDrawer from "../profile/ProfileSelectionDrawer";
import MemberSelectionDrawer from "../member/MemberSelectionDrawer";
import { Button, Dropdown, Select, theme } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import ExecutiveSelectionDrawer from "../member/ExecutiveSelectionDrawer";
import { HiAnnotation, HiBriefcase, HiPlusCircle, HiTag } from "react-icons/hi";
import { twMerge } from "tailwind-merge";
import { HiBars3, HiMiniArrowTopRightOnSquare } from "react-icons/hi2";
import ProfileMultipleSelectionDrawer from "../profile/ProfileMultipleSelectionDrawer";
import MemberSelectionModal from "../member/MemberSelectionModal";
import ProfileMutipleSelectionModal from "../profile/ProfileMultipleSelectModal";
import ExecutiveSelectionModal from "../member/ExecutiveSelectionModal";
import { AxiosError } from "axios";

interface NetworkingListRowProps {
  networking: NetworkingSummary;
  onClose: () => void;
  searchCondition: NetworkingSearchCondition;
  onChangeChecked: (checked: boolean, networkId: number) => void;
}
//TODO 타겟 직급 추가
const mapStatus = (status: string) => {
  if ("READY_NETWORKING" == status) {
    return "미할당";
  } else if ("DOING_NETWORKING" == status) {
    return "활동 中";
  } else if ("READY_APPROVEMENT" == status) {
    return "승인 대기 中";
  } else if ("DONE" == status) {
    return "완료됨";
  } else {
    return "해당 없음";
  }
};

const mapStatusReverse = (status: string) => {
  if ("미할당" == status) {
    return "READY_NETWORKING";
  } else if ("활동 中" == status) {
    return "DOING_NETWORKING";
  } else if ("승인 대기 中" == status) {
    return "READY_APPROVEMENT";
  } else if ("완료됨" == status) {
    return "DONE";
  } else {
    return "";
  }
};
export default function NetworkingListRow({
  networking,
  onClose,
  searchCondition,
  onChangeChecked,
}: NetworkingListRowProps) {
  const localAxios = useLocalAxios();
  const [isProfileSelectionOpen, setIsProfileSelectionOpen] = useState(false);
  const [category, setCategory] = useState<string>(networking.category);
  const onChangeCategory = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    setCategory(e.target.value);
  };

  function parseLocalDateTime(date: Date): string {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth()}.${newDate.getDay()}`;
  }

  const formData = useRef<NetworkingUpdate>({
    networkingId: networking.networkingId,
    category: networking.category,
    memberId: networking.memberId,
    networkingStatus: networking.status,
    executiveId: networking.executiveId,
    networkingProfileIds: networking.networkingProfilePreviewSummaryDtos.map(
      (np) => np.profileId,
    ),
    executiveName: networking.executiveName,
    responsibleMemberName: networking.responsibleMemberName,
  });

  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);
  const [isExecutiveSelectionOpen, setIsExecutiveSelectionOpen] =
    useState(false);
  const [isEditingCategory, setIsEditingCategory] = useState(false);
  const [isEditingStatus, setIsEditingStatus] = useState(false);
  const [status, setStatus] = useState<string>(networking.status);

  const updateNetworking = async () => {
    const response = await localAxios.put(
      `/networking/${networking.networkingId}`,
      formData.current,
    );

    if (response.status == 200) {
      onClose();
    }
  };

  const addNetworkingProfile = (ids: number[]) => {
    const nextIds = ids.concat(
      networking.networkingProfilePreviewSummaryDtos.map((p) => p.profileId),
    );
    updateNetworkingProfile(nextIds);
  };
  const deleteNetworkingProfile = (id: number) => {
    if (confirm("정말 해당 인재를 네트워킹 해제 하시겠습니까?")) {
      formData.current.networkingProfileIds =
        formData.current.networkingProfileIds.filter((pid) => pid !== id);

      updateNetworkingProfile(formData.current.networkingProfileIds);
    }
  };

  const updateNetworkingProfile = async (
    nextProfileNetworkingIds: number[],
  ) => {
    const updateNetworkingProfile = {
      networkingProfileIds: nextProfileNetworkingIds,
    };
    // console.log("update", nextProfileNetworkingIds);

    const response = await localAxios.put(
      `/networking/profile/${networking.networkingId}`,
      updateNetworkingProfile,
    );
    if (response.status == 200) {
      formData.current.networkingProfileIds = nextProfileNetworkingIds;
      onClose();
    }
  };

  const addNetworkingMember = (memberId: number, memberName: string) => {
    formData.current.memberId = memberId;
    formData.current.responsibleMemberName = memberName;
  };

  const onChangeStatus = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    setStatus(e.target.value);
    formData.current.networkingStatus = e.target.value;
  };
  const onBlurStatus = () => {
    setIsEditingStatus(false);
    updateNetworking();
  };

  const onBlurCategory = () => {
    formData.current.category = category;
    updateNetworking();
    setIsEditingCategory(false);
  };

  const toggleMemberSelectionDrawer = () => {
    setIsMemberSelectionOpen(!isMemberSelectionOpen);
  };

  const toggleProfileSelectionDrawer = () => {
    setIsProfileSelectionOpen(!isProfileSelectionOpen);
  };
  const handleProfileSelectionClose = (ids: number[]) => {
    addNetworkingProfile(ids);
    toggleProfileSelectionDrawer();
  };

  const addNetworkingExecutive = async (
    executiveId: number,
    executiveName: string,
  ) => {
    formData.current.executiveId = executiveId;
    formData.current.executiveName = executiveName;

    const networkingExecutiveResponse = await localAxios.put(
      `networking/executive/${networking.networkingId}`,
      formData.current,
    );
    if (networkingExecutiveResponse.status == 200) {
      alert("네트워킹 매핑 수정에 성공하였습니다.");
      onClose();
    }
  };

  const toggleExecutiveSelectionDrawer = () => {
    setIsExecutiveSelectionOpen(!isExecutiveSelectionOpen);
  };

  const deleteNetworking = async () => {
    if (confirm("정말 삭제하시겠습니까?")) {
      const response = await localAxios.delete(
        `/networking/${networking.networkingId}`,
      );
      if (response.status == 200) {
        alert("삭제에 성공하였습니다.");
        onClose();
      }
    }
  };

  const onCategoryKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key == "Enter") {
      setIsEditingCategory(false);
      formData.current.category = category;
      updateNetworking();
    }
  };

  return (
    <>
      <tr
        key={networking.networkingId}
        className="h-[110px] text-xs text-slate-700 hover:bg-slate-100"
      >
        <td className="px-1  py-1 pl-3">
          <input
            type="checkbox"
            onChange={(e) =>
              onChangeChecked(e.target.checked, networking.networkingId)
            }
          />
        </td>
        <td
          className="px-4 py-1 text-left"
          onDoubleClick={() => setIsEditingCategory(true)}
        >
          {isEditingCategory ? (
            <input
              type="text"
              name="category"
              value={category}
              onChange={onChangeCategory}
              onKeyDown={onCategoryKeyDown}
              className="w-[300px] text-xs"
              onBlur={() => onBlurCategory()}
            />
          ) : (
            category
          )}
        </td>
        <td
          className="px-1 py-1 pl-4"
          onDoubleClick={toggleExecutiveSelectionDrawer}
        >
          {networking.executiveName}
        </td>
        <td className="px-4 py-1">{networking.responsibleMemberName}</td>
        {/* <td className="px-1 py-1" onDoubleClick={toggleMemberSelectionDrawer}>
          {formData.responsibleMemberName}
        </td> */}
        <td
          className="scroll scroll1 flex overflow-x-scroll px-1 py-1 text-xs"
          style={{ width: "600px", height: "110px" }}
        >
          {networking.networkingProfilePreviewSummaryDtos.map((n, index) => (
            <div
              key={index}
              className="group relative mx-1 w-[200px] flex-shrink-0"
              onClick={() => deleteNetworkingProfile(n.profileId)}
            >
              <div className="p-1 text-xs transition duration-200 group-hover:opacity-50">
                {n.name ? n.name : "해당 없음"}(
                {n.column1 ? n.column1 : "해당 없음"})
                <br />
                {n.companyName ? n.companyName : "해당 없음"},
                {n.jobRank ? n.jobRank : "해당 없음"}(
                {n.careerStartedAt
                  ? parseLocalDateTime(n.careerStartedAt)
                  : "해당 없음"}
                ~
                {n.careerEndedAt
                  ? parseLocalDateTime(n.careerEndedAt)
                  : "해당 없음"}
                )
                <br />({n.schoolName ? n.schoolName : "해당 없음"}),{" "}
                {n.schoolMajor ? n.schoolMajor : "해당 없음"} ,{" "}
                {n.schoolDegree ? n.schoolDegree : "해당 없음"}
                <br />
                분야:{n.techDetailName ? n.techDetailName : "전문 분야 없음"}
              </div>
              <div className="absolute inset-0 flex items-center justify-center opacity-0 transition duration-200 group-hover:opacity-100">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                  className="h-6 w-6 text-red-500"
                >
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
            </div>
          ))}
        </td>
        <td
          className="cursor-pointer px-1 text-center"
          onClick={toggleProfileSelectionDrawer}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={1.5}
            stroke="currentColor"
            className="mx-auto h-6 w-6"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M12 4.5v15m7.5-7.5h-15"
            />
          </svg>
        </td>

        <td
          className="px-1 py-1 pl-4"
          onDoubleClick={() => {
            if (
              networking.status == "READY_NETWORKING" ||
              networking.status == "READY_APPROVEMENT"
            ) {
              alert("아직 승인이 되지 않은 네트워킹 매핑 입니다.");
              return;
            }
            setIsEditingStatus(true);
          }}
        >
          {isEditingStatus ? (
            <Select
              name="networkingStatus"
              value={status}
              onChange={onChangeStatus}
              onBlur={() => onBlurStatus()}
            >
              <option value={"DOING_NETWORKING"}>활동 中</option>
              {/* <option value={"READY_APPROVEMENT"}>승인 대기 中</option> */}
              <option value={"DONE"}>완료</option>
            </Select>
          ) : (
            <>
              {/* TODO 상태 변경일자를 기준으로 시작일을 변경한다.*/}
              {mapStatus(status)}
              <div className="text-xs text-gray-400">시작일 : 24.01.01</div>
            </>
          )}
        </td>
      </tr>
      <MemberSelectionModal
        isOpen={isMemberSelectionOpen}
        toggleDrawer={toggleMemberSelectionDrawer}
        onClose={addNetworkingMember}
        size="3xl"
        title="네트워킹 담당자 선택"
      />
      <ProfileMutipleSelectionModal
        size="2xl"
        domain="network"
        domainId={networking.networkingId}
        title="네트워킹 인재 선택"
        isOpen={isProfileSelectionOpen}
        toggleDrawer={toggleProfileSelectionDrawer}
        onClose={handleProfileSelectionClose}
      />
      {/* <MemberSelectionDrawer
        isOpen={isMemberSelectionOpen}
        toggleDrawer={toggleMemberSelectionDrawer}
        onClose={addNetworkingMember}
      /> */}
      <ExecutiveSelectionModal
        title="네트워킹 직급4 선택"
        size="3xl"
        isOpen={isExecutiveSelectionOpen}
        toggleDrawer={toggleExecutiveSelectionDrawer}
        onClose={addNetworkingExecutive}
      />
    </>
  );
}
