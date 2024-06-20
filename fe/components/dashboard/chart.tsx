"use client";
import { DashboardPageData } from "@/app/(main)/dashboard/page";
import { useLocalAxios } from "@/app/api/axios";
import {
  Dashboard,
  DashboardMainContent,
  DashboardMainContentFull,
} from "@/types/dashboard/Dashboard";
import dynamic from "next/dynamic";
import type { ComponentProps } from "react";
import { useEffect, useState } from "react";

const ApexChart = dynamic(() => import("react-apexcharts"), { ssr: false });

function Chart(props: ComponentProps<typeof ApexChart>) {
  return <ApexChart {...props} />;
}

export default Chart;
