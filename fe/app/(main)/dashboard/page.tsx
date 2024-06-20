"use client";
import dashboard from "@/data/dashboard.json";
import {
  Dashboard,
  DashboardMainContent,
  DashboardMainContentFull,
  DashboardSessionsByCountryMap,
  DashboardUserSignupsThisWeek,
  DashboardVisitorsThisWeek,
} from "@/types/dashboard/Dashboard";
import DashboardPageContent from "./content";
import { useEffect, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import Spinner from "@/components/common/Spinner";

export interface DashboardPageData {
  dashboard: Dashboard;
}

// async function getData() {
//   return { dashboard } as DashboardPageData;
// }

export default function UsersListPage() {
  const [mounted, setMounted] = useState(false);
  const [dashboardData, setDashboardData] = useState<Dashboard>();
  useEffect(() => {
    baseSetting();
  }, []);
  const localAxios = useLocalAxios();
  useEffect(() => {
    if (
      dashboardData?.mainPageContent &&
      dashboardData.acquisitionOverview &&
      dashboardData.userSignupsThisWeek &&
      dashboardData.visitorsThisWeek
    ) {
      setMounted(true);
    }
  }, [dashboardData]);
  const baseSetting = async () => {
    const mainPageContentResponse =
      await localAxios.get<DashboardMainContentFull>(`/dashboard/main`);
    // setDashboardData({});

    const departmentResponse =
      await localAxios.post<DashboardUserSignupsThisWeek>(
        `/dashboard/main/department`,
        {},
      );

    const monthlyResponse = await localAxios.post<DashboardVisitorsThisWeek>(
      `/dashboard/main/monthly`,
      {
        createdYear: 2024,
      },
    );
    const now = new Date();
    const countryResponse =
      await localAxios.post<DashboardSessionsByCountryMap>(
        `/dashboard/main/country`,
        {
          viewYear: 2024,
          viewMonth: 4,
        },
      );
    setDashboardData({
      mainPageContent: mainPageContentResponse.data.mainPageContent,
      acquisitionOverview: mainPageContentResponse.data.acquisitionOverview,
      userSignupsThisWeek: departmentResponse.data,
      visitorsThisWeek: monthlyResponse.data,
      sessionsByCountryMap: countryResponse.data,
    });
  };

  // return <DashboardPageContent {...await getData()} />;
  if (dashboardData && mounted) {
    return <DashboardPageContent dashboard={dashboardData} />;
  } else {
    return (
      <div>
        <Spinner size={60} color="#ff6347" />
      </div>
    );
  }
}
