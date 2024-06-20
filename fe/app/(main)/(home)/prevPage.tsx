"use client";
import ApexChart from "@/components/dashboard/chart";
import RecentProfiles from "@/components/main/RecentProfiles";
import RecentProjects from "@/components/main/RecentProjects";
import { useAuthStore } from "@/stores/auth";
import { FC, useEffect, useState } from "react";
import MainPopup from "@/components/common/MainPopup";
import { useLocalAxios } from "@/app/api/axios";
import { Badge, Select, useThemeMode } from "flowbite-react";
import { DashboardPageData } from "../dashboard/page";
import {
  Dashboard,
  DashboardMainContentFull,
  DashboardMonthlySearchConditionDto,
  DashboardSessionsByCountryMap,
  DashboardUserSignupsThisWeek,
  DashboardVisitorsThisWeek,
} from "@/types/dashboard/Dashboard";
import { DepartmentAdminSummary } from "@/types/admin/Member";

const HomePage = function () {
  const [showPopup, setShowPopup] = useState(false);
  const authStore = useAuthStore();
  const localAxios = useLocalAxios();
  const [content, setContent] = useState("");
  const fetchData = async () => {
    await localAxios.get("/admin/popup/show").then((Response) => {
      setContent(Response.data.content);
    });
  };

  useEffect(() => {
    if (authStore.authority) {
      if (!authStore.isCheckedPopup) {
        fetchData();
        setShowPopup(true);
        baseSetting();
      }
    }
  }, [authStore]);

  const closePopup = () => {
    setShowPopup(false);
    authStore.setIsCheckedPopup(true);
  };
  const [allPoolSize, setAllPoolSize] = useState<number>(0);
  const [dashBoardState, setDashboardState] = useState<Dashboard>();
  const [networkingPoolSize, setNetworkingPoolSize] = useState<number>(0);
  const baseSetting = async () => {
    const size = await localAxios.get<number>(`/dashboard/sum-pool-size`);
    setAllPoolSize(size.data);
    const mainPageContentResponse =
      await localAxios.get<DashboardMainContentFull>(`/dashboard/main`);
    // setDashboardData({});
    // console.log(mainPageContentResponse);

    const departmentResponse =
      await localAxios.post<DashboardUserSignupsThisWeek>(
        `/dashboard/main/department`,
        {},
      );
    // console.log(departmentResponse);

    const monthlyResponse = await localAxios.post<DashboardVisitorsThisWeek>(
      `/dashboard/main/monthly`,
      {
        createdYear: 2024,
      },
    );
    // console.log(monthlyResponse);
    const now = new Date();

    const countryResponse =
      await localAxios.post<DashboardSessionsByCountryMap>(
        `/dashboard/main/country`,
        {
          viewYear: 2024,
          viewMonth: 4,
        },
      );
    // console.log("country", countryResponse.data);
    setDashboardState({
      mainPageContent: mainPageContentResponse.data.mainPageContent,
      acquisitionOverview: mainPageContentResponse.data.acquisitionOverview,
      userSignupsThisWeek: departmentResponse.data,
      visitorsThisWeek: monthlyResponse.data,
      sessionsByCountryMap: countryResponse.data,
    });

    const networkingResponse = await localAxios.get(`/dashboard/networking`);
    setNetworkingPoolSize(networkingResponse.data);
  };

  return (
    <div className="bg-white">
      <div className="flex min-h-[calc(100vh-136px)] flex-col space-y-5 p-5">
        {/* <h3 className="text-3xl font-bold text-center my-5">인재 Pool 관리 현황</h3> */}
        <div className="flex h-72 space-x-5">
          <div className="flex-1 border bg-white ">
            <div className=" h-full w-full">
              <div className="flex h-full items-center justify-center rounded-lg border border-gray-200 bg-white p-6 text-center shadow hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
                <div className="">
                  <p className="font-normal text-gray-700 dark:text-gray-400">
                    인재 POOL 누적 인원 수
                  </p>
                  <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
                    {allPoolSize}명
                  </h5>
                </div>
              </div>
            </div>
          </div>
          <div className="flex-1 border bg-white">
            <div className=" rounded-lg border border-gray-200 bg-white p-6 text-center shadow hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
              <div className="flex items-center">
                <div className="flex w-full justify-between">
                  <span className="text-xl font-bold leading-none text-gray-900 dark:text-white">
                    {/* {dashboard.visitorsThisWeek.visitors} */}
                    월별 인재 Pool 발굴 현황
                  </span>
                </div>
                {/* <div className="m-5 flex w-0 flex-1 items-center justify-end text-base font-bold text-green-500 dark:text-green-400">
          {dashboard.visitorsThisWeek.percentage * 100}%
          <svg
            className="h-5 w-5"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M5.293 7.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 5.414V17a1 1 0 11-2 0V5.414L6.707 7.707a1 1 0 01-1.414 0z"
              clipRule="evenodd"
            />
          </svg>
        </div> */}
              </div>
              <div className="flex w-full justify-center">
                {dashBoardState &&
                  dashBoardState.visitorsThisWeek.labels.length > 0 ? (
                  <VisitorsApexChart dashboard={dashBoardState} />
                ) : (
                  <div className="h-[218px]">데이터가 없습니다.</div>
                )}
              </div>

              {/* <div className="mt-3.5 flex items-center justify-between border-t border-gray-200 pt-3 dark:border-gray-700 sm:pt-6">
        <DateRangeDropdown />
        <div className="shrink-0">
          <Link
            href="#"
            className="text-primary-700 dark:text-primary-500 inline-flex items-center rounded-lg p-2 text-xs font-medium uppercase hover:bg-gray-100 dark:hover:bg-gray-700 sm:text-sm"
          >
            Visits Report
            <svg
              className="ml-1 h-4 w-4 sm:h-5 sm:w-5"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M9 5l7 7-7 7"
              />
            </svg>
          </Link>
        </div>
      </div> */}
            </div>
          </div>

          <div className="flex-1 border bg-white ">
            <div className=" h-full w-full">
              <div className="flex h-full items-center justify-center rounded-lg border border-gray-200 bg-white p-6 text-center shadow hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
                <div className="">
                  <p className="font-normal text-gray-700 dark:text-gray-400">
                    네트워킹 현황
                  </p>
                  <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
                    {networkingPoolSize}명
                  </h5>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="flex h-72 space-x-5">
          <RecentProfiles />
          <RecentProjects />
        </div>
      </div>
      <MainPopup
        isModalOpen={showPopup}
        content={content}
        onClose={closePopup}
      />
    </div>
  );
};

