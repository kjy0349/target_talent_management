'use client';

import { useState, useMemo, useEffect, useCallback } from "react"

import ProfileSummaryView from "@/components/profile/ProfileSummaryView"

import ProfileInfoDiv from "@/components/profile/detail/ProfileInfoDiv"
import MeetingInfoDiv from "@/components/profile/detail/MeetingInfoDiv"
import UsagePlanInfoDiv from "@/components/profile/detail/UsagePlanInfoDiv"
import RecruitInfoDiv from "@/components/profile/detail/RecruitnfoDiv";
import ProfessionInfoDiv from "@/components/profile/detail/ProfessionInfoDIv";
import { Button, Card, Checkbox, Textarea } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { Memo, ProfileDetail, Project } from "@/types/talent/ProfileDetailResponse";
import { HiPlus } from "react-icons/hi";
import CustomAddButton from "@/components/common/CustomAddButton";
import MemoSection from "@/components/profile/detail/MemoSection";
import Link from "next/link";
import RecommendSection from "@/components/profile/detail/RecommendSection";

const profileTabs = [
  {
    name: "profile",
    label: "주요 프로필"
  },
  {
    name: "profession",
    label: "special"
  },
  {
    name: "usage-plan",
    label: "활용계획"
  },
  {
    name: "meeting",
    label: "면담"
  },
  {
    name: "employment-history",
    label: "채용전형"
  }
];

