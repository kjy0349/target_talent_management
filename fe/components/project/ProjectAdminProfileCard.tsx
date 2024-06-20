"use client";
import Image from "next/image";
import { HiChevronRight, HiChevronUp } from "react-icons/hi";
import { useState } from "react";
import {
  Dropdown,
  Badge,
  Avatar,
  Card,
  Accordion,
  Button,
} from "flowbite-react";
import { ProjectProfilePreview } from "@/types/admin/Project";
import MemberSelectionDrawer from "../member/MemberSelectionDrawer";
import { useLocalAxios } from "@/app/api/axios";
import ExecutiveSelectionDrawer from "../member/ExecutiveSelectionDrawer";
interface ProjectProfileCardProps {
  projectId: number;
  profile: ProjectProfilePreview;
  onDeleteProfile: (profileId: number) => void;
  rebase: () => void;
  handleCheck: () => void;
  checked: boolean;
}
export default function ProjectAdminProfileCard({
  projectId,
  profile,
  onDeleteProfile,
  rebase,
  handleCheck,
  checked,
}: ProjectProfileCardProps) {
  const [showAllCareer, setShowAllCareer] = useState(false);
  const initialCareers = profile.careersPreview.slice(0, 2);
  const remainingCareers = profile.careersPreview.slice(2);
  const localAxios = useLocalAxios();
  const getKoreanDegree = (degree: string) => {
    if (degree === "BACHELOR") return "학";
    else if (degree === "MASTER") return "석";
    else return "박";
  };
  const toggleMemberSelectionDrawer = () => {
    setIsMemberSelectionOpen(!isMemberSelectionOpen);
  };
  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);
  const onCloseExecutiveSelection = async (executiveId: number) => {
    const sendData = {
      executiveId: executiveId,
      profileId: profile.profileId,
    };
    const updateResponse = await localAxios.put(
      "/admin/profile/executive",
      sendData,
    );
    if (updateResponse.status == 200) {
      alert("수정에 성공하였습니다.");
      rebase();
    } else {
      alert("수정에 실패하였습니다 잠시 후 다시 시도해주세요. ");
    }
  };
  const getTagColorClass = (index: number) => {
    const colorClasses = [
      "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300",
      "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300",
      "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300",
      "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300",
      "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300",
      "bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-300",
      "bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-300",
      "bg-pink-100 text-pink-800 dark:bg-pink-900 dark:text-pink-300",
    ];
    return colorClasses[index % colorClasses.length];
  };
  return (
    <Card className="mb-2 w-full">
      <div>
        <div>
          <div>
            <div className="flex w-full items-center justify-between">
              <div className="flex items-center space-x-4">
                <Avatar
                  alt="Profile"
                  size="lg"
                  className="rounded-full shadow-lg"
                />
                <div className="w-full">
                  <h5 className="text-lg font-bold text-gray-900 dark:text-white">
                    {profile.columnDatas.name}
                    <span className="ml-2 text-sm text-gray-600 dark:text-white">
                      {profile.columnDatas.column1}
                    </span>
                  </h5>
                  <p className="text-sm text-gray-500 dark:text-gray-400">
                    {/* {profile.careersPreview[profile.careersPreview.length - 1]} */}
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div>
            <div className="mt-4">
              <div className="mb-4">
                <div className="flex w-full justify-between">
                  <h3 className="mb-2 font-semibold text-gray-900 dark:text-white">
                    경력
                  </h3>
                  <Dropdown
                    inline
                    arrowIcon={false}
                    label={
                      <svg
                        className="size-5"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z" />
                      </svg>
                    }
                  >
                    <Dropdown.Item>tech에 추가</Dropdown.Item>
                    <Dropdown.Item onClick={toggleMemberSelectionDrawer}>
                      네트워크 담당자 편집
                    </Dropdown.Item>
                    <Dropdown.Item>PDF로 저장</Dropdown.Item>
                    <Dropdown.Item
                      className="text-red-500"
                      onClick={() => onDeleteProfile(profile.profileId)}
                    >
                      프로젝트에서 삭제
                    </Dropdown.Item>
                  </Dropdown>
                </div>
                {initialCareers.map((c, index) => (
                  <p
                    key={index}
                    className="text-sm text-gray-500 dark:text-gray-400"
                  >
                    {c.companyName}, {c.role} ({" "}
                    {new Date(c.startedAt).toLocaleDateString()} ~{" "}
                    {new Date(c.endedAt).toLocaleDateString()})
                  </p>
                ))}
                {!showAllCareer && remainingCareers.length > 0 && (
                  <button
                    className="mt-1 text-sm font-medium text-blue-600 hover:underline dark:text-blue-500"
                    onClick={() => setShowAllCareer(true)}
                  >
                    더 보기 ({remainingCareers.length}){" "}
                    <HiChevronRight className="inline-block" />
                  </button>
                )}
                {showAllCareer && (
                  <>
                    {remainingCareers.map((c, index) => (
                      <p
                        key={index}
                        className="text-sm text-gray-500 dark:text-gray-400"
                      >
                        {c.companyName}, {c.role} ({" "}
                        {new Date(c.startedAt).toLocaleDateString()} ~{" "}
                        {new Date(c.endedAt).toLocaleDateString()})
                      </p>
                    ))}
                    <button
                      className="mt-1 text-sm font-medium text-blue-600 hover:underline dark:text-blue-500"
                      onClick={() => setShowAllCareer(false)}
                    >
                      숨기기 <HiChevronUp className="inline-block" />
                    </button>
                  </>
                )}
              </div>
              <div className="mb-4">
                <h3 className="mb-2 font-semibold text-gray-900 dark:text-white">
                  학력
                </h3>
                {profile.educationsPreview.map((e, index) => (
                  <p
                    key={index}
                    className="text-sm text-gray-500 dark:text-gray-400"
                  >
                    {getKoreanDegree(e.degree.toString())} {e.schoolName},{" "}
                    {e.major} ( {new Date(e.enteredAt).getFullYear()} ~{" "}
                    {new Date(e.graduatedAt).getFullYear()})
                  </p>
                ))}
              </div>
              <div className="mb-4">
                <h3 className="mb-2 font-semibold text-gray-900 dark:text-white">
                  special
                </h3>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  special 없음
                </p>
                <div className="flex flex-wrap gap-2">
                  {profile.keywordsPreview.map((k, index) => (
                    <Badge key={index} color="gray" className="p-1">
                      {k.data}
                    </Badge>
                  ))}
                </div>
              </div>
              <div className="mb-4">
                <h3 className="mb-2 font-semibold text-gray-900 dark:text-white">
                  네트워킹
                </h3>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  {profile.networkingExecutivePreview
                    ? profile.networkingExecutivePreview.executiveName
                    : "담당자 없음"}{" "}
                  {profile.networkingExecutivePreview && (
                    <span>
                      (네트워킹 횟수{" "}
                      {profile.networkingExecutivePreview
                        ? profile.networkingExecutivePreview.NetworkingCount ||
                          0
                        : 0}{" "}
                      회)
                    </span>
                  )}
                </p>
              </div>
              <div className="text-right text-sm text-gray-500 dark:text-gray-400">
                <p>
                  {profile.memberPreview?.departmentName} /{" "}
                  {profile.memberPreview?.name}
                </p>
                <p>
                  {new Date(profile.createdAt).getFullYear()}-
                  {new Date(profile.createdAt).getMonth() + 1}-
                  {new Date(profile.createdAt).getDate()}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <ExecutiveSelectionDrawer
        isOpen={isMemberSelectionOpen}
        toggleDrawer={toggleMemberSelectionDrawer}
        onClose={onCloseExecutiveSelection}
      />
    </Card>
  );
}
