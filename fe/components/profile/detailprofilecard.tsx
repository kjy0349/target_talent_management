"use client";

import Image from "next/image";
import { HiChevronLeft, HiChevronRight, HiChevronUp } from "react-icons/hi";
import { useState, useMemo, useEffect } from "react";
import { useRouter } from "next/navigation";
import ProfileCardProp from "@/types/talent/ProfileCardProp";
import { Checkbox, Dropdown, Label } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";

const employStatusSteps = [
  { label: "발굴", value: "FOUND" },
  { label: "접촉", value: "CONTACT" },
  { label: "활용도 검토", value: "USAGE_REVIEW" },
  { label: "중/장기 관리", value: "MID_LONG_TERM_MANAGE" },
  { label: "단기 관리", value: "SHORT_TERM_MANAGE" },
  { label: "인터뷰1", value: "인터뷰1" },
  { label: "인터뷰2", value: "인터뷰2" },
  { label: "인터뷰3", value: "인터뷰3" },
  { label: "인터뷰4", value: "for_interview" },
  { label: "처우협의", value: "NEGOTIATION" },
  { label: "처우결렬", value: "NEGOTIATION_DENIED" },
  { label: "입사포기", value: "EMPLOY_ABANDON" },
  { label: "입사대기", value: "EMPLOY_WAITING" },
  { label: "입사", value: "EMPLOYED" },
];