export default function TalentDetailPage({ params }: { params: { slug: string } }) {
  const localAxios = useLocalAxios();
  const [profileDetail, setProfileDetail] = useState<ProfileDetail>();
  const [showAllProjects, setShowAllProjects] = useState(false);
  const [showAlltechmaps, setShowAlltechmaps] = useState(false);

  const fetchData = useCallback(async () => {
    try {
      const response = await localAxios.get<ProfileDetail>(`/profile/${params.slug}`);
      setProfileDetail({
        ...response.data,
        employmentHistories: response.data.employmentHistories.sort((a, b) => new Date(b.datetime).valueOf() - new Date(a.datetime).valueOf())
      });
    } catch (error) {
      console.error('Failed to fetch profile details:', error);
    }
  }, []);

  const createMemoHandler = async (content: string, isPrivate: boolean) => {
    const memoResponse = await localAxios.post<Memo>(`/profile/${params.slug}/memo`, {
      content,
      isPrivate
    });
    fetchData();
  };

  const deleteMemoHandler = async (id: number) => {
    localAxios.delete(`/profile/${params.slug}/memo/${id}`);
    fetchData();
  }

  const toggleShowAllProjects = () => {
    setShowAllProjects(!showAllProjects);
  }

  const toggleShowAlltechmaps = () => {
    setShowAlltechmaps(!showAlltechmaps);
  }

  useEffect(() => {
    fetchData();
    const recentString = localStorage.getItem("recentProfiles");
    let recent = recentString ? JSON.parse(recentString) : [];

    const index = recent.indexOf(parseInt(params.slug));
    if (index !== -1) {
      recent.splice(index, 1);
    }
    recent.push(parseInt(params.slug));

    if (recent.length > 5) {
      recent = recent.slice(recent.length - 5);
    }

    localStorage.setItem("recentProfiles", JSON.stringify(recent));
  }, [fetchData]);

  const [activeTab, setActiveTab] = useState('profile');

  const renderContent = () => {
    switch (activeTab) {
      case 'profile':
        if (profileDetail) {
          return <ProfileInfoDiv
            profile={profileDetail}
            params={params}
            reload={fetchData}
          />;
        }
        break;
      case 'profession':
        if (profileDetail) return <ProfessionInfoDiv profileId={profileDetail.profileId} />;
        break;
      case 'usage-plan':
        if (profileDetail) return <UsagePlanInfoDiv profileId={profileDetail.profileId} />;
        break;
      case 'meeting':
        if (profileDetail) return <MeetingInfoDiv profileId={profileDetail.profileId} />;
        break;
      case 'employment-history':
        if (profileDetail) return <RecruitInfoDiv profileId={profileDetail.profileId} />;
        break;
    }

    return undefined;
  };

  if (profileDetail) return (
    <div className="w-full p-10 bg-white flex gap-4">
      <div className="flex flex-col gap-4 flex-1">
        <ProfileSummaryView profile={profileDetail} />
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-2">
          <Card>
            <div className="flex flex-col justify-between gap-4">
              <p className="text-xl font-bold">총 {profileDetail.projectsPreview.length}개 프로젝트</p>
              {
                profileDetail.projectsPreview.length > 0
                  ? <>
                    <div className="flex flex-col gap-1">
                      {
                        showAllProjects
                          ? profileDetail.projectsPreview.map((project, index) => (
                            <Link key={index} className="text-lg font-bold text-blue-600"
                              href={"/project/" + project.projectId}>({project.targetYear}) {project.title}</Link>
                          ))
                          : profileDetail.projectsPreview.slice(0, 3).map((project, index) => (
                            <Link key={index} className="text-lg font-bold text-blue-600"
                              href={"/project/" + project.projectId}>({project.targetYear}) {project.title}</Link>
                          ))
                      }
                    </div>
                    <button className="font-bold text-blue-800 cursor-pointer" onClick={toggleShowAllProjects}>
                      {showAllProjects ? "프로젝트 숨기기" : "프로젝트 더보기"}
                    </button>
                  </>
                  : <div className="font-semibold mx-auto mt-1">속해있는 프로젝트가 없습니다.</div>
              }
            </div>
          </Card>
          <Card>
            <div className="flex flex-col justify-between gap-4">
              <p className="text-xl font-bold">총 {profileDetail.techmapsPreview.length}개 tech</p>
              {
                profileDetail.techmapsPreview.length > 0
                  ? <>
                    <div className="flex flex-col gap-1">
                      {
                        showAlltechmaps
                          ? profileDetail.techmapsPreview.map((techmap, index) => (
                            <Link key={index} className="text-lg font-bold text-blue-600"
                              href={"/techmap/" + techmap.techmapId}>
                              ({techmap.targetYear == -1 ? "-" : techmap.targetYear}, {techmap.targetDepartmentName}) {techmap.techMainCategoryName} - {techmap.techSubCategoryName} - {techmap.techDetailName}
                            </Link>
                          ))
                          : profileDetail.techmapsPreview.slice(0, 3).map((techmap, index) => (
                            <Link key={index} className="text-lg font-bold text-blue-600"
                              href={"/techmap/" + techmap.techmapId}>
                              ({techmap.targetYear == -1 ? "-" : techmap.targetYear}, {techmap.targetDepartmentName}) {techmap.techMainCategoryName} - {techmap.techSubCategoryName} - {techmap.techDetailName}
                            </Link>
                          ))
                      }
                    </div>
                    <button className="font-bold text-blue-800 cursor-pointer" onClick={toggleShowAlltechmaps}>
                      {showAlltechmaps ? "tech 숨기기" : "tech 더보기"}
                    </button>
                  </>
                  : <div className="font-semibold mx-auto mt-1">속해있는 tech이 없습니다.</div>
              }
            </div>
          </Card>
        </div>
        <header className="h-16 flex flex-row justify-around text-lg font-bold">
          {
            profileTabs.map(tab => (
              <button
                key={tab.name}
                className={"w-full hover:bg-gray-100 border-2 " + (activeTab === tab.name ? "bg-gray-200" : "bg-white")}
                onClick={() => {
                  setActiveTab(tab.name)
                }}
              >
                {tab.label}
              </button>
            ))
          }
        </header>
        <div>
          {renderContent()}
        </div>
      </div>
      <div className="flex flex-col gap-4 w-1/4">
        <Card className="bg-gray-50">
          <div className="flex flex-col justify-between">
            <div className="w-full flex justify-between items-center border-b-2 pb-2 mb-2">
              <p className="text-lg font-bold">Pool 관리 주요 히스토리</p>
              <span className="text-gray-700 ml-2">(최신순)</span>
            </div>
            <div>
              <ul className="leading-7 max-h-[300px] tracking-tighter overflow-y-auto">
                {
                  // TODO: History Section도 컴포넌트 분리
                  profileDetail.employmentHistories.length == 0
                  && <div className="w-full text-center font-bold pt-4 pb-2">등록된 히스토리가 존재하지 않습니다.</div>
                }
                {
                  profileDetail.employmentHistories.map((history, index) => (
                    <li key={index} className="pr-2 truncate">
                      <span className="font-bold mr-1">{history.type}</span>
                      {history.step && <span className="text-gray-700 text-sm mr-1">({history.step})</span>}
                      <span className="mr-1 text-md whitespace-pre-wrap">{history.contents.join(', ')}</span>
                      {history.datetime && <span
                        className="text-gray-700 text-sm">({new Date(history.datetime).toLocaleDateString()})</span>}
                    </li>
                  ))
                }
              </ul>
            </div>
          </div>
        </Card>
        <MemoSection
          data={profileDetail.memos}
          onCreate={createMemoHandler}
          onDelete={deleteMemoHandler}
        />
        <RecommendSection
          profileId={profileDetail.profileId}
        />
      </div>
    </div>
  )
}

