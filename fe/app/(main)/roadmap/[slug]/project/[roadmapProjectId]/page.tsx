"use client";

import {
  Pagination,
  Badge,
  Avatar,
  Button,
  Checkbox,
  Dropdown,
  DropdownItem,
} from "flowbite-react";
import {
  HiOutlinePencilAlt,
  HiLockOpen,
  HiLockClosed,
  HiPlus,
} from "react-icons/hi";
import { twMerge } from "tailwind-merge";
import { useState, useMemo, useEffect, useRef } from "react";
import { useLocalAxios } from "@/app/api/axios";
import {
  ProjectFull,
  ProjectProfilePreview,
  ProjectUpdate,
} from "@/types/admin/Project";
import ProjectAdminProfileCard from "@/components/project/ProjectAdminProfileCard";
import CustomAvatar from "@/components/CustomAvatar";
import MemberSelectionDrawer from "@/components/member/MemberSelectionDrawer";
import ProfileMultipleSelectionDrawer from "@/components/profile/ProfileMultipleSelectionDrawer";

import ProfileMutipleSelectionModal from "@/components/profile/ProfileMultipleSelectModal";
import next from "next";
import ProfileCardProp from "@/types/talent/ProfileCardProp";
import DetailProfileCard from "@/components/profile/detailprofilecard";
import ProjectDetailProfileCard from "@/components/project/ProjectDetailProfileCard";
import MemberSelectionModal from "@/components/profile/ProfileMultipleSelectionDrawer";
import MemberMultipleSelectModal from "@/components/member/MemberMultipleSelectModal";
import ProjectSelectionModal from "@/components/project/ProjectSelectModal";
import { NetworkingCreateModal } from "@/components/networking/NetworkingCreateModal";
import { NetworkingSearchCondition } from "@/types/networking/Networking";
import { NetworkingProjectCreateModal } from "@/components/networking/NetworkingProjectCreateModal";
import { PageState } from "@/types/techmap/common";

const filterLabelArray = [
  "전체",
  "발굴",
  "접촉",
  "활용도 검토",
  "중장기관리",
  "단기관리",
  "처우협의",
  "처우결렬",
  "입사 포기",
  "입사 대기",
  "입사",
];
const filterValueArray: string[] = [
  "NONE",
  "FOUND", // 발굴
  "CONTACT", // 접촉
  "USAGE_REVIEW", // 활용도 검토
  "MID_LONG_TERM_MANAGE", // 중/장기 관리
  "SHORT_TERM_MANAGE", // 단기관리
  "인터뷰1", //  인터뷰
  "인터뷰2", //  인터뷰
  "인터뷰3", // interview3
  "for_interview", // 인터뷰 4
  "NEGOTIATION", // 처우협의
  "NEGOTIATION_DENIED", // 처우결렬
  "EMPLOY_ABANDON", // 입사포기
  "EMPLOY_WAITING", // 입사 대기
  "EMPLOYED", // 입사
];

interface PageInterface {
  profilePreviews: ProfileCardProp[];
  totalPages: number;
  totalElements: number;
  currentPage: number;
  size: number;
  filteredArray: number[];
}

