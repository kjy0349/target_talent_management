"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  ProfileAdminFilterGraphFull,
  ProfileAdminFilterSearch,
} from "@/types/admin/Profile";
import { useEffect, useState } from "react";
import { HiChevronDown, HiPlus, HiX } from "react-icons/hi";
import {
  Area,
  AreaChart,
  ReferenceArea,
  ResponsiveContainer,
  XAxis,
  YAxis,
} from "recharts";
type ProfileGrapPollSizeata = {
  date: string;
  count: number;
};

interface ProfileAdminFilterProps {
  isFilterSidebarOpen: boolean;
}
const ProfileAdminFilter = ({
  isFilterSidebarOpen,
}: ProfileAdminFilterProps) => {
  const localAxios = useLocalAxios();

  const baseSetting = async () => {
    const grapPollSizeataResponse =
      await localAxios.get<ProfileAdminFilterGraphFull>(
        `/admin/profile/filter/graph`,
      );
    setProfileCountData(
      Object.entries(grapPollSizeataResponse.data.grapPollSizeata).map(
        ([date, count]) => ({
          date,
          count,
        }),
      ),
    );
  };

  useEffect(() => {
    baseSetting();
  }, []);
  const [profileCountData, setProfileCountData] =
    useState<ProfileGrapPollSizeata[]>();
  const reverseDate = (selectedDate: string | undefined) => {
    if (selectedDate == undefined) return null;
    const parts = selectedDate.split("-");
    const year = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1;
    const date = new Date(year, month);
    return date;
  };
  const [filters, setFilters] = useState<ProfileAdminFilterSearch>({
    names: "",
    column2: [],
    jobRanks: [],
    companyNames: [],
    degrees: [],
    schoolNames: [],
    majors: [],
    keywords: [],
    employStatuses: [],
    foundDeptids: [],
    founders: [],
    networkingResponsibleMembers: [],
    careerStartedAt: null,
    careerEndedAt: null,
    graduatedAt: null,
  });
  const [selectedRange, setSelectedRange] = useState<
    { startDate: string; endDate: string } | undefined
  >(undefined);

  useEffect(() => {
    const nextFilters = {
      ...filters,
      careerStartedAt: reverseDate(selectedRange?.startDate),
      careerEndedAt: reverseDate(selectedRange?.endDate),
    };
    setFilters(nextFilters);
  }, [selectedRange]);

  const handleStartDateChange = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const index = parseInt(event.target.value, 10);
    if (profileCountData) {
      if (profileCountData.length > 0) {
        setSelectedRange((prev) => ({
          ...prev!,
          startDate: profileCountData[index].date,
        }));
      }
    }
  };

  const handleEndDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const index = parseInt(event.target.value, 10);
    if (profileCountData) {
      if (profileCountData.length > 0) {
        setSelectedRange((prev) => ({
          ...prev!,
          endDate: profileCountData[index].date,
        }));
      }
    }
  };
  const toggleAccordion = (id: string) => {
    const accordionContent = document.getElementById(id);
    const accordionIcon = document.getElementById(
      `accordion-icon-${id.replace("accordion-collapse-", "")}`,
    );

    if (accordionContent && accordionIcon) {
      accordionContent.classList.toggle("hidden");
      accordionIcon.classList.toggle("rotate-180");
    }
  };
  return (
    <>
      <div className="fixed inset-y-0 right-0 z-50 h-full w-1/3 transform overflow-y-auto bg-white p-6 shadow-xl transition-transform">
        <h3 className="mb-4 text-xl font-bold">필터 보기</h3>
        <div id="accordion-filters">
          <div className="mb-4">
            <div
              id={`accordion-heading-column2`}
              className="flex items-center justify-between border border-gray-200 bg-gray-100 p-4 dark:border-gray-700 dark:bg-gray-800"
            >
              <h2 className="text-lg font-medium text-gray-900 dark:text-white">
                column1
              </h2>
              <button
                type="button"
                className="flex items-center justify-center rounded-full bg-gray-200 p-2 text-gray-500 hover:bg-gray-300 focus:outline-none dark:bg-gray-700 dark:text-gray-400 dark:hover:bg-gray-600"
                onClick={() => toggleAccordion(`accordion-collapse-column2`)}
              >
                <HiChevronDown
                  id={`accordion-icon-column2`}
                  className="h-5 w-5 transition-transform"
                />
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default ProfileAdminFilter;
