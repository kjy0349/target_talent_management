"use client";

import { Button, Pagination, TextInput, theme } from "flowbite-react";
import { useState, useEffect } from "react";
import ProfileListCard from "../../../components/profile/ProfileListRow";
import { HiSearch } from "react-icons/hi";
import { useLocalAxios } from "@/app/api/axios";
import ProfileCardProp from "@/types/talent/ProfileCardProp";

export default function AdminTalentPage() {
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState(1);
  const localAxios = useLocalAxios();
  const [profiles, setProfiles] = useState<ProfileCardProp[]>([]);

  const fetchData = async () => {
    const response = await localAxios.post(`/profile/search`, {
      searchString: "",
      column2: [
        // "string",
      ],
      jobRanks: [
        // "string"
      ],
      companyNames: [
        // "string"
      ],
      degrees: [
        // "pPollSize"
      ],
      schoolNames: [
        // "string"
      ],
      departmentNames: [
        // "string"
      ],
      founderNames: [
        // "string"
      ],
      careerMinYear: 0,
      careerMaxYear: 3000,
      techSkillKeywords: [
        // "string"
      ],
      graduateMinYear: 0,
      graduateMaxYear: 3000,
      employStatuses: ["FOUND"],
      profileKeywords: [
        // "string"
      ],
    });
    setProfiles(response.data.content);
    setTotalPage(response.data.totalPages);
  };

  useEffect(() => {
    fetchData();
  }, [currentPage]);

  return (
    <div className="relative overflow-x-auto bg-white p-10">
      <div className="flex flex-col space-y-5 p-5">
        <h3 className="text-center text-3xl font-bold">프로필 관리 현황</h3>
      </div>

      <div className="mt-2 flex justify-center">
        <div className="relative mb-4 w-1/2 px-2">
          <TextInput
            icon={() => (
              <HiSearch className="h-4 w-full text-gray-500 dark:text-gray-400" />
            )}
            id="default-search"
            name="default-search"
            placeholder="검색어를 입력하세요"
            required
            type="search"
            className="h-10 w-full"
          />
          <Button
            type="submit"
            className="bprder-2 absolute inset-y-0 right-0 h-10 rounded-l-none rounded-r-lg bg-primary"
          >
            검색
          </Button>
        </div>
      </div>

      <div>
        {profiles.length === 0 && (
          <div className="h-[50vh]">해당하는 인재가 존재하지 않습니다</div>
        )}
        {profiles.map((profile) => (
          <ProfileListCard key={profile.profileId} Prop={profile} />
        ))}
      </div>
      <div className="mb-5 mt-2 flex w-full justify-center">
        <Pagination
          currentPage={currentPage}
          nextLabel=""
          onPageChange={(page) => setCurrentPage(page)}
          previousLabel=""
          showIcons
          totalPages={totalPage}
        />
      </div>
    </div>
  );
}