export default function DetailProfileCard(props: ProfileCardProp) {
  const [career, setCareer] = useState<any[]>(props.careersPreview);
  const foldable = career.length >= 2;
  const [selectedStep, setSelectedStep] = useState<string>("");
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
    const columns = [
      props.columnDatas?.column1,
      props.columnDatas?.foundPath,
      props.columnDatas?.country,
    ];
    return columns.filter(Boolean);
  }, [props]);

  const lastCareerSummary = useMemo(() => {
    const lastCareer = props.careersPreview[0];

    if (lastCareer) {
      return lastCareer.companyName ? ", " + lastCareer.companyName : "";
      // lastCareer.role
      //     ? lastCareer.role
      //     : "" +
    }
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
    const oldStatus = props.employStatus;
    await localAxios
      .put(`/profile/status`, {
        status: e,
        profileIds: [props.profileId],
      })
      .then(() => {
        if (props.handleOnUpdateDetail) {
          props.handleOnUpdateDetail(oldStatus, e);
        }
      });
  };

  const getPropsStatusIndex = (e: string) => {
    return employStatusSteps.findIndex((step) => step.value === e);
  };

  const getCurrentStatusIndex = (e: string) => {
    return employStatusSteps.findIndex((step) => step.label === e);
  };

  const handlePrevEmployStatus = async () => {
    const currentIndex = getCurrentStatusIndex(selectedStep);
    if (currentIndex > 0) {
      const prevStatus = employStatusSteps[currentIndex - 1].label;
      setSelectedStep(prevStatus);
      updateEmployStatus(employStatusSteps[currentIndex - 1].value);
    }
  };

  const handleNextEmployStatus = async () => {
    const currentIndex = getCurrentStatusIndex(selectedStep);
    if (currentIndex >= 0 && currentIndex < employStatusSteps.length - 1) {
      const nextStatus = employStatusSteps[currentIndex + 1].label;
      setSelectedStep(nextStatus);
      updateEmployStatus(employStatusSteps[currentIndex + 1].value);
    }
  };

  useEffect(() => {
    const index = getPropsStatusIndex(props.employStatus);
    if (index !== -1) {
      setSelectedStep(employStatusSteps[index].label);
    }
  }, [props.employStatus]);

  return (
    <div className="relative flex w-full min-w-[60vw] max-w-[60vw] flex-row justify-end border bg-white p-10 shadow-md">
      <Checkbox
        className="mr-2 mt-4"
        checked={props.checked}
        onChange={props.handleCheck}
      />
      <div className="flex w-full flex-col">
        <div className="mx-2 flex flex-row items-start gap-4">
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
            className="flex cursor-pointer flex-col self-center whitespace-nowrap text-left align-middle"
            onClick={() => router.push("/talent/" + props.profileId)}
          >
            {/* 이름 */}
            <p className="mb-1 whitespace-nowrap text-xl font-bold text-gray-900">
              {props.columnDatas.name}
              {/* 강프로 */}
              <span className="mx-2 text-xl text-gray-600 dark:text-white">
                (
                {props.columnDatas?.nameEng && props.columnDatas.nameEng + ", "}
                {props.columnDatas?.column1 &&
                  +props.columnDatas.column1 + "년생"}
                )
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
              <p className="text-sm font-bold">
                상세보기
                <HiChevronRight className="inline-block" />
              </p>
            </div>
          </div>
        </div>
        {/* 경력 */}
        <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500">
          <div className="whitespace-nowrap">
            <h1 className="font-semibold text-black">경력</h1>
          </div>
          <div>
            {career
              .slice(0, showAllCareer ? career.length : 2)
              .map((career, index) => (
                <p key={index}>
                  <span>
                    {career.companyName}, {career.role} (
                    <span>{new Date(career.startedAt).getFullYear()}</span> ~{" "}
                    <span>
                      {career.endedAt
                        ? new Date(career.endedAt).getFullYear()
                        : "현재"}
                    </span>
                    )
                  </span>
                </p>
              ))}
            {foldable && !showAllCareer && (
              <button onClick={() => setShowAllCareer(true)}>
                <span className="font-bold">
                  더 보기(
                  {career.length - 2})
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
          <div className="whitespace-nowrap">
            <h1 className="font-semibold text-black">학력</h1>
          </div>
          <div>
            {props.educationsPreview.map((education, index) => (
              <p key={index}>
                <span>
                  {translateEdu(education.degree)}) {education.schoolName},{" "}
                  {education.major}
                  <span>
                    ({new Date(education.enteredAt).getFullYear()} ~{" "}
                    {new Date(education.graduatedAt).getFullYear()})
                  </span>
                </span>
              </p>
            ))}
          </div>
        </div>
        {/* <div className="mb-4 flex flex-row gap-4 text-sm text-gray-500">
          <div className="whitespace-nowrap">
            <h1 className="font-semibold text-black">special</h1>
          </div>
          {props.specializationDetails?.map((keyword, index) => (
            <p key={index}>
              {keyword.specialPoint}{" "}
            </p>
          ))}
        </div> */}
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
          <div className="whitespace-nowrap">
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
        </div>{" "}
        {/* 네트워킹*/}
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
        <div className="flex flex-row items-center gap-4 text-sm text-gray-500 dark:text-gray-400">
          <div className="whitespace-nowrap">
            <h1 className="font-semibold text-black">현황</h1>
          </div>
          <div>
            <Dropdown
              inline
              label={
                <span className="text-primary hover:underline">{`소속 프로젝트 ${props.projectsPreview.length}개`}</span>
              }
              color="white"
              theme={{
                content: "m-0 h-fit p-0 text-sm",
                inlineWrapper: "m-0 h-fit p-0",
                floating: {
                  base: "m-0 p-0",
                  content: "m-0 h-fit p-0",
                },
              }}
              arrowIcon={false}
              placement="top"
            >
              {props.projectsPreview.length > 0 &&
                props.projectsPreview.map((project) => (
                  // TODO : ID 받아서 링크
                  <Dropdown.Item
                    key={project.projectId}
                    onClick={() => router.push(`/projects/${project.title}`)}
                    className="z-10"
                  >
                    {project.title}
                  </Dropdown.Item>
                ))}
            </Dropdown>
            <Dropdown
              inline
              label={
                <span className="ml-5 text-primary hover:underline">{`소속 tech ${props.techmapsPreview.length}개`}</span>
              }
              color="white"
              theme={{
                content: "m-0 h-fit p-0 text-sm",
                inlineWrapper: "m-0 h-fit p-0",
                floating: {
                  base: "m-0 p-0",
                  content: "m-0 h-fit p-0",
                },
              }}
              arrowIcon={false}
              placement="top"
            >
              {props.techmapsPreview.length > 0 &&
                props.techmapsPreview.map((techmap) => (
                  // TODO : ID 받아서 링크
                  <Dropdown.Item
                    key={techmap.techmapId}
                    onClick={() =>
                      router.push(`/projects/${techmap.techmapId}`)
                    }
                    className="z-10"
                  >
                    {"(" +
                      techmap.targetYear +
                      ") " +
                      techmap.techMainCategoryName +
                      " > " +
                      techmap.techSubCategoryName +
                      " > " +
                      techmap.techDetailName}
                  </Dropdown.Item>
                ))}
            </Dropdown>
          </div>
        </div>
      </div>
      <div className="my-10 flex flex-col">
        <div className="flex flex-row items-center gap-4">
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center">
              <HiChevronLeft
                className="size-6 cursor-pointer"
                onClick={handlePrevEmployStatus}
              />
              <Dropdown
                label={<p className="whitespace-nowrap">{selectedStep}</p>}
                color="blue"
                size="sm"
                arrowIcon={false}
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
              <HiChevronRight
                className="size-6 cursor-pointer"
                onClick={handleNextEmployStatus}
              />
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
