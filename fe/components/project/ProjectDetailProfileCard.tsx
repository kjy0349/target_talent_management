"use client";

import Image from "next/image";
import {
  HiPlus,
  HiChevronRight,
  HiChevronUp,
  HiChevronLeft,
  HiAnnotation,
} from "react-icons/hi";
import { useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import ProfileCardProp, {
  ProjectProfileCardProp,
} from "@/types/talent/ProfileCardProp";
import dateString from "@/hooks/dateToString";
import { Checkbox, Dropdown, DropdownItem, Label } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import Link from "next/link";

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

export default function ProjectDetailProfileCard({
  props,
}: {
  props: ProjectProfileCardProp;
}) {
  const [career, setCareer] = useState<any[]>(props.careersPreview);
  const foldable = career.length > 2;
  const [selectedStep, setSelectedStep] = useState<string>(() => {
    const matchedStep = employStatusSteps.find(
      (step) => step.value === props.employStatus,
    );
    return matchedStep ? matchedStep.label : "";
  });
  const localAxios = useLocalAxios();
  const [showAllCareer, setShowAllCareer] = useState(false);
  const router = useRouter();
  const translateEdu = (edu: string) => {
    switch (edu) {
      case "BACHELOR":
        return "학";
      case "MASTER":
        return "석";
      case "pPollSize":
        return "박";
    }
  };

  const summaryColumns = useMemo(() => {
    return [
      props.columnDatas?.column1,
      props.columnDatas?.foundPath,
      props.columnDatas?.country,
    ];
  }, [props]);

  const lastCareerSummary = useMemo(() => {
    const lastCareer = props.careersPreview[0];

    if (lastCareer) return lastCareer.role + ", " + lastCareer.companyName;
  }, [props.careersPreview]);

  const lastEducationSummary = useMemo(() => {
    const lastEducation = props.educationsPreview[0];

    if (lastEducation)
      return (
        lastEducation.schoolName +
        " " +
        lastEducation.major +
        " " +
        translateEdu(lastEducation.degree) +
        "사 "
      );
  }, [props.educationsPreview]);

  const updateEmployStatus = async (e: string) => {
    await localAxios.put(`/profile/${props.profileId}/status`, {
      status: e,
      profileIds: [props.profileId],
    });
    if (props.handleReload) {
      props.handleReload();
    }
  };

  return (
    <div className="relative flex w-full flex-row justify-end border bg-white p-10 shadow-md">
      <Checkbox
        className="mr-2 mt-4"
        checked={props.checked}
        onChange={props.handleCheck}
      />
      <div className="flex w-full flex-col">
        <div className="mx-2 flex flex-row items-center gap-4">
          <img
            alt="Profile"
            src={
              props.profileImage
                ? process.env.NEXT_PUBLIC_BASE_URL + "/" + props.profileImage
                : "/assets/picture/profile.png"
            }
            className="my-4 h-40 w-36 shadow-lg"
          />
          <div
            className="flex cursor-pointer flex-col self-center whitespace-nowrap text-left align-middle "
            onClick={() => router.push("/talent/" + props.profileId)}
          >
            {/* 이름 */}
            <p className="mb-1 text-xl font-bold text-gray-900">
              {props.columnDatas.name}
              {/* 강프로 */}
              <span className="mx-2 text-xl text-gray-600 dark:text-white">
                (
                {props.columnDatas?.nameEng && props.columnDatas.nameEng + ", "}
                {props.columnDatas?.column1 &&
                  props.columnDatas?.column1.substring(0, 4)}
                년생)
              </span>
            </p>
            {/* 포지션  */}
            <p className="mb-1 text-sm text-gray-900">
              {props.careersPreview.length > 0 && lastCareerSummary}
              {props.educationsPreview.length > 0 &&
                " / " + lastEducationSummary}
            </p>
            {/* 상세정보 */}
            <p className="mb-1 text-sm text-gray-900">
              {summaryColumns.join(", ")}
            </p>

            <div className="whitespace-nowrap">
              <Link
                href={`/talent/${props.profileId}`}
                className="text-sm font-bold"
              >
                상세보기
                <HiChevronRight className="inline-block" />
              </Link>
            </div>
          </div>
        </div>

        {/* 경력 */}
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500">
          <div>
            <h1 className="font-semibold text-black">경력</h1>
          </div>
          <div>
            {career &&
              career
                .slice(0, showAllCareer ? career.length : 3)
                .map((career, index) => (
                  <p key={index}>
                    <span>
                      {career.companyName}, {career.role} (
                      <span>{dateString(career.startedAt)}</span> ~{" "}
                      <span>{dateString(career.endedAt)}</span>)
                    </span>
                  </p>
                ))}
            {foldable && !showAllCareer && (
              <button onClick={() => setShowAllCareer(true)}>
                <span className="font-bold">
                  더 보기(
                  {career.length - 3})
                </span>
                <HiChevronRight className="inline-block" />{" "}
              </button>
            )}
            {foldable && showAllCareer && (
              <button onClick={() => setShowAllCareer(false)}>
                <span className="font-bold">숨기기</span>{" "}
                <HiChevronUp className="inline-block" />
              </button>
            )}
          </div>
        </div>
        {/* 학력 */}
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500">
          <div>
            <h1 className="font-semibold text-black">학력</h1>
          </div>
          <div>
            {props.educationsPreview.map((education, index) => (
              <p key={index}>
                <span>
                  {translateEdu(education.degree)}) {education.schoolName},{" "}
                  {education.major}
                  <span>
                    ({dateString(education.enteredAt)} ~{" "}
                    {dateString(education.graduatedAt)})
                  </span>
                </span>
              </p>
            ))}
          </div>
        </div>
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500">
          <div>
            <h1 className="font-semibold text-black">special</h1>
          </div>
          {props.specializationDetails?.map((keyword, index) => (
            <p key={index}>{keyword.specialPoint}</p>
          ))}
        </div>

        {/* 활용부서*/}
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500 dark:text-gray-400">
          <div>
            <h1 className="font-semibold text-black">활용부서</h1>
          </div>
          <div>
            {props.usagePlans &&
              props.usagePlans?.map((u, index) => (
                <p key={index}>{u.targetDepartmentName + ", " + u?.jobRank}</p>
              ))}
            {/* MX사업부 개발실 XXX그룹 */}
          </div>
        </div>

        {/* 네트워킹*/}
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500 dark:text-gray-400">
          <div>
            <h1 className="font-semibold text-black">네트워킹</h1>
          </div>
          <div>
            {props.networking?.map((network, index) => (
              <span key={index} className="mr-1">
                <span>{network.executiveName}</span>
                {props.networking && index === props.networking.length - 1
                  ? null
                  : ", "}
              </span>
            ))}
            ( 네트워킹 활동
            {" " + props.networking?.length || 0}회 )
            {/* MX사업부 개발실 XXX 부직급1 (네트워킹 활동 2회) */}
          </div>
        </div>

        <div className="absolute bottom-10 right-10 text-right text-sm tracking-tighter text-black">
          <p>
            ( 상태 변경일 :{" "}
            {new Date(props.statusModifiedAt).toLocaleDateString()} )
          </p>
          <p>
            채용 담당 :{" "}
            {props.memberPreview.name +
              " / " +
              props.memberPreview.departmentName}
          </p>
        </div>
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500 dark:text-gray-400">
          <div>
            <h1 className="font-semibold text-black">기술분야</h1>
          </div>
          <div>
            <p>
              {props.columnDatas.skillMainCategory
                ? props.columnDatas.skillMainCategory
                : ""}
              {props.columnDatas.skillSubCategory
                ? " > " + props.columnDatas.skillSubCategory
                : ""}
              {props.columnDatas.skillDetail
                ? " > " + props.columnDatas.skillDetail
                : ""}
            </p>
          </div>
        </div>
      </div>
      <div className="my-10 flex flex-col">
        <div className="flex flex-row items-center gap-4">
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              <Dropdown
                label={<p className="whitespace-nowrap">{selectedStep}</p>}
                color="blue"
                size="sm"
              >
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
              <Dropdown
                label={
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-6 w-6"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"
                    />
                  </svg>
                }
                inline={true}
                arrowIcon={false}
              >
                <DropdownItem
                  onClick={() => {
                    if (props.handleOnAddingNetworking) {
                      props.handleOnAddingNetworking(props.profileId);
                    }
                  }}
                >
                  네트워킹 담당자 추가
                </DropdownItem>
                <DropdownItem
                  onClick={() => {
                    if (props.handleOnDeleteProject) {
                      props.handleOnDeleteProject(props.profileId);
                    }
                  }}
                >
                  프로젝트에서 프로필 삭제
                </DropdownItem>
                <DropdownItem
                  onClick={() => {
                    if (props.handleOnCopyOtherProject) {
                      props.handleOnCopyOtherProject(props.profileId);
                    }
                  }}
                >
                  다른 프로젝트로 복사
                </DropdownItem>
                {/* <DropdownItem onClick={() => console.log("미구현")}>
                  프로필 PDF 저장
                </DropdownItem> */}
              </Dropdown>
            </div>
          </div>
        </div>
        <div className="whitespace-nowrap text-right text-sm tracking-tighter text-black">
          <p>
            ( 상태 변경일 :{" "}
            {new Date(props.statusModifiedAt).toLocaleDateString()} )
          </p>
          <p>
            채용 담당 :{" "}
            {props.memberPreview.name +
              " / " +
              props.memberPreview.departmentName}
          </p>
        </div>
      </div>
    </div>
  );
}
