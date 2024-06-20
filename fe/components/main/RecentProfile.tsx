"use client";

import { Card } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { useCallback, useEffect, useMemo, useState } from "react";
import { ProfileDetail } from "@/types/talent/ProfileDetailResponse";
import { useRouter } from "next/navigation";

interface props {
  id: number;
}

const formatDegree = (degreeString: string) => {
  switch (degreeString) {
    case "pPollSize":
      return "박사";
    case "MASTER":
      return "석사";
    case "BACHELOR":
      return "학사";
  }
};

export default function RecentProfile({ id }: props) {
  const localAxios = useLocalAxios();
  const [profileDetail, setProfileDetail] = useState<ProfileDetail>();
  const column = (key: string) => {
    return profileDetail?.columnDatas.find((x) => x.name == key)?.value;
  };

  const fetchData = useCallback(async () => {
    try {
      const response = await localAxios.get<ProfileDetail>(`/profile/${id}`);
      setProfileDetail({
        ...response.data,
        employmentHistories: response.data.employmentHistories.sort(
          (a, b) =>
            new Date(b.datetime).valueOf() - new Date(a.datetime).valueOf(),
        ),
      });
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, []);

  const lastCareerSummary = useMemo(() => {
    const lastCareer = profileDetail?.careersDetail[0];

    if (lastCareer)
      return (
        lastCareer.companyName +
        ", " +
        lastCareer.role +
        " (" +
        (lastCareer.startedAt
          ? new Date(lastCareer.startedAt).getFullYear()
          : "현재") +
        " ~ " +
        (lastCareer.endedAt
          ? new Date(lastCareer.endedAt).getFullYear()
          : "현재") +
        ")"
      );
  }, [profileDetail?.careersDetail]);

  const lastEducationSummary = useMemo(() => {
    const lastEducation = profileDetail?.educationsDetail[0];

    if (lastEducation)
      return (
        lastEducation.school.schoolName +
        ", " +
        lastEducation.major +
        ", " +
        formatDegree(lastEducation.degree) +
        " (" +
        (lastEducation.enteredAt
          ? new Date(lastEducation.enteredAt).getFullYear()
          : "현재") +
        " ~ " +
        (lastEducation.graduatedAt
          ? new Date(lastEducation.graduatedAt).getFullYear()
          : "현재") +
        ")"
      );
  }, [profileDetail?.educationsDetail]);

  const summaryColumns = useMemo(() => {
    return [
      column("column1"),
      column("column1") + "년생",
      column("foundPath"),
      column("country"),
    ];
  }, [profileDetail?.columnDatas]);

  const router = useRouter();

  return (
    <Card
      onClick={() => router.push(`talent/${id}`)}
      className="mx-5 cursor-pointer"
    >
      <div className="flex w-full gap-6">
        <img
          className="size-24 rounded-sm border-2"
          src={
            profileDetail?.profileImage
              ? profileDetail?.profileImage
              : "/assets/picture/profile.png"
          }
          alt="Profile Image"
        />
        <div className="flex w-full flex-col justify-between gap-4">
          <div className="flex flex-1 flex-col justify-between gap-1">
            <div className="flex items-center justify-between">
              <h1 className="flex items-center gap-2 text-xl font-bold">
                <span>{column("name")}</span>
                {column("nameEng") ? <span>({column("nameEng")})</span> : <></>}
              </h1>
            </div>
            <div className="text-gray-500">{summaryColumns.join(", ")}</div>
            <div>{lastCareerSummary}</div>
            <div>{lastEducationSummary}</div>
          </div>
        </div>
      </div>
      {profileDetail?.memberPreview.name ? (
        <span className="text-sm tracking-tight text-gray-500">
          <span>Pool 관리 담당 : </span>
          <span>{profileDetail.memberPreview.departmentName} </span>
          <span>{profileDetail.memberPreview.name} </span>
          <span>프로</span>
        </span>
      ) : (
        <></>
      )}
    </Card>
  );
}
