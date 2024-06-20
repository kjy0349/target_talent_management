"use client";

import { ProfileDetail } from "@/types/talent/ProfileDetailResponse";
import { Badge, Card, Dropdown } from "flowbite-react";
import {
  HiDocumentReport,
  HiFolderAdd,
  HiLockClosed,
  HiOutlineDocumentReport,
  HiOutlinePlusCircle,
  HiOutlinePrinter,
  HiPlus,
  HiPlusCircle,
  HiRefresh,
  HiUser,
  HiUserAdd,
  HiUserRemove,
} from "react-icons/hi";
import { useEffect, useMemo, useState } from "react";
import dateString from "@/hooks/dateToString";
import { useLocalAxios } from "@/app/api/axios";
import Image from "next/image";
import { router } from "next/client";
import { redirect, useRouter } from "next/navigation";
import AddToProjectModal from "@/components/project/AddToProjectModal";
import ChangeManageModal from "@/components/profile/ChangeManageModal";
import useFilterStore from "@/stores/filter";

interface ProfileSummaryViewProps {
  className?: string;
  profile: ProfileDetail;
}

interface InteractionMenu {
  icon: JSX.Element;
  label: string;
  onClick?: () => void;
}

// TODO: 따로 분리해서 데이터를 관리해야 할 것 같다. 필터쪽에서도 이 데이터를 사용하는 것으로 알고있는데 동기화 해주자.
const employStatusSteps = [
  { label: "발굴", value: "FOUND" },
  { label: "접촉", value: "CONTACT" },
  { label: "활용도 검토", value: "USAGE_REVIEW" },
  { label: "중/장기 관리", value: "MID_LONG_TERM_MANAGE" },
  { label: "단기 관리", value: "SHORT_TERM_MANAGE" },
  { label: " 인터뷰", value: "인터뷰1" },
  { label: " 인터뷰", value: "인터뷰2" },
  { label: "interview3", value: "인터뷰3" },
  { label: "인터뷰 4", value: "for_interview" },
  { label: "처우협의", value: "NEGOTIATION" },
  { label: "처우결렬", value: "NEGOTIATION_DENIED" },
  { label: "입사포기", value: "EMPLOY_ABANDON" },
  { label: "입사대기", value: "EMPLOY_WAITING" },
  { label: "입사", value: "EMPLOYED" },
];