export default function ProjectDetailPage({
  params,
}: {
  params: { techmapProjectId: number };
}) {
  const projectId = params.techmapProjectId;
  const [isPrivate, setIsPrivate] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [filteredNumArray, setFilteredNumArray] = useState<number[]>([]);
  const [pageData, setPageData] = useState<PageInterface>();
  const [isOpenProfileMultipleSection, setIsOpenProfileMultipleSelection] =
    useState(false);
  const [isProjectSelectionOpen, setIsOpenProjectSelection] =
    useState<boolean>(false);
  const [project, setProject] = useState<ProjectFull>();
  const [filteredProfiles, setFilteredProfiles] = useState<ProfileCardProp[]>();
  const [pageStateData, setPageStateData] = useState<PageState>({
    pageNumber: 0,
    size: 5,
  });
  const [networkingSearchCondition, setSearchCondition] =
    useState<NetworkingSearchCondition>({
      targetYear: 0,
      departmentId: 0,
      category: "",
    });

  const [projectUpdate, setProjectUpdate] = useState<ProjectUpdate>();
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [allIdSelected, setAllIdSelected] = useState(false);
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];
  const [maxCount, setMaxCount] = useState(0);
  const localAxios = useLocalAxios();
  const baseSetting = async () => {
    // const response = await localAxios.get<ProjectFull>(`/project/${projectId}`);
    // console.log("received Details", response.data);
    // setMaxCount(response.data.poolSize);
    // // setMaxCount(response.data.count);
    // const sum = response.data.filteredArray.reduce((acc, cur) => acc + cur, 0);
    // const nextFilteredArray = [sum, ...response.data.filteredArray];
    // setIsPrivate(response.data.isPrivate);
    // setFilteredNumArray(nextFilteredArray);
    // setProject(response.data);
    // setProjectUpdate({
    //   title: response.data.title,
    //   targetMemberCount: response.data.targetMemberCount,
    //   targetYear: response.data.targetYear,
    //   isPrivate: response.data.isPrivate,
    //   projectType: response.data.projectType.toString(),
    //   targetCountry: response.data.targetCountry,
    //   responsibleMemberId: response.data.responsibleMemberId,
    //   projectMembers: response.data.projectMembers.map((pm) => pm.memberId),
    //   projectProfiles: response.data.projectProfilesPreviewDtos.map(
    //     (pp) => pp.profileId,
    //   ),
    //   targetJobRanks: response.data.targetJobRanksIds,
    //   targetDepartmentId: response.data.projectDepartment.departmentId,
    // });
    const response = await localAxios.post(
      `/techmap-project/${params.techmapProjectId}/profiles`,
      pageStateData,
    );
    if (response.status === 200) {
      const sum = response.data.filteredArray.reduce(
        (acc: any, cur: any) => acc + cur,
        0,
      );
      const nextFilteredArray = [sum, ...response.data.filteredArray];
      setFilteredNumArray(nextFilteredArray);
      setPageData(response.data);
      setFilteredProfiles(response.data.profilePreviews);
    }
  };
  // const [flag, setFlag] = useState<boolean>(false);
  useEffect(() => {
    baseSetting();
  }, [pageStateData]);

  // useEffect(() => {
  //   if (project?.projectProfilesPreviewDtos) {
  //     setFilteredProfiles(project.projectProfilesPreviewDtos);
  //   }
  // }, [project?.projectProfilesPreviewDtos]);

  const toggleMemberSelectionDrawer = () => {
    setIsMemberSelectionOpen(!isMemberSelectionOpen);
  };

  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);

  const filterButtons = useMemo(
    () =>
      filterLabelArray.map((label, i) => (
        <Button
          key={i}
          color="light"
          className="relative my-2 w-full"
          onClick={() => filterProfile(i)}
        >
          <span className="text-base">{label}</span>
          <Badge color="gray" className="absolute bottom-2 right-2 text-base">
            {filteredNumArray[i]}
          </Badge>
        </Button>
      )),
    [filterLabelArray, filteredNumArray],
  );

  const filterProfile = (idx: number) => {
    // const sum = response.data.filteredArray.reduce(
    //   (acc: any, cur: any) => acc + cur,
    //   0,
    // );
    // const nextFilteredArray = [sum, ...response.data.filteredArray];
    // setFilteredNumArray(nextFilteredArray);
    // pageData
    // if (project) {
    if (idx == 0) {
      const nextFilteredArray = pageData?.profilePreviews;
      setFilteredProfiles(nextFilteredArray);
    } else {
      // const nextFilteredArray = project.projectProfilesPreviewDtos.filter(
      const nextFilteredArray = pageData?.profilePreviews.filter((p) => {
        return idx == filterValueArray.indexOf(p.employStatus);
      });
      // const nextFilteredArray = pageData?.profilePreviews.filter
      setFilteredProfiles(nextFilteredArray);
    }
    // }
  };

  const toggleIsOpenProfileMultipleSelection = () => {
    setIsOpenProfileMultipleSelection(!isOpenProfileMultipleSection);
  };

  const toggleProjectSelectionDrawer = () => {
    setIsOpenProjectSelection(!isProjectSelectionOpen);
  };

  const lockButton = useMemo(() => {
    return isPrivate ? (
      <Button
        color="light"
        onClick={() => setIsPrivate(false)}
        className="flex size-8 flex-col items-center"
      >
        <HiLockClosed className="size-5" />
        {/* 잠금 */}
      </Button>
    ) : (
      <Button
        color="light"
        onClick={() => setIsPrivate(true)}
        className="flex size-8 flex-col items-center"
      >
        <HiLockOpen className="size-5" />
        {/* 잠금 해제 */}
      </Button>
    );
  }, [isPrivate]);

  const updateProject = async (nextProjectUpdate: ProjectUpdate) => {
    localAxios
      .put(`/project/${projectId}`, nextProjectUpdate)
      .then((response) => {
        if (response.status == 200) {
          alert("수정에 성공하였습니다.");
          baseSetting();
        } else {
          alert("잠시 후 다시 시도 해주세요.");
        }
      });
  };
  const updateProjectProfiles = async (nextProjectUpdate: number[]) => {
    localAxios
      .put(`/techmap-project/${projectId}/update-profile`, nextProjectUpdate)
      .then((response) => {
        if (response.status == 200) {
          alert("등록에 성공하였습니다.");
          baseSetting();
        } else {
          alert("잠시 후 다시 시도 해주세요.");
        }
      });
    baseSetting();
  };

  const deleteProjectProfiles = async (nextProjectUpdate: number[]) => {
    localAxios
      .delete(`/techmap-project/${projectId}/update-profile`, {
        data: nextProjectUpdate,
      })
      .then((response) => {
        if (response.status == 200) {
          alert("삭제에 성공하였습니다.");
          baseSetting();
        } else {
          alert("잠시 후 다시 시도 해주세요.");
        }
      });

    baseSetting();
  };

  const updateProjectMembers = async (nextProjectUpdate: ProjectUpdate) => {
    localAxios
      .put(`/techmap-project/${projectId}/update-profile`, nextProjectUpdate)
      .then((response) => {
        if (response.status == 200) {
          alert("수정에 성공하였습니다.");
          baseSetting();
        } else {
          alert("잠시 후 다시 시도 해주세요.");
        }
      });
    setSelectedIds([]);
    baseSetting();
  };
  const onCloseProfileSelection = async (selectedProfileIds: number[]) => {
    updateProjectProfiles(selectedProfileIds);
    if (projectUpdate?.projectProfiles) {
      const nextProjectUpdate = {
        ...projectUpdate,
        projectProfiles:
          projectUpdate?.projectProfiles.concat(selectedProfileIds),
      };

      updateProjectProfiles(selectedProfileIds);
      setProjectUpdate(nextProjectUpdate);
    }
  };

  const onCloseMemberSelection = async (selectedMemberIds: number[]) => {
    if (projectUpdate?.projectMembers) {
      const nextProjectUpdate = {
        ...projectUpdate,
        projectMembers: projectUpdate?.projectMembers
          ? projectUpdate.projectMembers.concat(selectedMemberIds)
          : selectedMemberIds,
      };
      updateProjectMembers(nextProjectUpdate);
      setProjectUpdate(nextProjectUpdate);
    }
  };

  const onCloseProjectSelection = async (targetProjectId: number) => {
    const copyResponse = await localAxios.post(`project/copy/${projectId}`, {
      targetProjectId: targetProjectId,
      profileIds: selectedIds,
    });

    if (copyResponse.status == 200) {
      alert("복사에 성공하였습니다.");
      setSelectedIds([]);
    } else {
      alert("잠시 후 다시 시도해주세요.");
    }
  };

  const handlePage = (event: any) => {
    if (typeof event === "number") {
      const value = event;
      setPageStateData((prevState) => ({
        ...prevState,
        ["pageNumber"]: value - 1,
      }));
    } else {
      const { name, value } = event.target;
      setPageStateData((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  const handleSelectAllId = () => {
    if (allIdSelected) {
      setSelectedIds([]);
      setAllIdSelected(false);
    } else {
      const arr: number[] = [];
      filteredProfiles?.map((profile) => {
        arr.push(profile.profileId);
      });
      setSelectedIds(arr);
      setAllIdSelected(true);
    }
  };

  const handleDeleteSelectedProfiles = () => {
    if (projectUpdate?.projectProfiles) {
      const nextProjectProfiles = projectUpdate?.projectProfiles.filter(
        (id) => !selectedIds.includes(id),
      );
      const nextProjectUpdate = {
        ...projectUpdate,
        projectProfiles: nextProjectProfiles,
      };

      // updateProjectProfiles(nextProjectUpdate);
    }
    deleteProjectProfiles(selectedIds);

    // selected ids 삭제
  };

  const handleDeleteIndividualProfiles: (id: number) => void = (id: number) => {
    if (projectUpdate?.projectProfiles) {
      const nextProjectProfiles = projectUpdate?.projectProfiles.filter(
        (pid) => pid != id,
      );
      const nextProjectUpdate = {
        ...projectUpdate,
        projectProfiles: nextProjectProfiles,
      };
      // updateProjectProfiles(nextProjectUpdate);
    }

    // selected ids 삭제
  };
  const handleSelectId = (id: number) => {
    if (selectedIds.includes(id)) {
      const nextSelectedIds = selectedIds.filter((i) => i != id);
      setSelectedIds(nextSelectedIds);
    } else {
      const nextSelectedIds = [...selectedIds, id];
      setSelectedIds(nextSelectedIds);
    }
  };
  const handleCopySelectedProfiles = () => {
    if (confirm("선택하신 인재를 다른 프로젝트로 복사하시겠습니까?")) {
      {
        toggleProjectSelectionDrawer();
      }
    }
  };

  const handleCopytoOtherProjectIndividualProfile = (profileId: number) => {
    if (confirm("선택하신 인재를 다른 프로젝트로 복사하시겠습니까?")) {
      {
        setSelectedIds([profileId]);
        toggleProjectSelectionDrawer();
      }
    }
  };
  const toggleLock = () => {
    const now = isPrivate;
    if (projectUpdate?.projectProfiles) {
      const nextMembers = project
        ? !now
          ? [project?.responsibleMemberId]
          : projectUpdate?.projectMembers
            ? projectUpdate.projectMembers
            : []
        : [];
      const nextProjectUpdate = {
        ...projectUpdate,
        isPrivate: !now,
        projectMembers: nextMembers,
      };
      if (projectUpdate.projectMembers) {
        if (!now && projectUpdate?.projectMembers.length > 1) {
          if (
            confirm(
              "나만 보기를 활성화할 경우 같이 보는 사용자가 제거됩니다. 계속하시겠습니까?",
            )
          ) {
            setProjectUpdate(nextProjectUpdate);
            updateProjectMembers(nextProjectUpdate);
            setIsPrivate(!now);
          }
        } else {
          setProjectUpdate(nextProjectUpdate);
          updateProjectMembers(nextProjectUpdate);
          setIsPrivate(!now);
        }
      }
    }
  };
  const networkTarget = useRef<number>(0);
  const handleAddingNetworkingIndividualProfile = (profileId: number) => {
    networkTarget.current = profileId;
    toggleNetworkingCreateModal();
  };
  const [isNetworkingModalOpen, setIsNetworkingModalOpen] =
    useState<boolean>(false);
  const toggleNetworkingCreateModal = () => {
    setIsNetworkingModalOpen(!isNetworkingModalOpen);
  };
  const handleOnCloseNetworkingModal = async (
    networkingId: number,
    profileId: number,
  ) => {
    const networkingUpdateResponse = await localAxios.put(
      `networking/profile/${networkingId}`,
      {
        networkingProfileIds: [profileId],
      },
    );
    if (networkingUpdateResponse.status == 200) {
      console.log("네트워킹 등록에 성공하였습니다.");
    } else {
      console.log("잠시 후 다시 시도해주세요.");
    }
  };

  return (
    <div className="p-2">
      <div className="grid grid-cols-6 gap-2">
        <div className="col-span-1">
          <div className="h-fit rounded-md border border-slate-200 bg-white p-5">
            <div className="justfiy-bet flex">
              <h2 className="my-5 text-xl font-bold">현황</h2>
            </div>

            <hr></hr>
            <div className="space-y-1 text-xs">{filterButtons}</div>
          </div>
        </div>
        <div className="col-span-5 rounded-md border border-slate-200 bg-white p-5">
          <div className="m-10 flex items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              {/* <h2 className="text-2xl font-bold">{project?.title}</h2> */}
              {/* <Button
                color="light"
                size="sm"
                onClick={() => console.log("edit")}
                className="flex size-8 flex-row items-center"
              >
                <HiOutlinePencilAlt className="size-5" />
              </Button> */}
              {/* <div className="flex items-center space-x-2 p-1">
                {project?.targetJobRanks.map((jr, index) => (
                  <Badge
                    className="text-md"
                    key={index}
                    color={coloredBadge[index % 5]}
                  >
                    {jr}
                  </Badge>
                ))}
              </div> */}
            </div>

            <div className="flex flex-row items-center gap-2 space-x-2">
              <Button
                className="bg-primary"
                onClick={toggleIsOpenProfileMultipleSelection}
              >
                인재 추가하기
              </Button>
            </div>
          </div>
          <div className="flex items-center justify-between gap-2">
            <div className="ml-10 flex items-center gap-3">
              <Checkbox checked={allIdSelected} onChange={handleSelectAllId} />
              <h5 className="text-md w-24 font-semibold">
                {/* 이게 전체 */}총 : {pageData?.totalElements}명
                {/* 나중에 이걸로 바꿀 예정
                총 : {filteredProfiles?.length}명
                 */}
              </h5>
            </div>
            <div className="mr-20 flex flex-row gap-1">
              <button className=" items-center whitespace-nowrap rounded-md border p-2">
                활용단계 변경 (<span>{selectedIds.length}</span>)
              </button>
              <button className="whitespace-nowrap rounded-md border p-2">
                액셀 파일 저장(<span>{selectedIds.length}</span>)
              </button>
              <button className="whitespace-nowrap rounded-md border p-2">
                워드 파일 저장(<span>{selectedIds.length}</span>)
              </button>
              <button
                className="whitespace-nowrap rounded-md border p-2"
                onClick={handleCopySelectedProfiles}
              >
                다른 tech으로 복사 (<span>{selectedIds.length}</span>)
              </button>
              <button
                className="whitespace-nowrap rounded-md border p-2"
                onClick={handleDeleteSelectedProfiles}
              >
                tech에서 삭제 (<span>{selectedIds.length}</span>)
              </button>
            </div>

            {/* <Dropdown
              label={`저장(${0})`}
              theme={{
                arrowIcon: "ml-2 inline-block size-5 self-center",
                inlineWrapper:
                  "flex flex-row items-center whitespace-nowrap p-2 text-xl",
              }}
            >
              <DropdownItem onClick={() => console.log(selectedIds)}>
                엑셀로 저장
              </DropdownItem>
              <DropdownItem onClick={() => console.log(selectedIds)}>
                워드로 저장
              </DropdownItem>
            </Dropdown> */}
          </div>
          <div className="mt-4 space-y-4 bg-white">
            {filteredProfiles?.map((fp) => (
              <div key={fp.profileId} className="relative">
                <ProjectDetailProfileCard
                  key={fp.profileId}
                  props={{
                    ...fp,
                    checked: selectedIds.includes(fp.profileId),
                    handleCheck: () => handleSelectId(fp.profileId),
                    handleOnDeleteProject: () =>
                      handleDeleteIndividualProfiles(fp.profileId),
                    handleOnAddingNetworking: (id: number) =>
                      handleAddingNetworkingIndividualProfile(id),
                    handleOnCopyOtherProject: () =>
                      handleCopytoOtherProjectIndividualProfile(fp.profileId),
                    handleOnAddingtechmapProject: () => { },
                  }}
                />
              </div>
            ))}

            <Pagination
              currentPage={pageStateData.pageNumber + 1}
              nextLabel=""
              onPageChange={handlePage}
              previousLabel=""
              showIcons
              totalPages={pageData?.totalPages ?? 2}
            />
          </div>
        </div>
      </div>
      {project && (
        <MemberMultipleSelectModal
          title="프로젝트 담당자 추가"
          // size="2xl"
          openers={projectUpdate?.projectMembers}
          isOpen={isMemberSelectionOpen}
          toggleDrawer={toggleMemberSelectionDrawer}
          onClose={onCloseMemberSelection}
        />
      )}
      {filteredProfiles && (
        <ProfileMutipleSelectionModal
          isOpen={isOpenProfileMultipleSection}
          onClose={onCloseProfileSelection}
          title={"프로젝트 인재 선택"}
          toggleDrawer={toggleIsOpenProfileMultipleSelection}
          size={"2xl"}
          domain="techmap"
          domainId={projectId}
        />
      )}
      {/* {project && (
        <ProjectSelectionModal
          title="프로젝트 담당자 추가"
          size="2xl"
          isOpen={isProjectSelectionOpen}
          toggleDrawer={toggleProjectSelectionDrawer}
          onClose={onCloseProjectSelection}
          projectId={projectId}
        />
      )} */}
      {/* {project && (
        <NetworkingProjectCreateModal
          showModal={isNetworkingModalOpen}
          closeModal={toggleNetworkingCreateModal}
          size={"3xl"}
          onClose={handleOnCloseNetworkingModal}
          profileId={networkTarget.current}
          searchCondition={networkingSearchCondition}
        />
      )} */}
    </div>
  );
}
