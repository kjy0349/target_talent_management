import { create } from 'zustand';

import ProfileCardProp from "@/types/talent/ProfileCardProp";
import Filters from "@/types/talent/Filter";
import ProfileGrapPollSizeata from "@/types/talent/ProfileGrapPollSizeata";

interface StoreState {
  currentPage: number;
  setCurrentPage: (page: number) => void;
  pageSize: number;
  setPageSize: (size: number) => void;
  totalPage: number;
  setTotalPage: (page: number) => void;
  isMine: boolean;
  setIsMine: (isMine: boolean) => void;
  isDept: boolean;
  setIsDept: (isMine: boolean) => void;
  profiles: ProfileCardProp[];
  setProfiles: (profiles: ProfileCardProp[]) => void;
  careerGrapPollSizeata: ProfileGrapPollSizeata[];
  setCareerGrapPollSizeata: (data: ProfileGrapPollSizeata[]) => void;
  graduateGrapPollSizeata: ProfileGrapPollSizeata[];
  setGraduateGrapPollSizeata: (data: ProfileGrapPollSizeata[]) => void;
  selectedEmployee: string[];
  setSelectedEmployee: (employees: string[]) => void;
  selectedIds: number[];
  setSelectedIds: (ids: number[]) => void;
  departmentNames: string[];
  setDepartmentNames: (departmentNames: string[]) => void;
  filterResults: Filters;
  setFilterResults: (key: string, selectedItems: string[]) => void;
  careerMinYear?: number;
  setCareerMinYear: (careerMinYear?: number) => void;
  careerMaxYear?: number;
  setCareerMaxYear: (careerMaxYear?: number) => void;
  graduateMinYear?: number;
  setGraduateMinYear: (graduateMinYear?: number) => void;
  graduateMaxYear?: number;
  setGraduateMaxYear: (graduateMaxYear?: number) => void;
  sort: string;
  setSort: (sort: string) => void;
  isActiveSelected: boolean;
  setIsActiveSelected: (isActiveSelected: boolean) => void;
  isInactiveSelected: boolean;
  setIsInactiveSelected: (isInactiveSelected: boolean) => void;
  careerRange: number[];
  setCareerRange: (careerRange: number[]) => void;
  graduateRange: number[];
  setGraduateRange: (graduateRange: number[]) => void;
  clearFilter: () => void;
}

const useFilterStore = create<StoreState>()(
  // persist(
  (set) => ({
    currentPage: 1,
    setCurrentPage: (page) => set({ currentPage: page }),
    pageSize: 10,
    setPageSize: (size) => set({ pageSize: size }),
    totalPage: 0,
    setTotalPage: (page) => set({ totalPage: page }),
    isMine: false,
    setIsMine: (isMine) => set({ isMine }),
    isDept: false,
    setIsDept: (isDept) => set({ isDept }),
    profiles: [],
    setProfiles: (profiles) => set({ profiles }),
    careerGrapPollSizeata: [],
    setCareerGrapPollSizeata: (data) => set({ careerGrapPollSizeata: data }),
    graduateGrapPollSizeata: [],
    setGraduateGrapPollSizeata: (data) => set({ graduateGrapPollSizeata: data }),
    selectedEmployee: [],
    setSelectedEmployee: (employees) => set({ selectedEmployee: employees }),
    selectedIds: [],
    setSelectedIds: (ids) => set({ selectedIds: ids }),
    filterResults: {} as Filters,
    setFilterResults: (key: string, selectedItems: string[]) => set((state) => ({
      filterResults: {
        ...state.filterResults,
        [key]: selectedItems,
      }
    })),
    departmentNames: [],
    setDepartmentNames: (departmentNames) => set({ departmentNames }),
    careerMinYear: undefined,
    setCareerMinYear: (careerMinYear?) => set({ careerMinYear }),
    careerMaxYear: undefined,
    setCareerMaxYear: (careerMaxYear?) => set({ careerMaxYear }),
    graduateMinYear: undefined,
    setGraduateMinYear: (graduateMinYear?) => set({ graduateMinYear }),
    graduateMaxYear: undefined,
    setGraduateMaxYear: (graduateMaxYear?) => set({ graduateMaxYear }),
    sort: "",
    setSort: (sort) => set({ sort }),
    isActiveSelected: false,
    setIsActiveSelected: (isActiveSelected) => set({ isActiveSelected }),
    isInactiveSelected: false,
    setIsInactiveSelected: (isInactiveSelected) => set({ isInactiveSelected }),
    careerRange: [],
    setCareerRange: (careerRange: number[]) => set({ careerRange }),
    graduateRange: [],
    setGraduateRange: (graduateRange: number[]) => set({ graduateRange }),
    clearFilter: () => set({
      currentPage: 1,
      pageSize: 10,
      totalPage: 0,
      isMine: false,
      isDept: false,
      profiles: [],
      careerGrapPollSizeata: [],
      graduateGrapPollSizeata: [],
      selectedEmployee: [],
      selectedIds: [],
      filterResults: {} as Filters,
      departmentNames: [],
      careerMinYear: undefined,
      careerMaxYear: undefined,
      graduateMinYear: undefined,
      graduateMaxYear: undefined,
      sort: "",
      isActiveSelected: false,
      isInactiveSelected: false,
    })
  }),
  // {
  //   name: "filter-storage"
  // },
  // )
);

export default useFilterStore;