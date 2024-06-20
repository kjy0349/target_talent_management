"use client";

import { useEffect, useState } from "react";
import FilterItems from "./ProfileFilterItems";
import ProfileFilterItems from "../../data/ProfileFilterItems";
import DualRangeSlider from "./DualRangeSlider";
import EnhancedAreaChart from "./GraphChart";
import useFilterStore from "@/stores/filter";
import { HiChevronRight, HiChevronUp } from "react-icons/hi";

export default function SearchFilter(
  { onUpdateFilter, closeFilter }:
    { onUpdateFilter: () => void, closeFilter: () => void; }
) {
  const [isCareerGraphOpen, setIsCareerGraphOpen] = useState(false);
  const [isGraduateGraphOpen, setIsGraduateGraphOpen] = useState(false);
  const store = useFilterStore();

  const handleSelectionChange = (key: string, selectedItems: string[]) => {
    // if (key === "departmentNames") {
    //   store.setDepartmentNames(selectedItems);
    // }
    // else {
    store.setFilterResults(key, selectedItems);
    // }

    onUpdateFilter();
  };

  const handleShowCareerGraph = () => {
    if (!isCareerGraphOpen) {
      store.setCareerMinYear(0);
      store.setCareerMaxYear(3000);
      setIsCareerGraphOpen(true);
    }
    else {
      store.setCareerMinYear(undefined);
      store.setCareerMaxYear(undefined);
      setIsCareerGraphOpen(false);
    }
  }

  const handleShowGraduateGraph = () => {
    if (!isGraduateGraphOpen) {
      store.setGraduateMinYear(1970);
      store.setGraduateMaxYear(new Date().getFullYear());
      setIsGraduateGraphOpen(true);
    }
    else {
      store.setGraduateMinYear(undefined);
      store.setGraduateMaxYear(undefined);
      setIsGraduateGraphOpen(false);
    }
  }

  return (
    <>
      <div className="fixed bottom-0 right-0 top-[52px] z-10 flex w-[calc(40vw-288px)] border border-l flex-col overflow-auto bg-white p-5 pb-40 scrollbar-hide">
        <h1 className="text-xl font-bold ml-4">맞춤 필터</h1>
        {ProfileFilterItems.map((item, index) => {
          return (
            <FilterItems
              key={index}
              props={{
                seq: index,
                label: item.label,
                value: item.value,
                list: item.list,
                addAble: item.addAble,
                identifier: item?.identifier,
                isOption: item?.isOption,
                onSelectionChange: (selected) =>
                  handleSelectionChange(item.value, selected),
              }}
            />
          );
        })}
        {/* TODO : 경력, 학력 그래프 하드 */}
        <label className="text-lg font-bold cursor-pointer flex flex-row items-center" onClick={handleShowCareerGraph}>경력연차 {!isCareerGraphOpen ? <HiChevronRight /> : <HiChevronUp />}</label>
        {
          isCareerGraphOpen && (
            <div className="bg-white dark:bg-gray-800 p-4 border border-l">
              <EnhancedAreaChart
                identifier="career"
              />
              <DualRangeSlider
                minYear={store.careerRange[0]}
                maxYear={store.careerRange[1]}
                onMinChange={store.setCareerMinYear}
                onMaxChange={store.setCareerMaxYear}
              />
            </div>
          )
        }
        <label className="text-lg font-bold cursor-pointer flex flex-row items-center" onClick={handleShowGraduateGraph}>졸업년도 {!isGraduateGraphOpen ? <HiChevronRight /> : <HiChevronUp />}</label>
        {
          isGraduateGraphOpen && (
            <div className="rounded-lg bg-white p-4 shadow">
              <EnhancedAreaChart
                identifier="graduate"
              />
              <DualRangeSlider
                minYear={store.graduateRange[0]}
                maxYear={store.graduateRange[1]}
                onMinChange={store.setGraduateMinYear}
                onMaxChange={store.setGraduateMaxYear}
              />
            </div>
          )
        }
        <div className="fixed bottom-0 right-0 z-10 flex h-20 w-[calc(40vw-288px)] flex-row justify-end border-t bg-white border border-l">
          <button
            className="mr-10 flex w-fit items-center self-center rounded bg-primary p-2 px-6 text-white shadow-md"
            onClick={() => onUpdateFilter()}
          >
            적용
          </button>
        </div>

      </div>
    </>
  );
}
