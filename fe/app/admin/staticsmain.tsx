"use client";

// import {
//   Avatar,
//   Badge,
//   Button,
//   Checkbox,
//   Datepicker,
//   Dropdown,
//   Label,
//   Pagination,
//   Radio,
//   Table,
//   TextInput,
//   Tooltip,
//   // theme,
// } from "flowbite-react";

import ApexChart from "@/components/dashboard/chart";
import { FC, useEffect, useRef, useState } from "react";
import { HiSearch } from "react-icons/hi";
import { twMerge } from "tailwind-merge";
import Link from "next/link";
import {
  CartesianGrid,
  DefaultTooltipContent,
  Line,
  LineChart,
  XAxis,
  YAxis,
} from "recharts";
import { Chart } from "../../components/admin/Chart";
import { MemberUsageChange } from "@/types/admin/Member";
import { useLocalAxios } from "../api/axios";
import { DashboardPageData } from "../(main)/dashboard/page";
import { AdminDashboard, Dashboard } from "@/types/dashboard/Dashboard";
import { useThemeMode } from "flowbite-react";
import Spinner from "@/components/common/Spinner";
// import { useReactTable, flexRender, ColumnDef } from "@tanstack/react-table";
export interface AdminDashboardPageData {
  dashboard: AdminDashboard;
}
export function StaticsMain() {
  const [currentPage, setCurrentPage] = useState(1);
  const isLoaded = useRef<boolean>(false);
  const [dailyStatics, setDailyStatics] = useState<MemberUsageChange>();
  const [dashboard, setDashboard] = useState<AdminDashboard>();
  const localAxios = useLocalAxios();

  useEffect(() => {
    baseSetting();
  }, []);

  const baseSetting = async () => {
    const staticResponse = await localAxios.get(`/admin/member/report`);
    setDailyStatics(staticResponse.data);

    const dailyGraphResponse = await localAxios.get<AdminDashboard>(
      `/admin/member/report/static`,
    );
    const cwidthProp = document.querySelector("#stand");

    if (cwidthProp) {
      setCwidth(cwidthProp.clientWidth);
    } else {
      setCwidth(1200);
    }

    setDashboard({
      salesThisWeek: dailyGraphResponse.data.salesThisWeek,
      cwidth: cwidth,
    });
  };
  const [cwidth, setCwidth] = useState<number>(0);
  useEffect(() => {
    if (dashboard?.salesThisWeek) {
      isLoaded.current = true;
    }
  }, [dashboard]);

  return (
    <section className="w-full py-3">
      <div className=" mx-auto max-w-screen-2xl">
        <table className="w-full border  text-center text-sm text-gray-500 dark:text-gray-400 rtl:text-right">
          <thead className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th
                scope="col"
                className="border px-6 py-3"
                rowSpan={2}
                colSpan={2}
              >
                구분
              </th>
              <th scope="col" className="border px-6 py-3" rowSpan={2}>
                Total
              </th>

              <th scope="col" className="border px-6 py-3" colSpan={3}>
                증가 내역
              </th>
            </tr>
            <tr>
              <th scope="col" className="border px-6 py-3">
                전일 대비
              </th>
              <th scope="col" className="border px-6 py-3">
                전주 대비
              </th>
              <th scope="col" className="border px-6 py-3">
                전월 대비
              </th>
            </tr>
          </thead>
          <tbody>
            <tr className="border-b bg-white hover:bg-gray-50 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-600">
              <td
                scope="row"
                className="whitespace-nowrap border px-6 py-4 font-medium text-gray-900 dark:text-white"
                rowSpan={2}
              >
                방문 현황
              </td>
              <td className="border px-6 py-4">방문 횟수</td>
              <td className="border px-6 py-4">{dailyStatics?.totalCount}</td>
              <td className="border px-6 py-4">
                {dailyStatics?.countChangeYesterday}
              </td>
              <td className="border px-6 py-4">
                {dailyStatics?.countChangeLastWeek}
              </td>
              <td className="border px-6 py-4">
                {dailyStatics?.countChangeLastMonth}
              </td>
            </tr>
            <tr className="border-b bg-white hover:bg-gray-50 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-600">
              <td className="border px-6 py-4">방문자 수</td>
              <td className="border px-6 py-4">
                {dailyStatics?.totalVisitorCount}
              </td>
              <td className="border px-6 py-4">
                {dailyStatics?.visitorChangeYesterday}
              </td>
              <td className="border px-6 py-4">
                {dailyStatics?.visitorChangeLastWeek}
              </td>
              <td className="border px-6 py-4">
                {dailyStatics?.visitorChangeLastMonth}
              </td>
            </tr>

            <tr className="border-b bg-white hover:bg-gray-50 dark:border-gray-700 dark:bg-gray-800 dark:hover:bg-gray-600">
              <td
                scope="row"
                className="whitespace-nowrap border px-6 py-4 font-medium text-gray-900 dark:text-white"
              >
                멤버 수
              </td>
              <td className="border px-6 py-4">Total</td>
              <td className="border px-6 py-4" colSpan={4}>
                {dailyStatics?.totalMembers}
              </td>
            </tr>
          </tbody>
        </table>

        <nav
          className="flex flex-col items-start justify-between space-y-3 p-4 md:flex-row md:items-center md:space-y-0"
          aria-label="Table navigation"
        ></nav>
        <hr></hr>
        <div
          id="stand"
          className="border-b border-gray-200 text-center text-sm font-medium text-gray-500 dark:border-gray-700 dark:text-gray-400"
        >
          <ul className="-mb-px flex flex-wrap">
            {/* <li className="me-2">
              <div className="inline-block rounded-t-lg border-b-2 border-transparent p-4 hover:border-gray-300 hover:text-gray-600 dark:hover:text-gray-300">
                일간
              </div>
            </li> */}
            <li className="ms-2">
              <div className="active inline-block rounded-t-lg border-b-2 border-blue-600 p-4 text-blue-600 dark:border-blue-500 dark:text-blue-500">
                주간
              </div>
            </li>
            {/* <li className="me-2">
              <div className="inline-block rounded-t-lg border-b-2 border-transparent p-4 hover:border-gray-300 hover:text-gray-600 dark:hover:text-gray-300">
                월간
              </div>
            </li> */}
          </ul>
        </div>

        <div className="flex w-full flex-row justify-center">
          {/* <div className="flex w-1/2 flex-col p-3">
            <div className="w-full max-w-sm rounded-lg border border-gray-200 bg-white p-4 shadow dark:border-gray-700 dark:bg-gray-800 sm:p-6">
              <h5 className="mb-3 text-base font-semibold text-gray-900 dark:text-white md:text-xl">
                사용률( 일 평균 )
              </h5>
              <p className="text-sm font-normal text-gray-500 dark:text-gray-400">
                사용률(당일 방문자수 / 당일 총 회원수) * 100
              </p>

              <Chart />

              <div>
                <a
                  href="#"
                  className="inline-flex items-center text-xs font-normal text-gray-500 hover:underline dark:text-gray-400"
                >
                  <svg
                    className="me-2 h-3 w-3"
                    aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 20 20"
                  >
                    <path
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M7.529 7.988a2.502 2.502 0 0 1 5 .191A2.441 2.441 0 0 1 10 10.582V12m-.01 3.008H10M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
                    />
                  </svg>
                  Workspace 회원중 얼마나 방문하는지에 대한 지표
                </a>
              </div>
            </div>
          </div>
          <div className="flex w-1/2 flex-col p-3">
            <div className="w-full max-w-sm rounded-lg border border-gray-200 bg-white p-4 shadow dark:border-gray-700 dark:bg-gray-800 sm:p-6">
              <h5 className="mb-3 text-base font-semibold text-gray-900 dark:text-white md:text-xl">
                사용 빈도( 일 평균 )
              </h5>
              <p className="text-sm font-normal text-gray-500 dark:text-gray-400">
                사용 빈도(당일 접속 횟수 / 당일 총 방문자 수) * 100
              </p>

              <Chart />

              <div>
                <a
                  href="#"
                  className="inline-flex items-center text-xs font-normal text-gray-500 hover:underline dark:text-gray-400"
                >
                  <svg
                    className="me-2 h-3 w-3"
                    aria-hidden="true"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 20 20"
                  >
                    <path
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M7.529 7.988a2.502 2.502 0 0 1 5 .191A2.441 2.441 0 0 1 10 10.582V12m-.01 3.008H10M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"
                    />
                  </svg>
                  방문한 회원마다 얼마나 사용하였는지에 대한 지표
                </a>
              </div>
            </div>
          </div> */}
          {dashboard?.salesThisWeek ? (
            <SalesThisWeek dashboard={dashboard} />
          ) : (
            <div>
              <Spinner size={60} color="#ff6347" />
            </div>
          )}
        </div>
      </div>
    </section>
  );
}

