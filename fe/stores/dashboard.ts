import { Dashboard } from "@/types/dashboard/Dashboard";
import { create } from "zustand";

interface DashboardState {
  dashboard: Dashboard | undefined;
  isFetched: boolean;
  memberId: number;
  setDashboard: (dashboard: Dashboard) => void;
  clearDashboard: () => void;
  getDashboard: () => Dashboard | undefined;
  setMemberId: (id: number) => void;
  getMemberId: () => number;
  afterFetched: () => void;
}

const useDashbasordStore = create<DashboardState>((set, get) => ({
  dashboard: undefined, // 초기 상태 설정
  isFetched: false,
  memberId: 0,

  afterFetched: () => {
    set({ isFetched: true });
  },

  setDashboard: (dashboard: Dashboard) => {
    set({ dashboard: dashboard });
  },

  setMemberId: (id: number) => {
    set({ memberId: id });
  },

  getMemberId: () => {
    const { memberId } = get();
    return memberId;
  },

  clearDashboard: () => {
    set({ dashboard: undefined });
  },

  getDashboard: () => {
    const { dashboard } = get();
    return dashboard;
  },
}));

export { useDashbasordStore };
