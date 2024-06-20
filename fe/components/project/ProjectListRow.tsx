"use client";

import { useLocalAxios } from "@/app/api/axios";
import { useAuthStore } from "@/stores/auth";
import { ProjectSummary } from "@/types/admin/Project";
import { AxiosError } from "axios";
import { Badge, Dropdown, Table } from "flowbite-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { HiLockClosed, HiLockOpen } from "react-icons/hi";
import { twMerge } from "tailwind-merge";

interface ProjectListRowProps {
  project: ProjectSummary;
  onDeleteParent: () => void;
  openModal: (id: number) => void;
  closeModal: () => void;
  authLevel?: number;
  // checked: boolean,
  // handleCheck: () => void;
}

export default function ProjectListRow({
  project,
  onDeleteParent,
  openModal,
  closeModal,
  authLevel,
}: ProjectListRowProps) {
  const router = useRouter();
  const localAxios = useLocalAxios();
  const [isBookmarked, setBookmarked] = useState(project.isBookMarked);

  const onDeleteProject = async (id: number) => {
    if (confirm("프로젝트를 삭제하시겠습니까?")) {
      try {
        const response = await localAxios.delete(`/project/${id}`);
        if (response.status == 200) {
          alert("프로젝트를 삭제했습니다.");
          onDeleteParent();
        }
      } catch (error) {
        const e = error as AxiosError;
        if (e.response?.status == 403) {
          alert("권한이 없습니다.");
        } else {
          alert("잠시 후 다시 시도해주세요.");
        }
      }
    }
  };
  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth() + 1}.${newDate.getDate()}`;
  };
  const handleonChangeBooKMark = async (isBookMarked: boolean) => {
    const updateResponse = await localAxios.put(
      `/project/bookmark/${project.id}`,
      {
        isBookMarked: isBookMarked,
      },
    );
    if (updateResponse.status == 200) {
      // alert("북마크 등록에 성공하였습니다!");
      setBookmarked(isBookMarked);
      onDeleteParent();
    } else {
      // alert("북마크 등록에 실패하였습니다.");
    }
  };
  const getProjectTypeName = (projectType: string) => {
    switch (projectType) {
      case "BOTH":
        return "전사/사업부";
      case "ALL_COMPANY":
        return "전사";
      default:
        return "사업부";
    }
  };
  const getTagColorClass = (index: number) => {
    const colorClasses = [
      "bg-blue-300 text-blue-800 dark:bg-blue-900 dark:text-blue-300",
      // "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300",
      "bg-red-300 text-red-800 dark:bg-red-900 dark:text-red-300",
      "bg-green-300 text-green-800 dark:bg-green-900 dark:text-green-300",
      "bg-yellow-300 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300",
      "bg-indigo-300 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-300",
      "bg-purple-300 text-purple-800 dark:bg-purple-900 dark:text-purple-300",
      "bg-pink-300 text-pink-800 dark:bg-pink-900 dark:text-pink-300",
    ];
    return colorClasses[index % colorClasses.length];
  };

  const getLockedIfAdmin = () => {
    if (authLevel == 1 && project.isPrivate) {
      return (
        <div className="flex w-full overflow-hidden">
          <HiLockClosed size={15} />
          <div className="w-[330px] overflow-hidden">{project.title}</div>
        </div>
      );
    }
    return <>{project.title}</>;
  };
  if (authLevel == undefined) return null;
  return (
    <tr
      className="whitespace-nowrap border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
      style={{ height: "40px" }}
    >
      <td className="h-10">
        <button
          className="text-gray-400 hover:text-yellow-500 focus:outline-none"
          onClick={() => handleonChangeBooKMark(!isBookmarked)}
        >
          <svg
            viewBox="0 0 24 24"
            className={`size-6 fill-current ${isBookmarked ? "text-yellow-300" : "text-slate-100"}`}
            xmlns="http://www.w3.org/2000/svg"
          >
            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.62L12 2 9.19 8.62 2 9.24l5.46 4.73L5.82 21z" />
          </svg>
        </button>{" "}
      </td>
      <td
        scope="row"
        className="flex w-full cursor-pointer items-center overflow-hidden px-2 py-2 font-medium text-gray-900 hover:underline "
        onClick={() => router.push(`/project/${project.id}`)}
      >
        {getLockedIfAdmin()}
      </td>
      <td className="overflow-hidden px-2 py-2">{project.targetYear}</td>
      <td className="w-24 overflow-hidden px-2 py-2">
        {project.departmentName}
      </td>

      <td className="overflow-hidden px-2 py-2">
        <div className="flex w-full -space-x-4">
          <div className="flex w-32 flex-row flex-wrap gap-2">
            {project.targetJobRanks.length ? (
              project.targetJobRanks.map((str, index) => (
                <Badge
                  key={index}
                  className={`${getTagColorClass(index)} text-white`}
                >
                  {str}
                </Badge>
              ))
            ) : (
              <Badge className={`${getTagColorClass(0)} text-white`}>
                "타겟 직급 없음"
              </Badge>
            )}

            {/* <Badge className="bg-red-500 text-white">직급4</Badge> */}
          </div>
        </div>
      </td>
      <td className="w-32 overflow-hidden px-2 py-2">
        {getProjectTypeName(project.projectType)}
      </td>

      <td className="w-16 overflow-hidden px-4 py-2">
        {project.poolSize ? project.poolSize : 0}
      </td>
      <td className="w-24 overflow-hidden px-2 py-2 text-xs font-medium text-gray-900 dark:text-white">
        {project.responsibleMemberName}
      </td>
      <td className="w-16 overflow-hidden px-2 py-2 text-xs font-medium text-gray-900 dark:text-white">
        {parseLocalDateTime(project.createAt)}
      </td>

      <td className="px-4 py-2">
        <Dropdown
          inline
          arrowIcon={false}
          label={
            <>
              <svg
                className="h-5 w-5"
                aria-hidden
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z" />
              </svg>
            </>
          }
        >
          <Dropdown.Item onClick={() => openModal(project.id)}>
            프로젝트 설정 수정
          </Dropdown.Item>
          <Dropdown.Item
            className="text-red-500"
            onClick={() => onDeleteProject(project.id)}
          >
            프로젝트 삭제
          </Dropdown.Item>
        </Dropdown>
      </td>
    </tr>
  );
}