export default HomePage;

const VisitorsThisWeek: FC<DashboardPageData> = function ({ dashboard }) {
  const now = new Date().getFullYear();
  const serverStartDate = 2024;
  const [dashBoardState, setDashboardState] = useState<Dashboard>({
    ...dashboard,
  });

  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>();

  const [monthlySearchCondition, setMonthlySearchCondition] =
    useState<DashboardMonthlySearchConditionDto>({
      createdYear: now,
      departmentId: 0,
    });
  const localAxios = useLocalAxios();
  useEffect(() => {
    baseSetting();
  }, []);
  const baseSetting = async () => {
    const departmentResponse =
      await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);

    setDepartments(departmentResponse.data);
  };
  const generateYearOptions = (startYear: number, endYear: number) => {
    const options = [];
    for (let year = startYear; year <= endYear; year++) {
      options.push(
        <option key={year} value={year}>
          {year}
        </option>,
      );
    }
    return options;
  };

  useEffect(() => {
    // console.log(monthlySearchCondition);
  }, [monthlySearchCondition]);
  const searchMonthlyStatics = async (
    searchCondition: DashboardMonthlySearchConditionDto,
  ) => {
    const monthlyStaticsResponse = await localAxios.post(
      `/dashboard/main/monthly`,

      searchCondition,
    );

    // console.log(
    //   "condition",
    //   searchCondition,
    //   "data ",
    //   monthlyStaticsResponse.data,
    // );

    setDashboardState({
      ...dashboard,
      visitorsThisWeek: monthlyStaticsResponse.data,
    });
  };

  const handleOnChangeCreateYear = (
    e: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    const nextCondition = {
      ...monthlySearchCondition,
      createdYear: Number(e.target.value),
    };
    setMonthlySearchCondition(nextCondition);
    searchMonthlyStatics(nextCondition);
  };

  const handleOnChangeDepartmentId = (
    e: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    const nextCondition = {
      ...monthlySearchCondition,
      departmentId: Number(e.target.value),
    };
    setMonthlySearchCondition(nextCondition);
    searchMonthlyStatics(nextCondition);
  };
  return (
    <div className=" rounded-lg border border-gray-200 bg-white p-6 text-center shadow hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-700">
      <div className="flex items-center">
        <div className="flex w-full justify-between">
          <span className="text-xl font-bold leading-none text-gray-900 dark:text-white">
            {/* {dashboard.visitorsThisWeek.visitors} */}
            월별 인재 Pool 발굴 현황
          </span>
          <div className="flex justify-end gap-2">
            <div className="flex items-center gap-1">
              <Badge className="h-10">연도</Badge>
              <Select
                value={monthlySearchCondition.createdYear}
                onChange={handleOnChangeCreateYear}
              >
                {now && now > 0 && generateYearOptions(serverStartDate, now)}
              </Select>
            </div>
            <div className="flex items-center gap-1">
              <Badge className="h-10">사업부</Badge>
              <Select
                value={monthlySearchCondition.departmentId}
                onChange={handleOnChangeDepartmentId}
              >
                <option value={0}>사업부 전체</option>
                {departments &&
                  departments.map((d) => (
                    <option key={d.departmentId} value={d.departmentId}>
                      {d.name}
                    </option>
                  ))}
              </Select>
            </div>
          </div>
        </div>
        {/* <div className="m-5 flex w-0 flex-1 items-center justify-end text-base font-bold text-green-500 dark:text-green-400">
          {dashboard.visitorsThisWeek.percentage * 100}%
          <svg
            className="h-5 w-5"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M5.293 7.707a1 1 0 010-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 01-1.414 1.414L11 5.414V17a1 1 0 11-2 0V5.414L6.707 7.707a1 1 0 01-1.414 0z"
              clipRule="evenodd"
            />
          </svg>
        </div> */}
      </div>
      <div className="flex w-full justify-center">
        {dashBoardState.visitorsThisWeek.labels.length > 0 ? (
          <VisitorsApexChart dashboard={dashBoardState} />
        ) : (
          <div>데이터가 없습니다.</div>
        )}
      </div>

      {/* <div className="mt-3.5 flex items-center justify-between border-t border-gray-200 pt-3 dark:border-gray-700 sm:pt-6">
        <DateRangeDropdown />
        <div className="shrink-0">
          <Link
            href="#"
            className="text-primary-700 dark:text-primary-500 inline-flex items-center rounded-lg p-2 text-xs font-medium uppercase hover:bg-gray-100 dark:hover:bg-gray-700 sm:text-sm"
          >
            Visits Report
            <svg
              className="ml-1 h-4 w-4 sm:h-5 sm:w-5"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M9 5l7 7-7 7"
              />
            </svg>
          </Link>
        </div>
      </div> */}
    </div>
  );
};

const VisitorsApexChart: FC<DashboardPageData> = function ({ dashboard }) {
  const { mode } = useThemeMode();
  const isDarkTheme = mode === "dark";

  const fillGradientShade = isDarkTheme ? "dark" : "light";
  const fillGradientShadeIntensity = isDarkTheme ? 0.45 : 1;

  const options: ApexCharts.ApexOptions = {
    labels: dashboard.visitorsThisWeek.labels,
    chart: {
      fontFamily: "Inter, sans-serif",
      sparkline: {
        enabled: true,
      },
      toolbar: {
        show: false,
      },
    },
    fill: {
      type: "gradient",
      gradient: {
        shade: fillGradientShade,
        shadeIntensity: fillGradientShadeIntensity,
      },
    },
    plotOptions: {
      area: {
        fillTo: "end",
      },
    },
    theme: {
      monochrome: {
        enabled: true,
        color: "#1A56DB",
      },
    },
    tooltip: {
      style: {
        fontSize: "14px",
        fontFamily: "Inter, sans-serif",
      },
    },
  };
  const series = dashboard.visitorsThisWeek.series;

  return (
    <ApexChart
      width={400}
      height={217}
      options={options}
      series={series}
      type="area"
    />
  );
};