const SalesThisWeek: FC<AdminDashboardPageData> = function ({ dashboard }) {
  return (
    <div className="rounded-lg bg-white shadow dark:bg-gray-800 ">
      <div className="mb-4 flex items-center justify-between">
        <div className="p-5">
          <span className="text-2xl font-bold leading-none text-gray-900 dark:text-white sm:text-3xl">
            사용량 통계
          </span>
        </div>
        <div className="flex flex-1 items-center justify-end text-base font-bold text-green-500 dark:text-green-400"></div>
      </div>
      <SalesApexChart dashboard={dashboard} />
    </div>
  );
};

const SalesApexChart: FC<AdminDashboardPageData> = function ({ dashboard }) {
  const { mode } = useThemeMode();
  const isDarkTheme = mode === "dark";

  const borderColor = isDarkTheme ? "#374151" : "#F3F4F6";
  const labelColor = isDarkTheme ? "#9ca3af" : "#6B7280";
  const opacityFrom = isDarkTheme ? 0 : 0.45;
  const opacityTo = isDarkTheme ? 0.15 : 0;

  const options: ApexCharts.ApexOptions = {
    stroke: {
      curve: "smooth",
    },
    chart: {
      type: "area",
      fontFamily: "Inter, sans-serif",
      foreColor: labelColor,
      toolbar: {
        show: false,
      },
    },
    fill: {
      type: "gradient",
      gradient: {
        opacityFrom,
        opacityTo,
        type: "vertical",
      },
    },
    dataLabels: {
      enabled: false,
    },
    tooltip: {
      style: {
        fontSize: "14px",
        fontFamily: "Inter, sans-serif",
      },
    },
    grid: {
      show: true,
      borderColor: borderColor,
      strokeDashArray: 1,
      padding: {
        left: 35,
        bottom: 15,
      },
    },
    markers: {
      size: 5,
      strokeColors: "#ffffff",
      hover: {
        size: undefined,
        sizeOffset: 3,
      },
    },
    xaxis: {
      categories: dashboard.salesThisWeek.categories,
      labels: {
        style: {
          colors: [labelColor],
          fontSize: "14px",
          fontWeight: 500,
        },
      },
      axisBorder: {
        color: borderColor,
      },
      axisTicks: {
        color: borderColor,
      },
      crosshairs: {
        show: true,
        position: "back",
        stroke: {
          color: borderColor,
          width: 1,
          dashArray: 10,
        },
      },
    },
    yaxis: {
      labels: {
        style: {
          colors: [labelColor],
          fontSize: "14px",
          fontWeight: 500,
        },
        formatter: function (value) {
          return Math.floor(value) + "%";
        },
      },
    },
    legend: {
      fontSize: "14px",
      fontWeight: 500,
      fontFamily: "Inter, sans-serif",
      labels: {
        colors: [labelColor],
      },
      itemMargin: {
        horizontal: 10,
      },
    },
    responsive: [
      {
        breakpoint: 1024,
        options: {
          xaxis: {
            labels: {
              show: false,
            },
          },
        },
      },
    ],
  };
  const series = dashboard.salesThisWeek.series;

  return (
    <div className="">
      <ApexChart
        height={420}
        width={1200}
        options={options}
        series={series}
        type="area"
      />
    </div>
  );
};
