"use client";

import { Badge, Button } from "flowbite-react";
import { HiCheck } from "react-icons/hi";

const CommuntiyTable = () => {
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];

  return (
    <>
      <div className="flex min-h-screen flex-col space-y-5 bg-white p-5">
        <h3 className="text-xl">기본 정보 및 권한 관리</h3>
        <div className="flex flex-col space-y-4 text-center">
          <div className="flex items-center bg-gray-100 px-4 py-3 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <div className="w-1/4 font-semibold">항목 값</div>
            <div className="w-1/4 font-semibold">구분</div>
            <div className="w-1/2 font-semibold">상세 내용</div>
          </div>

          <div className="flex px-4">
            <div className="flex w-1/4 flex-col">
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                프로필 항목 관리
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                담당 업무
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                회원 등급
              </div>
            </div>
            <div className="flex w-1/4 flex-col">
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                1
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                2
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                3
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                4
              </div>
              <div className="flex h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 hover:from-neutral-100">
                5
              </div>
            </div>
            <div className="w-1/2"></div>
          </div>
        </div>
      </div>
    </>
  );
};

export default CommuntiyTable;
