"use client";

import { useEffect, useState } from "react";
import { HiChevronDoubleLeft, HiFilter } from "react-icons/hi";
import {
  Checkbox,
  Dropdown,
  DropdownItem,
  Label,
  Pagination,
} from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import DetailProfileCard from "@/components/profile/detailprofilecard";
import SearchFilter from "@/components/filter/searchfilter";
import { useSearchParams } from "next/navigation";
import AddToProjectModal from "@/components/project/AddToProjectModal";
import ChangeManageModal from "@/components/profile/ChangeManageModal";
import ProfileGrapPollSizeata from "@/types/talent/ProfileGrapPollSizeata";
import { FilterCount } from "@/types/talent/Filter";
import { useAuthStore } from "@/stores/auth";
import useFilterStore from "@/stores/filter";
import ProfileCardProp from "@/types/talent/ProfileCardProp";

interface Step {
  label: string;
  value: string;
}

const activeStatusSteps: Step[] = [
  { label: "발굴", value: "FOUND" },
  { label: "접촉", value: "CONTACT" },
  { label: "활용도 검토", value: "USAGE_REVIEW" },
  { label: "중/장기 관리", value: "MID_LONG_TERM_MANAGE" },
  { label: "단기 관리", value: "SHORT_TERM_MANAGE" },
  { label: "인터뷰1", value: "인터뷰1" },
  { label: "인터뷰2", value: "인터뷰2" },
  { label: "인터뷰3", value: "인터뷰3" },
  { label: "인터뷰4", value: "for_interview" },
  { label: "처우협의", value: "NEGOTIATION" },
  { label: "입사대기", value: "EMPLOY_WAITING" },
  { label: "입사", value: "EMPLOYED" },
];

const inactiveStatusSteps = [
  { label: "처우결렬", value: "NEGOTIATION_DENIED" },
  { label: "입사포기", value: "EMPLOY_ABANDON" },
];

const sortOptions: Step[] = [
  { label: "최근 업데이트 순", value: "modifiedAt,desc" },
  { label: "프로필 생성 최신순", value: "createdAt,desc" },
  { label: "프로필 생성 과거순", value: "createdAt,asc" },
  { label: "이름 오름차순", value: "name,asc" },
  { label: "이름 내림차순", value: "name,desc" },
  // { label: "초기화", value: "" },
];

const initialActiveArr = Array(activeStatusSteps.length).fill(0);
const initialInactiveArr = Array(inactiveStatusSteps.length).fill(0);

const findMinAndMaxYear = (data: ProfileGrapPollSizeata[]) => {
  let minYear = 10000;
  let maxYear = -10000;
  data.forEach((entry) => {
    if (entry.data !== "none") {
      minYear = Math.min(minYear, parseInt(entry.data));
      maxYear = Math.max(maxYear, parseInt(entry.data));
    }
  });
  return [minYear, maxYear];
};

