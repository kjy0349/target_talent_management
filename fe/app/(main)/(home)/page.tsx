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
import { useRouter } from "next/navigation";
import DashboardPageContent from "../dashboard/content";
import Spinner from "@/components/common/Spinner";
import { useDashbasordStore } from "@/stores/dashboard";
import { useCookies } from "react-cookie";

const HomePage = function () {
  const [showPopup, setShowPopup] = useState(false);
  const authStore = useAuthStore();
  const localAxios = useLocalAxios();
  const router = useRouter();
  const [content, setContent] = useState("");
  const dashboardStore = useDashbasordStore();
  const [cookies] = useCookies(["checked"]);
  const [popupClosed, setPopupClosed] = useState<boolean>(false);
  const fetchData = async () => {
    await localAxios.get("/admin/popup/show").then((Response) => {
      setContent(Response.data.content);
    });
  };

  useEffect(() => {
    if (authStore.authority) {
      if (!cookies.checked) {
        fetchData();
        if (!popupClosed) {
          setShowPopup(true);
        }
      }
    }
  }, [authStore]);

  const closePopup = () => {
    authStore.setIsCheckedPopup(true);
    setShowPopup(false);
    setPopupClosed(true);
  };

  //대시보드 추가
  const [mounted, setMounted] = useState(false);
  const [dashboardData, setDashboardData] = useState<Dashboard>();
  useEffect(() => {
    if (
      dashboardStore.getDashboard() != undefined &&
      authStore.id == dashboardStore.getMemberId()
    ) {
      console.log("dashboard exists!");
      const dashboard = dashboardStore.getDashboard();
      setDashboardData(dashboard);
      return;
    }
    baseSetting();
  }, []);
  useEffect(() => {
    if (
      dashboardData?.mainPageContent &&
      dashboardData.acquisitionOverview &&
      dashboardData.userSignupsThisWeek &&
      dashboardData.visitorsThisWeek
    ) {
      dashboardStore.setDashboard(dashboardData);
      dashboardStore.setMemberId(authStore.id ?? 0);
      setMounted(true);
    }
  }, [dashboardData]);
  const baseSetting = async () => {
    const mainPageContentResponse =
      await localAxios.get<DashboardMainContentFull>(`/dashboard/main`);
    // setDashboardData({});
    console.log(mainPageContentResponse);

    const departmentResponse =
      await localAxios.post<DashboardUserSignupsThisWeek>(
        `/dashboard/main/department`,
        {},
      );
    console.log(departmentResponse);

    const monthlyResponse = await localAxios.post<DashboardVisitorsThisWeek>(
      `/dashboard/main/monthly`,
      {
        createdYear: 2024,
      },
    );
    console.log(monthlyResponse);
    const now = new Date();
    const countryResponse =
      await localAxios.post<DashboardSessionsByCountryMap>(
        `/dashboard/main/country`,
        {
          viewYear: 2024,
          viewMonth: 4,
        },
      );
    console.log("country", countryResponse.data);
    setDashboardData({
      mainPageContent: mainPageContentResponse.data.mainPageContent,
      acquisitionOverview: mainPageContentResponse.data.acquisitionOverview,
      userSignupsThisWeek: departmentResponse.data,
      visitorsThisWeek: monthlyResponse.data,
      sessionsByCountryMap: countryResponse.data,
    });
  };
  if (dashboardData && mounted) {
    return (
      <div>
        <DashboardPageContent dashboard={dashboardData} />;
        <MainPopup
          isModalOpen={showPopup}
          content={content}
          onClose={closePopup}
        />
      </div>
    );
  } else {
    return (
      <div>
        <Spinner size={60} color="#ff6347" />
      </div>
    );
  }
};

export default HomePage;
