"use client";

import { useLocalAxios } from "@/app/api/axios";
import { useAuthStore } from "@/stores/auth";
import { ProjectSummary } from "@/types/admin/Project";
import { Badge, Dropdown, Table } from "flowbite-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { HiLockClosed, HiLockOpen } from "react-icons/hi";
import { twMerge } from "tailwind-merge";

interface ProjectListRowProps {
  project: ProjectSummary;
  onClick?: () => void;
  clickable?: boolean
}

export default function ProjectListRowSimple({
  project, clickable, onClick
}: ProjectListRowProps) {
  const router = useRouter();

  const parseLocalDateTime = (date: Date): string => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}.${newDate.getMonth()}.${newDate.getDay()}`;
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
  const auth = useAuthStore();
  return (
    <tr
      className={"whitespace-nowrap border-b text-xs hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700" + clickable ? " cursor-pointer" : ""}
      style={{ height: "40px" }}
      onClick={onClick}
    >
      <td
        scope="row"
        className="flex w-32 cursor-pointer items-center overflow-hidden px-4 py-2 font-medium text-gray-900 hover:underline dark:text-white"
      >
        {auth.authLevel == 1 &&
          (project.isPrivate ? <HiLockClosed /> : <HiLockOpen />)}
        {project.title}
      </td>
      <td className="overflow-hidden px-4 py-2">{project.targetYear}</td>
      <td className="w-24 overflow-hidden px-4 py-2">
        {project.departmentName}
      </td>
      <td className="w-32 overflow-hidden px-4 py-2">
        {getProjectTypeName(project.projectType)}
      </td>
      <td className="w-16 overflow-hidden px-4 py-2">
        {project.poolSize ? project.poolSize : 0}
      </td>
      <td className="w-24 overflow-hidden px-4 py-2 text-xs font-medium text-gray-900 dark:text-white">
        {project.responsibleMemberName}
      </td>
      <td className="w-16 overflow-hidden px-4 py-2 text-xs font-medium text-gray-900 dark:text-white">
        {parseLocalDateTime(project.createAt)}
      </td>
    </tr>
  );
}