const ProfileSummaryView = (props: ProfileSummaryViewProps) => {
  const localAxios = useLocalAxios();
  const router = useRouter();
  const filterStore = useFilterStore();

  const [selectedStep, setSelectedStep] = useState<string>();
  const [showAddToProjectModal, setShowAddToProjectModal] = useState(false);
  const [showChangeManagerModal, setShowChangeManagerModal] = useState(false);

  const interactionDropdownMenus: InteractionMenu[] = [
    {
      icon: <HiPlus />,
      label: "프로젝트에 추가",
      onClick: () => {
        setShowAddToProjectModal(true);
      },
    },
    // {
    //   icon: <HiPlus />,
    //   label: "tech에 추가"
    // },
    // {
    //   icon: <HiUserAdd />,
    //   label: "네트워킹 담당자 추가",
    // },
    {
      icon: <HiRefresh />,
      label: "담당자 변경",
      onClick: () => {
        setShowChangeManagerModal(true);
      },
    },
    {
      icon: <HiUserRemove />,
      label: "프로필 삭제",
      onClick: async () => {
        if (confirm("해당 후보자를 삭제하겠습니까?")) {
          await localAxios
            .delete("/profile", {
              data: {
                profileIds: [props.profile.profileId],
              },
            })
            .then((Response) => {
              if (Response.status === 200) {
                alert("후보자를 삭제했습니다.");
                router.push("/talent");
              } else {
                alert("서버 오류");
              }
            });
        }
      },
    },
  ];

  useEffect(() => {
    if (props.profile.employStatus)
      setSelectedStep(
        employStatusSteps.find((x) => x.value == props.profile.employStatus)
          ?.label,
      );
  }, [props.profile.employStatus]);

  const column = (key: string) => {
    return props.profile?.columnDatas.find((x) => x.name == key)?.value;
  };

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

  const summaryColumns = useMemo(() => {
    const result = [];

    const column1 = column("column1");
    const column1 = column("column1");
    const foundPath = column("foundPath");
    const recommenderInfo = column("recommenderInfo");

    if (column1 && column1.trim().length > 0) result.push(column1);
    if (column1 && column1.trim().length > 0) result.push(column1);
    if (foundPath && foundPath.trim().length > 0) {
      if (recommenderInfo && recommenderInfo.trim().length > 0)
        result.push(`${foundPath} (${recommenderInfo})`);
      else result.push(foundPath);
    }

    return result;
  }, [props.profile]);

  const lastCareerSummary = useMemo(() => {
    const lastCareer = props.profile.careersDetail[0];

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
  }, [props.profile.careersDetail]);

  const lastEducationSummary = useMemo(() => {
    const lastEducation = props.profile.educationsDetail[0];

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
  }, [props.profile.educationsDetail]);

  const updateEmployStatus = async (e: string) => {
    const response = await localAxios.put(`/profile/status`, {
      profileIds: [props.profile.profileId],
      status: e,
    });
  };

  const addToProject = async (projectId: number) => {
    if (confirm("해당 프로젝트에 추가하시겠습니까?")) {
      await localAxios
        .put(`/project/profiles/${projectId}`, {
          projectProfiles: [props.profile.profileId],
        })
        .then((Response) => {
          if (Response.status === 200) {
            alert("프로젝트에 추가했습니다.");
            setShowAddToProjectModal(false);
          } else {
            alert("서버 오류");
          }
        });
    }
  };

  const changeManagement = async (memberId: number) => {
    if (confirm("담당자를 변경하시겠습니까?")) {
      await localAxios
        .put("/profile/manager", {
          profileIds: [props.profile.profileId],
          memberId: memberId,
        })
        .then((Response) => {
          if (Response.status === 200) {
            alert("담당자를 변경했습니다.");
            setShowChangeManagerModal(false);
          } else {
            alert("서버 오류");
          }
        });
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()}`;
  };

  return (
    <Card className={props.className}>
      <div className="flex w-full gap-6">
        <img
          className="size-48 rounded-sm border-2"
          src={
            props.profile.profileImage
              ? process.env.NEXT_PUBLIC_BASE_URL +
                "/" +
                props.profile.profileImage
              : "/assets/picture/profile.png"
          }
          alt="Profile Image"
        />
        <div className="flex w-full flex-col justify-between gap-4">
          <div className="flex flex-1 flex-col justify-between gap-1">
            <div className="flex flex-col gap-2">
              <h1 className="flex items-center gap-2 text-2xl font-bold">
                <span>
                  <span>{column("name")}</span>
                  {column("nameEng") ? (
                    <span> ({column("nameEng")})</span>
                  ) : (
                    <></>
                  )}
                </span>
                <div className="ml-2 flex gap-2">
                  {props.profile.isAllCompany && (
                    <Badge color="gray" size="xs" className="whitespace-nowrap">
                      전사
                    </Badge>
                  )}
                  <Badge color="blue" size="xs">
                    {props.profile.memberPreview.departmentName}
                  </Badge>
                  <Badge color="red" size="xs">
                    {props.profile.jobRankDetail?.description}
                  </Badge>
                </div>
                {props.profile.isPrivate && <HiLockClosed />}
              </h1>
              <div className="flex items-center gap-2 self-end text-3xl">
                <HiOutlineDocumentReport />
                <HiOutlinePrinter />
              </div>
            </div>
            <div className="-mt-5 text-gray-500">
              {summaryColumns.join(", ")}
            </div>
            <div>{lastCareerSummary}</div>
            <div>{lastEducationSummary}</div>
          </div>
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              <Dropdown label={selectedStep} color="blue" size="sm">
                {employStatusSteps.map((item) => (
                  <Dropdown.Item
                    key={item.value}
                    onClick={() => {
                      updateEmployStatus(item.value);
                      setSelectedStep(item.label);
                    }}
                  >
                    {item.label}
                  </Dropdown.Item>
                ))}
              </Dropdown>
              {props.profile.statusModifiedAt ? (
                <div className="text-md tracking-tighter text-gray-500">
                  ( 상태 변경일 :{" "}
                  {new Date(
                    props.profile.statusModifiedAt,
                  ).toLocaleDateString()}{" "}
                  )
                </div>
              ) : (
                <></>
              )}
            </div>
            <Dropdown
              label=""
              dismissOnClick={false}
              renderTrigger={() => (
                <div className="flex h-8 w-8 cursor-pointer items-center justify-center rounded-full border-2 border-gray-800 text-xl font-bold">
                  <HiPlus />
                </div>
              )}
            >
              {interactionDropdownMenus.map((item) => (
                <Dropdown.Item
                  key={item.label}
                  className="flex items-center gap-2"
                  onClick={item.onClick}
                >
                  <span>{item.icon}</span>
                  <span>{item.label}</span>
                </Dropdown.Item>
              ))}
            </Dropdown>
          </div>
        </div>
      </div>
      {props.profile.memberPreview.name ? (
        <div className="text-md mt-2 text-right tracking-tight text-black">
          <span>Pool 관리 담당 : </span>
          <span>{props.profile.memberPreview.departmentName} </span>
          <span>{props.profile.memberPreview.name} </span>
          <span>프로</span>
          <span> (최초등록일 : {formatDate(props.profile.createdAt)})</span>
        </div>
      ) : (
        <></>
      )}
      <AddToProjectModal
        showModal={showAddToProjectModal}
        closeModal={() => setShowAddToProjectModal(false)}
        addToProject={addToProject}
      />
      <ChangeManageModal
        showModal={showChangeManagerModal}
        closeModal={() => setShowChangeManagerModal(false)}
        changeManagement={changeManagement}
      />
    </Card>
  );
};

export default ProfileSummaryView;
