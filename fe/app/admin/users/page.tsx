"use client";
import { AuthorityModal } from "@/components/admin/AuthorityAdminModal";
import { DepartmentModal } from "@/components/admin/DepartmentAdminModal";
import { TeamRoleModal } from "@/components/admin/TeamRoleAdminModal";
import { Modal } from "flowbite-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import {
  HiAnnotation,
  HiArchive,
  HiBadgeCheck,
  HiMenu,
  HiOutlineAdjustments,
  HiUsers,
} from "react-icons/hi";
import {
  HiBuildingOffice,
  HiMiniBell,
  HiMiniDocument,
  HiMiniMegaphone,
} from "react-icons/hi2";

export default function AdminMemberPage() {
  const router = useRouter();
  const [showAuthorityModal, setAuthorityModal] = useState(false);

  const openAuthorityModal = () => {
    setAuthorityModal(true);
  };
  const closeAuthorityModal = () => {
    setAuthorityModal(false);
  };
  const [showDepartmentModal, setDepartmentModal] = useState(false);

  const openDepartmentModal = () => {
    setDepartmentModal(true);
  };
  const closeDepartmentModal = () => {
    setDepartmentModal(false);
  };
  const [showTeamAndRuleModal, setTeamAndRuleModal] = useState(false);

  const openTeamAndRuleModal = () => {
    setTeamAndRuleModal(true);
  };
  const closeTeamAndRuleModal = () => {
    setTeamAndRuleModal(false);
  };

  const handleClick = (url: string) => {
    router.push(url);
  };
  return (
    <>
      <div className="flex flex-col space-y-5 p-5">
        <h3 className="text-xl">전체 사용자 관리</h3>
      </div>
      <div className="mb-8 grid grid-cols-1 gap-4 rounded-lg border border-gray-200 bg-white p-5 shadow-sm dark:border-gray-700 dark:bg-gray-800 md:mb-12 md:grid-cols-2">
        <figure
          className="flex h-80 flex-col items-center justify-center rounded-t-lg border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-t-none md:rounded-ss-lg md:border-e"
          onClick={() => handleClick("/admin/users/member")}
        >
          <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              회원 정보 관리
            </h3>
            <p className="my-4">전체 회원 정보에 대해 관리합니다.</p>
          </blockquote>
          <figcaption className="flex items-center justify-center ">
            <div className="h-9 w-9 rounded-full">
              <HiUsers className="h-full w-full" />
            </div>
          </figcaption>
        </figure>
        <figure
          className="flex flex-col items-center justify-center border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-se-lg"
          onClick={() => openAuthorityModal()}
        >
          <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              권한 관리
            </h3>
            <p className="my-4">
              사용자들이 활용 가능한 권한에 대해 관리합니다.
            </p>
          </blockquote>
          <figcaption className="flex items-center justify-center ">
            <div className="h-9 w-9 rounded-full">
              <HiOutlineAdjustments className="h-full w-full" />
            </div>
          </figcaption>
        </figure>
        <figure
          className="flex h-80 flex-col items-center justify-center border-b border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-es-lg md:border-b-0 md:border-e"
          onClick={openDepartmentModal}
        >
          <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              전사 부서 관리
            </h3>
            <p className="my-4">전사 부서( ex. D.X )를 관리합니다.</p>
          </blockquote>
          <figcaption className="flex items-center justify-center ">
            <div className="h-9 w-9 rounded-full">
              <HiBuildingOffice className="h-full w-full" />
            </div>
          </figcaption>
        </figure>
        <figure
          className="flex flex-col items-center justify-center rounded-b-lg border-gray-200 bg-white p-8 text-center hover:bg-slate-100 dark:border-gray-700 dark:bg-gray-800 md:rounded-se-lg"
          onClick={openTeamAndRuleModal}
        >
          <blockquote className="mx-auto mb-4 max-w-2xl text-gray-500 dark:text-gray-400 lg:mb-8">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              개인 팀 및 담당 업무 관리
            </h3>
            <p className="my-4">
              사용자들이 각자 담당하는 팀과 업무에 대해 관리합니다.
            </p>
          </blockquote>
          <figcaption className="flex items-center justify-center ">
            <div className="h-9 w-9 rounded-full">
              <HiMenu className="h-full w-full" />
            </div>
          </figcaption>
        </figure>
      </div>

      <AuthorityModal
        showModal={showAuthorityModal}
        closeModal={closeAuthorityModal}
      />
      <DepartmentModal
        size="3xl"
        showModal={showDepartmentModal}
        closeModal={closeDepartmentModal}
      />
      <TeamRoleModal
        size="3xl"
        showModal={showTeamAndRuleModal}
        closeModal={closeTeamAndRuleModal}
      />
    </>
  );
}
