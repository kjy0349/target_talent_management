import { useEffect, useState } from "react";
import { HiOutlinePencil, HiPencil } from "react-icons/hi";
import {
  ProfileColumn,
  ProfileDetail,
} from "@/types/talent/ProfileDetailResponse";
import CustomAddButton from "@/components/common/CustomAddButton";
import { useRouter } from "next/navigation";

export default function ProfileDetailSection({
  params,
  profile,
}: {
  params: { slug: string };
  profile: ProfileDetail;
}) {
  const column = (key: string) => {
    return profile.columnDatas.find((x) => x.name == key)?.value;
  };
  const router = useRouter();

  const handleNavigate = () => {
    const data = {
      profileImage: profile.profileImage,
      isPrivate: profile.isPrivate,
      isAllCompany: profile.isAllCompany,
      profileId: profile.profileId,
      targetJobRankId: profile.jobRankDetail?.id,
      targetJobRank: profile.jobRankDetail,
      columns: profile.columnDatas,
      // 기타 데이터들
    };
    sessionStorage.setItem("profileData", JSON.stringify(data));
    router.push("/talent/register");
  };

  return (
    <div className="p-10 shadow-md">
      <div className="mb-6 flex flex-row items-center justify-between border-b-2 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold" whitespace-nowrap>
            인적사항
          </p>
        </div>

        <HiOutlinePencil
          className="size-7 hover:cursor-pointer"
          onClick={handleNavigate}
        />
      </div>
      <table className="mx-4 w-full table-fixed text-xl">
        <tbody>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">column1</td>
            <td className="w-5/6" colSpan={3}>
              {column("column1")}{" "}
              {column("visa") ? "(비자 유형 : " + column("visa") + ")" : ""}
            </td>
          </tr>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">거주지역</td>
            <td className="w-2/6">{column("foundCountry")}</td>
            <td className="w-1/6 whitespace-nowrap font-bold">
              한국어/Relocation
            </td>
            <td className="w-2/6">
              {column("korean")} / {column("relocation")}
            </td>
          </tr>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">타겟직급</td>
            <td className="w-2/6">{profile.jobRankDetail?.description}</td>
            <td className="w-1/6 whitespace-nowrap font-bold">tech</td>
            <td className="w-2/6">{column("skillMainCategory")}</td>
          </tr>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">기술분야</td>
            <td className="w-2/6">{column("skillSubCategory")}</td>
            <td className="w-1/6 whitespace-nowrap font-bold">상세분야</td>
            <td className="w-2/6">{column("skillDetail")}</td>
          </tr>
          <tr></tr>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">발굴경로</td>
            <td className="w-5/6" colSpan={3}>
              {column("foundPath")}
              {column("recommenderInfo")
                ? " (추천인 : " +
                  column("recommenderInfo") +
                  (column("recommenderRelation")
                    ? ", " + column("recommenderRelation")
                    : "") +
                  ")"
                : ""}
            </td>
          </tr>
          <tr>
            <td className="w-1/6 whitespace-nowrap font-bold">전화번호</td>
            <td className="w-2/6">{column("phone")}</td>
            <td className="w-1/6 whitespace-nowrap font-bold">이메일</td>
            <td className="w-2/6">
              <span className="w-2/6 break-words">{column("email")}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
