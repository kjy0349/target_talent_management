"use client";

import { useLocalAxios } from "@/app/api/axios";
import { MemberAdminSummary } from "@/types/admin/Member";
import { Avatar, Dropdown, theme } from "flowbite-react";
import Image from "next/image";
import { useEffect } from "react";
import { twMerge } from "tailwind-merge";
interface MemberListProp {
  member: MemberAdminSummary;
  deleteMember: (id: number) => void;
}
export default function MemberListRow({
  member,
  deleteMember,
}: MemberListProp) {
  const localAxios = useLocalAxios();
  const formatDate = (dateArray: Date) => {
    const date = new Date(dateArray);
    if (date.getFullYear() == 1970) {
      return "-";
    }
    return `${date.getFullYear()}년 ${date.getMonth()}월 ${date.getDay()}일 ${date.getHours()}시`;
  };

  const deleteIndividualMember = async () => {
    if (confirm("정말로 삭제하시겠습니까?")) {
      const result = await localAxios.delete(`/admin/member/${member.id}`);
      if (result.status == 200) {
        alert("삭제에 성공하였습니다.");
        deleteMember(member.id);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <>
      <div className="mb-4 flex w-full rounded-lg bg-white p-6 shadow-md hover:bg-slate-50 dark:bg-gray-800">
        <div className="flex w-full flex-row items-center justify-between">
          <Avatar
            alt="Profile"
            img={
              member.profileImage == null || member.profileImage == ""
                ? ""
                : `http://localhost:8080/${member.profileImage}`
            }
            size={"xl"}
            className="mx-8 rounded-full shadow-lg"
          />
          <div className="flex flex-col">
            <h5 className="mb-1 text-lg font-bold text-gray-900 dark:text-white">
              {`${member.name} (${member.knoxId})`}
            </h5>
            <div className="flex flex-col">
              <p className="mb-1 text-xs text-gray-500 dark:text-gray-400">
                {`${member.departmentName}`}
              </p>
              <p className="mb-1 text-xs text-gray-500 dark:text-gray-400">
                {`${member.telephone}`}
              </p>
              <p className="mb-1 text-xs text-gray-500 dark:text-gray-400">
                등록일: {`${formatDate(member.createdAt)}`}
              </p>
              <p className="text-xs text-gray-500 dark:text-gray-400">
                최종 접속일: {`${formatDate(member.lastAccessDate)}`}
              </p>
            </div>
          </div>
          <div className=" rounded-lg border border-gray-200 bg-white shadow dark:border-gray-700 dark:bg-gray-800">
            <div
              id="fullWidivhTabContent"
              className="border-t border-gray-200 dark:border-gray-600"
            >
              <div className="flex flex-col items-center gap-8 p-4 text-gray-900 dark:text-white">
                <div className="flex flex-row gap-4">
                  <div className="flex flex-col items-center justify-center">
                    <div className="mb-2 text-xl font-extrabold">
                      {member.visitCount ? member.visitCount : 0}
                    </div>
                    <div className="text-gray-500 dark:text-gray-400">
                      접속 횟수
                    </div>
                  </div>
                  <div className="flex flex-col items-center justify-center">
                    <div className="mb-2 text-xl font-extrabold">
                      {member.isSecuritySigned ? "동의함" : "비동의"}
                    </div>
                    <div className="text-gray-500 dark:text-gray-400">
                      보안 서약서 동의
                    </div>
                  </div>
                </div>
                <div className="flex flex-row gap-4">
                  <div className="flex flex-col items-center justify-center">
                    <div className="mb-2 text-xl font-extrabold">
                      {member.managePoolSize}
                    </div>
                    <div className="text-gray-500 dark:text-gray-400">
                      관리 인재 수
                    </div>
                  </div>
                  <div className="flex flex-col items-center justify-center">
                    <div className="mb-2 text-xl font-extrabold">
                      {member.completedNetworks}
                    </div>
                    <div className="text-gray-500 dark:text-gray-400">
                      네트워킹 매핑 완료 수
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="">
            <Dropdown
              inline
              label={
                <>
                  <svg
                    className="h-6 w-6 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-white"
                    aria-hidden
                    fill="currentColor"
                    viewBox="0 0 20 20"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z" />
                  </svg>
                </>
              }
              theme={{
                arrowIcon: "hidden",
                floating: {
                  base: twMerge(theme.dropdown.floating.base, "w-48"),
                },
              }}
            >
              <Dropdown.Item>수정</Dropdown.Item>
              <Dropdown.Item onClick={deleteIndividualMember}>
                삭제
              </Dropdown.Item>
              <Dropdown.Item>알림 보내기</Dropdown.Item>
            </Dropdown>
          </div>
        </div>
      </div>
    </>
  );
}
