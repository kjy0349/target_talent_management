"use client";

import Image from "next/image";
import { useRouter } from "next/navigation";
import { Dropdown } from "flowbite-react";
import { twMerge } from "tailwind-merge";
import { theme } from "flowbite-react";
import ProfileCardProp from "@/types/talent/ProfileCardProp";

export default function ProfileAdminListCard({
  Prop,
}: {
  Prop: ProfileCardProp;
}) {
  const router = useRouter();

  return (
    <div className="mb-4 flex items-center justify-between rounded-lg bg-white p-4 shadow-md">
      <div className="flex items-center">
        <Image
          alt="Profile"
          height="64"
          src="/assets/picture/profile.png"
          width="64"
          className="mr-4 cursor-pointer rounded-full shadow-lg"
          onClick={() => router.push(`/talent/${Prop.profileId}`)}
        />
        <div>
          <h5 className="text-lg font-bold text-gray-900">
            {Prop.columnDatas.name}
          </h5>
          <div className="mt-1 text-sm text-gray-500">
            {Prop.careersPreview.length > 0 && (
              <p>
                {Prop.careersPreview[0].companyName},{" "}
                {Prop.careersPreview[0].role} (
                <span>
                  {new Date(Prop.careersPreview[0].startedAt).getFullYear()} -{" "}
                  {new Date(Prop.careersPreview[0].endedAt).getFullYear()}
                </span>
                )
              </p>
            )}
            {Prop.educationsPreview.length > 0 && (
              <p>
                {Prop.educationsPreview[0].schoolName},{" "}
                {Prop.educationsPreview[0].major} (
                <span>
                  {new Date(Prop.educationsPreview[0].enteredAt).getFullYear()}{" "}
                  -{" "}
                  {new Date(
                    Prop.educationsPreview[0].graduatedAt,
                  ).getFullYear()}
                </span>
                )
              </p>
            )}
            {Prop.keywordsPreview.length > 0 && (
              <p>
                {Prop.keywordsPreview.map((keyword, index) => (
                  <span key={index}>{keyword.data}, </span>
                ))}
              </p>
            )}
          </div>
        </div>
      </div>
      <div className="ml-4 hidden flex-col gap-2 whitespace-nowrap md:flex">
        <div>
          <span className="font-semibold">네트워킹:</span>{" "}
          <span className="text-sm">김상성 직급4 (네트워킹 횟수 {3}회)</span>
        </div>
        <div>
          <span className="font-semibold">소속 프로젝트:</span>{" "}
          <span className="text-sm">{Prop.projectsPreview.length}개</span>
        </div>
        <div>
          <span className="font-semibold">담당자:</span>{" "}
          <span className="text-sm">
            {Prop.memberPreview?.departmentName} / {Prop.memberPreview?.name}
          </span>
        </div>
      </div>
      <Dropdown
        inline
        label={
          <svg
            className="h-5 w-5 text-gray-500"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path d="M6 10a2 2 0 11-4 0 2 2 0 014 0zM12 10a2 2 0 11-4 0 2 2 0 014 0zM16 12a2 2 0 100-4 2 2 0 000 4z" />
          </svg>
        }
        theme={{
          arrowIcon: "hidden",
          floating: {
            base: twMerge(theme.dropdown.floating.base, "w-40"),
          },
        }}
      >
        <Dropdown.Item>네트워크 담당자 편집</Dropdown.Item>
        <Dropdown.Item>PDF로 저장</Dropdown.Item>
        <Dropdown.Item className="text-red-500">프로필 삭제</Dropdown.Item>
      </Dropdown>
    </div>
  );
}
