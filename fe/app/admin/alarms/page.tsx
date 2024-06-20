"use client";

import { useRouter } from "next/navigation";
import { HiAnnotation, HiArchive, HiBadgeCheck, HiMenu } from "react-icons/hi";
import { HiMiniBell, HiMiniMegaphone } from "react-icons/hi2";

export default function AdminAlarmsPage() {
  const router = useRouter();
  const handleClick = (url: string) => {
    router.push(url);
  };
  return (
    <>
      <div className="h-screen">
        <div className="flex flex-col space-y-5 p-5">
          <h3 className="text-xl">전체 공지 관리</h3>
        </div>
        <div className="mb-8 grid grid-cols-1 gap-4 rounded-lg border border-gray-200 bg-white p-5 shadow-sm dark:border-gray-700 dark:bg-gray-800 md:mb-12 md:grid-cols-2">
          <figure
            className="flex h-80 flex-col items-center justify-center rounded-t-lg border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-t-none md:rounded-ss-lg md:border-e"
            onClick={() => handleClick("/admin/alarms/board")}
          >
            <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                공지 사항 관리
              </h3>
              <p className="my-4">공지사항 게시판의 내용을 관리합니다.</p>
            </blockquote>
            <figcaption className="flex items-center justify-center ">
              <div className="h-9 w-9 rounded-full">
                <HiMiniMegaphone className="h-full w-full" />
              </div>
            </figcaption>
          </figure>
          <figure
            className="flex flex-col items-center justify-center border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-se-lg"
            onClick={() => handleClick("/admin/alarms/notification")}
          >
            <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                알림 관리
              </h3>
              <p className="my-4">알림 관련 주기 및 개인 알림을 관리합니다.</p>
            </blockquote>
            <figcaption className="flex items-center justify-center ">
              <div className="h-9 w-9 rounded-full">
                <HiAnnotation className="h-full w-full" />
              </div>
            </figcaption>
          </figure>
          <figure
            className="flex h-80 flex-col items-center justify-center border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-es-lg md:border-b-0 md:border-e"
            onClick={() => handleClick("/admin/alarms/popup")}
          >
            <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                팝업 메시지 관리
              </h3>
              <p className="my-4">
                사이트 접속시 호출되는 팝업 메시지를 관리합니다.
              </p>
            </blockquote>
            <figcaption className="flex items-center justify-center ">
              <div className="h-9 w-9 rounded-full">
                <HiMiniBell className="h-full w-full" />
              </div>
            </figcaption>
          </figure>
          <figure
            className="flex flex-col items-center justify-center rounded-b-lg border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-se-lg"
            onClick={() => handleClick("/admin/alarms/menu")}
          >
            <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                메뉴 관리
              </h3>
              <p className="my-4">사이드 바 메뉴를 관리합니다.</p>
            </blockquote>
            <figcaption className="flex items-center justify-center ">
              <div className="h-9 w-9 rounded-full">
                <HiMenu className="h-full w-full" />
              </div>
            </figcaption>
          </figure>
        </div>
      </div>
    </>
  );
}