export default function SearchPage() {
  const store = useFilterStore();
  const [showFilter, setShowFilter] = useState(false);
  const [profiles, setProfiles] = useState<ProfileCardProp[]>([]);
  const [fullCount, setFullCount] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [filterCounts, setFilterCounts] = useState<FilterCount[]>([]);
  const [filterActiveCount, setFilterActiveCount] = useState(0);
  const [filterActiveCounts, setFilterActiveCounts] =
    useState<number[]>(initialActiveArr);
  const [filterInactiveCount, setFilterInactiveCount] = useState(0);
  const [filterInactiveCounts, setFilterInactiveCounts] =
    useState<number[]>(initialInactiveArr);
  // const [selectedEmployee, setSelectedEmployee] = useState<string[]>([]);
  const [mineCount, setMineCount] = useState(0);
  const [deptCount, setDeptCount] = useState(0);
  // const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const localAxios = useLocalAxios();
  const [showAddToProjectModal, setShowAddToProjectModal] = useState(false);
  const [showChangeManagerModal, setShowChangeManagerModal] = useState(false);
  const [checkAll, setCheckAll] = useState(false);
  const [isUpdate, setIsUpdate] = useState(false);
  const [isFilterUpdate, setIsFilterUpdate] = useState(false);
  const authStore = useAuthStore();
  const dept = authStore.departmentName;
  const searchParams = useSearchParams();
  const keyword = searchParams.get("search") || "";
  // 필터 숫자 초기화 후 계산
  const getFilterCount = () => {
    setFilterActiveCount(0);
    setFilterInactiveCount(0);
    setFilterActiveCounts(initialActiveArr);
    setFilterInactiveCounts(initialInactiveArr);
    filterCounts.map((count) => {
      const indexActive = activeStatusSteps.findIndex(
        (step) => step.value === count.data,
      );
      if (indexActive !== -1) {
        setFilterActiveCount((prevCount) => prevCount + count.count);
        setFilterActiveCounts((prevCounts) =>
          prevCounts.map((p, i) => (i === indexActive ? count.count : p)),
        );
      }
      const indexInactive = inactiveStatusSteps.findIndex(
        (step) => step.value === count.data,
      );
      if (indexInactive !== -1) {
        setFilterInactiveCount((prevCount) => prevCount + count.count);
        setFilterInactiveCounts((prevCounts) =>
          prevCounts.map((p, i) => (i === indexInactive ? count.count : p)),
        );
      }
    });
  };
  // 변경시 필터 값 업데이트
  useEffect(() => getFilterCount(), [filterCounts]);
  // 데이터 가져오기
  const fetchData = () => {
    localAxios
      .post(
        `/profile/search`,
        {
          searchString: keyword,
          careerMinYear: store.careerMinYear,
          careerMaxYear: store.careerMaxYear,
          graduateMinYear: store.graduateMinYear,
          graduateMaxYear: store.graduateMaxYear,
          isMine: store.isMine,
          employStatuses: store.selectedEmployee,
          departmentNames: store.departmentNames,
          ...store.filterResults,
        },
        {
          params: {
            page: store.currentPage - 1,
            size: store.pageSize,
            sort: store.sort,
          },
        },
      )
      .then((Response) => {
        setProfiles(Response.data.profilePreviews.content);
        // store.setCareerGrapPollSizeata(Response.data.careerRange);
        // store.setGraduateGrapPollSizeata(Response.data.graduateAtRange);
        store.setTotalPage(Response.data.profilePreviews.totalPages);
        window.scrollTo(0, 0);
      });
  };
  const getInitialRange = () => {
    localAxios
      .post(
        `/profile/search`,
        {
          searchString: keyword,
          careerMinYear: store.careerMinYear,
          careerMaxYear: store.careerMaxYear,
          graduateMinYear: store.graduateMinYear,
          graduateMaxYear: store.graduateMaxYear,
          isMine: store.isMine,
          employStatuses: [],
          ...store.filterResults,
        },
        {
          params: {
            page: store.currentPage - 1,
            size: store.pageSize,
          },
        },
      )
      .then((Response) => {
        store.setCareerRange(findMinAndMaxYear(Response.data.careerRange));
        store.setGraduateRange(
          findMinAndMaxYear(Response.data.graduateAtRange),
        );
      });
  };
  // 단계별 숫자 업데이트
  const getFilterNumber = () => {
    localAxios
      .post(
        `/profile/search`,
        {
          searchString: keyword,
          careerMinYear: store.careerMinYear,
          careerMaxYear: store.careerMaxYear,
          graduateMinYear: store.graduateMinYear,
          graduateMaxYear: store.graduateMaxYear,
          isMine: store.isMine,
          employStatuses: [],
          ...store.filterResults,
        },
        {
          params: {
            page: store.currentPage - 1,
            size: store.pageSize,
          },
        },
      )
      .then((Response) => {
        setTotalElements(Response.data.profilePreviews.totalElements);
        setMineCount(Response.data.myProfileCount);
        setFilterCounts(Response.data.employStatusCounts);
        setFullCount(Response.data.profilePreviews.totalElements);
        store.setCareerGrapPollSizeata(Response.data.careerRange);
        store.setGraduateGrapPollSizeata(Response.data.graduateAtRange);
        const myDept = Response.data.myDepartment;
        const departmentData: ProfileGrapPollSizeata[] =
          Response.data.founderDepartmentCounts;
        const matchingDept = departmentData.find(
          (data) => data.data === myDept,
        );
        if (matchingDept) {
          setDeptCount(matchingDept.count);
        } else {
          setDeptCount(0);
        }
        if (Response.data.employStatusCounts.length !== 0) {
          setFilterActiveCounts(filterActiveCounts.map((x) => 0));
          setFilterInactiveCounts(filterInactiveCounts.map((x) => 0));
        }
        if (!isFilterUpdate) {
          window.scrollTo(0, 0);
        }
      });
  };

  // 필터 제출
  const handleFilterResults = () => {
    setIsUpdate(!isUpdate);
  };
  useEffect(() => {
    getInitialRange();
  }, []);
  // 필터 업데이트
  useEffect(() => {
    getFilterNumber();
  }, [isFilterUpdate, store.filterResults, isUpdate, keyword]);
  // 데이터 가져오기
  useEffect(() => {
    if (authStore) {
      fetchData();
    }
    setCheckAll(false);
    setIsFilterUpdate(false);
  }, [
    keyword,
    store.sort,
    isUpdate,
    store.filterResults,
    store.currentPage,
    store.selectedEmployee,
    store.isMine,
    keyword,
    store.isDept,
    store.isMine,
    store.pageSize,
  ]);

  // 후보자 상태 업데이트 후 필터수 업데이트
  const handleOnUpdateDetail = () => {
    setIsFilterUpdate(true);
  };
  // 아이디 하나 클릭
  const handleSelectId = (id: number) => {
    if (store.selectedIds.includes(id)) {
      store.setSelectedIds(store.selectedIds.filter((prevId) => prevId !== id));
    } else {
      store.setSelectedIds([...store.selectedIds, id]);
    }
  };
  // 단계별로 보기
  const filterProfileByEmployStatus = (filterStep: string) => {
    store.setCurrentPage(1);
    store.setSelectedEmployee([filterStep]);
    store.setIsActiveSelected(false);
    store.setIsInactiveSelected(false);
  };
  // 활동 단계 보기
  const filterProfileActive = () => {
    store.setIsActiveSelected(!store.isActiveSelected);
    if (!store.isActiveSelected) {
      store.setIsActiveSelected(true);
      store.setIsInactiveSelected(false);
      const selectActive: string[] = [];
      activeStatusSteps.map((step) => {
        selectActive.push(step.value);
      });
      store.setSelectedEmployee(selectActive);
    } else {
      store.setIsActiveSelected(false);
      store.setIsInactiveSelected(false);
      store.setSelectedEmployee([]);
    }
  };
  // 비활동 단계 보기
  const filterProfileInactive = () => {
    if (!store.isInactiveSelected) {
      store.setIsInactiveSelected(true);
      store.setIsActiveSelected(false);
      const selectInactive: string[] = [];
      inactiveStatusSteps.map((step) => {
        selectInactive.push(step.value);
      });
      store.setSelectedEmployee(selectInactive);
    } else {
      store.setIsActiveSelected(false);
      store.setIsInactiveSelected(false);
      store.setSelectedEmployee([]);
    }
  };
  // 발굴단계 변경
  const updateEmployStatus = (e: string) => {
    localAxios
      .put(`/profile/status`, {
        status: e,
        profileIds: store.selectedIds,
      })
      .then(() => {
        // setIsUpdate(!isUpdate)
        setIsFilterUpdate(true);
      });
  };
  // 프로젝트에 추가
  const addToProject = (projectId: number) => {
    if (confirm("해당 프로젝트에 추가하시겠습니까?")) {
      localAxios
        .put(`/project/profiles/${projectId}`, {
          projectProfiles: store.selectedIds,
        })
        .then((Response) => {
          if (Response.status === 200) {
            alert("프로젝트에 추가했습니다.");
            setShowAddToProjectModal(false);
          } else {
            alert("서버 오류");
          }
        });
    }
  };
  // 담당자 변경
  const changeManagement = (memberId: number) => {
    if (confirm("담당자를 변경하시겠습니까?")) {
      localAxios
        .put("/profile/manager", {
          profileIds: store.selectedIds,
          memberId: memberId,
        })
        .then((Response) => {
          if (Response.status === 200) {
            alert("담당자를 변경했습니다.");
            setShowChangeManagerModal(false);
          } else {
            alert("서버 오류");
          }
        });
    }
  };
  // 전체 체크박스
  const handleCheckAll = () => {
    if (checkAll) {
      const updatedSelectedIds = store.selectedIds.filter(
        (id) => !profiles.some((profile) => profile.profileId === id),
      );
      setCheckAll(false);
      store.setSelectedIds(updatedSelectedIds);
    } else {
      const updatedSelectedIds = store.selectedIds;
      profiles.map(
        (profile) =>
          !updatedSelectedIds.includes(profile.profileId) &&
          updatedSelectedIds.push(profile.profileId),
      );
      setCheckAll(true);
      store.setSelectedIds(updatedSelectedIds);
    }
  };
  // 전체보기
  const handleSelectAllDept = () => {
    const departments = store.departmentNames;
    store.setDepartmentNames(departments.filter((item) => item !== dept));
    store.setSelectedEmployee([]);
    store.setIsMine(false);
    store.setIsDept(false);
    store.setIsActiveSelected(false);
    store.setIsInactiveSelected(false);
    setIsUpdate(!isUpdate);
  };
  // 사업부보기
  const handleSelectDept = () => {
    if (store.isDept) {
      const departments = store.departmentNames;
      store.setDepartmentNames(departments.filter((item) => item !== dept));
      store.setIsDept(false);
      store.setSelectedEmployee([]);
      store.setIsMine(false);
    } else {
      const departments = store.departmentNames;
      dept && departments.push(dept);
      store.setDepartmentNames(departments);
      store.setIsDept(true);
      store.setIsMine(false);
      dept && store.setDepartmentNames([dept]);
    }
  };
  // My Pool 보기
  const handleSelectMine = () => {
    if (store.isMine) {
      const departments = store.departmentNames;
      store.setDepartmentNames(departments.filter((item) => item !== dept));
      store.setIsMine(false);
      store.setIsDept(false);
    } else {
      const departments = store.departmentNames;
      store.setDepartmentNames(departments.filter((item) => item !== dept));
      store.setIsMine(true);
      store.setIsDept(false);
    }
  };

  //전체 단계 보기
  const handleSelectAllStep = () => {
    store.setSelectedEmployee([]);
    store.setIsActiveSelected(false);
    store.setIsInactiveSelected(false);
  };
  // 엑셀 다운
  const downloadToExcel = async () => {};
  // 워드저장
  const downloadToWord = async () => {};

  // 프로필삭제
  const deleteProfiles = () => {
    if (confirm("해당 후보자를 삭제하겠습니까?")) {
      localAxios
        .delete("/profile", {
          data: {
            profileIds: store.selectedIds,
          },
        })
        .then((Response) => {
          if (Response.status === 200) {
            alert("후보자를 삭제했습니다.");
            setIsUpdate(!isUpdate);
            setIsFilterUpdate(!isFilterUpdate);
            store.setSelectedIds([]);
          } else {
            alert("서버 오류");
          }
        });
    }
  };

  return (
    <div className="flex flex-row">
      <div className="relative h-fit min-w-52 max-w-52 whitespace-nowrap rounded-md border bg-white p-6">
        <button
          className="m-auto my-2 flex w-full flex-row justify-between pb-1 text-xl font-bold"
          onClick={handleSelectAllStep}
        >
          <span>전체</span>
          <span>{fullCount}</span>
        </button>
        <hr className="my-4 "></hr>
        <button
          className={
            "m-auto my-2 flex w-full flex-row justify-between text-xl font-bold" +
            (store.isActiveSelected ? " bg-yellow-100" : "")
          }
          onClick={filterProfileActive}
        >
          <span>활동 단계</span>
          <span>{filterActiveCount}</span>
        </button>
        <div className="space-y-1 text-xs">
          {activeStatusSteps.map((item, index) => {
            return (
              <button
                key={item.value}
                className={
                  "m-auto my-2 flex w-full flex-row justify-between rounded-md p-1 px-2 text-base hover:bg-yellow-100 " +
                  (!store.isActiveSelected &&
                  store.selectedEmployee.includes(item.value)
                    ? "bg-yellow-100"
                    : "")
                }
                onClick={() => {
                  // filterActiveCounts[index] > 0 &&
                  filterProfileByEmployStatus(item.value);
                }}
              >
                <span>{item.label}</span>
                <span>
                  {filterActiveCounts.length > 0
                    ? filterActiveCounts[index]
                    : 0}
                </span>
              </button>
            );
          })}
        </div>
        <hr className="my-4"></hr>
        <button
          className={
            "m-auto my-2 flex w-full flex-row justify-between text-xl font-bold" +
            (store.isInactiveSelected ? " bg-yellow-100" : "")
          }
          onClick={filterProfileInactive}
        >
          <span>비활동 단계</span>
          <span>{filterInactiveCount}</span>
        </button>
        <div className="space-y-1 text-xs">
          {inactiveStatusSteps.map((item, index) => {
            return (
              <button
                key={index}
                className={
                  "m-auto my-2 flex w-full flex-row justify-between rounded-md p-1 px-2 text-base hover:bg-yellow-100 " +
                  (!store.isInactiveSelected &&
                  store.selectedEmployee.includes(item.value)
                    ? "bg-yellow-100"
                    : "")
                }
                onClick={() => {
                  // filterInactiveCounts[index] > 0 &&
                  filterProfileByEmployStatus(item.value);
                }}
              >
                <span>{item.label}</span>
                <span>
                  {filterInactiveCounts.length > 0
                    ? filterInactiveCounts[index]
                    : 0}
                </span>
              </button>
            );
          })}
        </div>
      </div>
      <div className="fixed z-10 ml-52 flex h-fit min-w-[60vw] flex-row justify-between border-b border-slate-300 bg-white">
        <div className="relative m-2 flex flex-row items-center">
          <Checkbox
            className="mr-2"
            checked={checkAll}
            onChange={handleCheckAll}
          />
          <Label className="whitespace-nowrap text-xs font-bold">
            전체 선택
          </Label>
        </div>
        <div className="flex w-full flex-col text-xs">
          <div className="mt-1 flex h-10 flex-row justify-between">
            <div className="flex h-full flex-row justify-between">
              <button
                className={
                  "m-1 rounded-md border border-slate-300 px-2 text-center font-bold" +
                  (!store.isMine && !store.isDept ? " bg-yellow-100" : "")
                }
                onClick={handleSelectAllDept}
              >
                전체보기 ({totalElements})
              </button>
              <button
                className={
                  "m-1 rounded-md border border-slate-300 px-2 text-center font-bold" +
                  (store.isDept ? " bg-yellow-100" : "")
                }
                onClick={handleSelectDept}
              >
                사업부 ({deptCount})
              </button>
              <button
                className={
                  "m-1 rounded-md border border-slate-300 px-2 text-center font-bold" +
                  (store.isMine ? " bg-yellow-100" : "")
                }
                onClick={handleSelectMine}
              >
                My Pool ({mineCount})
              </button>
            </div>
            <div className="flex h-full flex-row justify-between">
              <button
                className="m-1 rounded-md bg-primary fill-white px-2 text-center font-bold text-white"
                onClick={() => setShowFilter(!showFilter)}
              >
                <HiFilter className="inline-block" />
                <span className="mx-2">필터</span>
              </button>
              <Dropdown
                inline
                arrowIcon={false}
                label={
                  <p className="m-1 rounded-md fill-white px-2 text-center font-bold">
                    {store.pageSize + "개씩 보기"}
                  </p>
                }
                theme={{
                  inlineWrapper: "m-1 rounded-md border",
                }}
              >
                <DropdownItem onClick={() => store.setPageSize(10)}>
                  10개씩 보기
                </DropdownItem>
                <DropdownItem onClick={() => store.setPageSize(30)}>
                  30개씩 보기
                </DropdownItem>
                <DropdownItem onClick={() => store.setPageSize(50)}>
                  50개씩 보기
                </DropdownItem>
              </Dropdown>

              <Dropdown
                inline
                arrowIcon={false}
                label={
                  <p className="m-1 rounded-md fill-white px-2 text-center font-bold">
                    {sortOptions.find((option) => option.value === store.sort)
                      ?.label || "정렬 기준"}
                  </p>
                }
                theme={{
                  inlineWrapper: "m-1 rounded-md border",
                }}
              >
                {sortOptions.map((option) => (
                  <DropdownItem
                    key={option.value}
                    onClick={() => store.setSort(option.value)}
                  >
                    {option.label}
                  </DropdownItem>
                ))}
                <DropdownItem onClick={() => store.setSort("")}>
                  초기화
                </DropdownItem>
              </Dropdown>
              {/* <Dropdown
                inline
                arrowIcon={false}
                label={
                  <p className="m-1 rounded-md fill-white px-2 text-center font-bold">
                    {this.sortOptions[store.sort] || "정렬 기준"}
                  </p>
                }
              >
                <DropdownItem onClick={() => store.setSort("modifiedAt, desc")}>
                  {sortOptions["modifiedAt, desc"]}
                </DropdownItem>
                <DropdownItem onClick={() => store.setSort("createdAt, desc")}>
                  {sortOptions["createdAt, desc"]}
                </DropdownItem>
                <DropdownItem onClick={() => store.setSort("createdAt, asc")}>
                  {sortOptions["createdAt, asc"]}
                </DropdownItem>
                <DropdownItem onClick={() => store.setSort("name, desc")}>
                  {sortOptions["name, desc"]}
                </DropdownItem>
                <DropdownItem onClick={() => store.setSort("name, asc")}>
                  {sortOptions["name, asc"]}
                </DropdownItem>
              </Dropdown> */}
            </div>
          </div>
          <div className="flex h-10 flex-row">
            <Dropdown
              inline
              label={
                <p className="m-1 rounded-md border border-slate-300 fill-white p-2 text-center font-bold">
                  활용단계 변경(
                  {store.selectedIds.length || 0})
                </p>
              }
              arrowIcon={false}
            >
              {activeStatusSteps.map((item) => (
                <Dropdown.Item
                  key={item.value}
                  onClick={() => {
                    updateEmployStatus(item.value);
                  }}
                >
                  {item.label}
                </Dropdown.Item>
              ))}
              {inactiveStatusSteps.map((item) => (
                <Dropdown.Item
                  key={item.value}
                  onClick={() => {
                    updateEmployStatus(item.value);
                  }}
                >
                  {item.label}
                </Dropdown.Item>
              ))}
            </Dropdown>
            <button
              className="m-1 rounded-md border border-slate-300 px-2 text-center font-bold"
              onClick={() =>
                // selectedIds.length > 0 &&
                setShowAddToProjectModal(true)
              }
            >
              프로젝트에 추가 ({store.selectedIds.length || 0})
            </button>
            {/* <button className="m-1 rounded-md border border-slate-300 px-2 text-center font-bold">
              엑셀파일 저장
              (
              {store.selectedIds.length || 0}
              )
            </button>
            <button className="m-1 rounded-md border border-slate-300 px-2 text-center font-bold">
              워드파일 저장
              (
              {store.selectedIds.length || 0}
              )
            </button> */}
            <button
              className="m-1 rounded-md border border-slate-300 px-2 text-center font-bold"
              onClick={() =>
                // selectedIds.length > 0 &&
                setShowChangeManagerModal(true)
              }
            >
              담당자 변경 ({store.selectedIds.length || 0})
            </button>
            <button
              className="m-1 rounded-md bg-red-500 px-2 text-center font-bold text-white"
              onClick={deleteProfiles}
            >
              삭제 ({store.selectedIds.length || 0})
            </button>
          </div>
        </div>
        <div className="flex flex-col items-center">
          <p className="my-auto text-base font-bold">
            <span>{(store.currentPage - 1) * store.pageSize + 1}</span>-
            <span>{store.currentPage * store.pageSize}</span>
          </p>
          <Pagination
            currentPage={store.currentPage}
            totalPages={store.totalPage}
            layout="navigation"
            onPageChange={(newPage) => store.setCurrentPage(newPage)}
            className="mx-auto w-fit"
            showIcons={true}
            previousLabel=""
            nextLabel=""
          />
        </div>
      </div>
      {/* {
        !showFilter &&
        <button className="fixed right-0 size-6" onClick={() => setShowFilter(true)}>
          <HiChevronDoubleLeft className="size-full" />
        </button>
      } */}
      {showFilter && (
        <SearchFilter
          closeFilter={() => setShowFilter(false)}
          onUpdateFilter={handleFilterResults}
        />
      )}
      <div className="mt-20">
        {profiles.map((profile) => (
          <DetailProfileCard
            key={profile.profileId}
            {...profile}
            checked={store.selectedIds.includes(profile.profileId)}
            handleCheck={() => handleSelectId(profile.profileId)}
            handleOnUpdateDetail={handleOnUpdateDetail}
          />
        ))}
      </div>
      <AddToProjectModal
        showModal={showAddToProjectModal}
        closeModal={() => setShowAddToProjectModal(false)}
        addToProject={addToProject}
      />
      <ChangeManageModal
        showModal={showChangeManagerModal}
        closeModal={() => setShowChangeManagerModal(false)}
        changeManagement={changeManagement}
      />
      {/* <div className="relative m-10 w-[80lvw] min-w-[50lvw] max-w-sm p-10">
        <Pagination
          currentPage={currentPage}
          totalPages={totalPage}
          layout="pagination"
          onPageChange={(newPage) => setCurrentPage(newPage)}
          className="mx-auto w-fit"
          previousLabel="이전"
          nextLabel="다음"
        />
      </div> */}
    </div>
  );
}
