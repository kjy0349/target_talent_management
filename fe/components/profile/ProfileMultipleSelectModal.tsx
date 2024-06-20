"use client";

import { useLocalAxios } from "@/app/api/axios";
import {
  EmployStatus,
  ProfileExternalSearchCondition,
  ProfileExternalSearchFull,
  ProfilePreview,
} from "@/types/admin/Profile";
import { Badge, Button, Modal, Pagination, TextInput } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";
import ProjectDetailProfileCard from "../project/ProjectDetailProfileCard";
import ProfileCardProp from "@/types/talent/ProfileCardProp";
import DetailProfileCard from "./detailprofilecard";

interface ProfileMutipleSelectionModalProps {
  isOpen: boolean;
  size: string;
  title: string;
  domain: string;
  domainId: number;
  toggleDrawer: () => void;
  onClose: (ids: number[]) => void;
}

const ProfileMutipleSelectionModal = ({
  isOpen,
  toggleDrawer,
  onClose,
  size,
  title,
  domain,
  domainId,
}: ProfileMutipleSelectionModalProps) => {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [selected, setSelected] = useState<number[]>([]);
  const [searchCondition, setSearchCondition] =
    useState<ProfileExternalSearchCondition>({
      searchString: "",
    });
  const [isMounted, setIsMounted] = useState<boolean>(false);
  const [maxCount, setMaxCount] = useState<number>();
  const [maxPage, setMaxPage] = useState<number>();
  const [allList, setAllList] = useState<number[]>();
  const [profiles, setProfiles] = useState<ProfileCardProp[]>();
  const handleKeywordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchCondition({
      searchString: e.target.value,
    });
  };
  const searchProfile = async (
    searchCondition: ProfileExternalSearchCondition,
    page: number,
  ) => {
    setProfiles([]);
    const response = await localAxios.post<ProfileExternalSearchFull>(
      `/profile/domaintype/${domain}/${domainId}?page=${page - 1}`,
      searchCondition,
    );
    setProfiles(response.data.profileList);
    setAllList(response.data.profileList.map((profile) => profile.profileId));
    setMaxCount(response.data.maxCount);
    setMaxPage(response.data.maxPage);
  };
  useEffect(() => {
    searchProfile(searchCondition, currentPage);
  }, []);

  const hanldeOnCurrentPageChange = (page: number) => {
    setCurrentPage(page);
    searchProfile(searchCondition, page);
  };

  const handleSelectId = (id: number) => {
    if (selected.includes(id)) {
      const nextSelected = selected.filter((sid) => sid != id);
      setSelected(nextSelected);
    } else {
      const nextSelected = [...selected, id];
      setSelected(nextSelected);
    }
  };

  const handleOnNormalClose = () => {
    setSelected([]);
    toggleDrawer();
  };
  const handleInputChecked = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked && profiles) {
      const allProfilesIds = profiles?.map((p) => p.profileId);
      const addList = profiles
        ?.filter((p) => !selected.includes(p.profileId))
        .map((p) => p.profileId);
      const nextSelected = selected.concat(addList);
      setSelected(nextSelected);
    } else {
      const allProfilesIds = profiles?.map((p) => p.profileId);
      const nextSelected = selected.filter(
        (id) => !allProfilesIds?.includes(id),
      );
      setSelected(nextSelected);
    }
  };

  return (
    <>
      <Modal show={isOpen} onClose={toggleDrawer} size={"5xl"}>
        <Modal.Header>
          <div className="flex justify-between">
            {title} 총:{maxCount}명{" "}
          </div>
        </Modal.Header>
        <Modal.Body className="w-full">
          <div className="flex w-full items-center justify-between p-2.5">
            <div className="flex w-full items-center justify-center gap-1">
              <div className="flex items-center">
                <Badge>
                  <label htmlFor="allSelectProfile">
                    페이지 인재 전체 선택
                  </label>
                </Badge>
                <input
                  id="allSelectProfile"
                  type="checkbox"
                  className="mr-2"
                  onChange={handleInputChecked}
                  checked={allList?.every((id) => selected.includes(id))}
                />
              </div>

              <input
                type="search"
                id="default-search"
                name="keyword"
                className="w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="인재와 관련한 키워드를 입력해주세요. ex) 이름_홍길동, 직장_삼성 등"
                value={searchCondition?.searchString}
                onChange={handleKeywordChange}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    e.preventDefault();
                    searchProfile(searchCondition, 1);
                  }
                }}
              />
              <button
                type="button"
                className=" rounded-r-lg border  border-blue-700 bg-blue-700 p-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300"
                onClick={() => searchProfile(searchCondition, currentPage)}
              >
                <HiSearch className="h-5 w-5" />
              </button>
            </div>
            <div></div>
          </div>
          <div className="space-y-4">
            {profiles &&
              profiles?.length > 0 &&
              profiles?.map((p, idx) => (
                <ProjectDetailProfileCard
                  key={p.profileId}
                  props={{
                    ...p,
                    checked: selected.includes(p.profileId),
                    handleCheck: () => handleSelectId(p.profileId),
                  }}
                />
              ))}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <div className="flex w-full flex-row justify-between p-5">
            <Pagination
              currentPage={currentPage}
              totalPages={maxPage ? maxPage : 1}
              layout="pagination"
              onPageChange={hanldeOnCurrentPageChange}
              className=" w-fit"
              previousLabel="이전"
              nextLabel="다음"
            />

            <Button
              onClick={() => {
                const sendProfileIds = [...selected];
                onClose(sendProfileIds);
                setSelected([]);
                toggleDrawer();
              }}
            >
              인재 추가하기
            </Button>
          </div>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProfileMutipleSelectionModal;
