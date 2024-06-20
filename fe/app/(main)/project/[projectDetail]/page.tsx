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
  HiX,
  HiCheck,
} from "react-icons/hi";
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
import { useParams } from "next/navigation";
import ProfileMutipleSelectionModal from "@/components/profile/ProfileMultipleSelectModal";
import next from "next";
import ProfileCardProp, {
  ProjectProfileCardProp,
} from "@/types/talent/ProfileCardProp";
import DetailProfileCard from "@/components/profile/detailprofilecard";
import ProjectDetailProfileCard from "@/components/project/ProjectDetailProfileCard";
import MemberSelectionModal from "@/components/profile/ProfileMultipleSelectionDrawer";
import MemberMultipleSelectModal from "@/components/member/MemberMultipleSelectModal";
import ProjectSelectionModal from "@/components/project/ProjectSelectModal";
import { NetworkingCreateModal } from "@/components/networking/NetworkingCreateModal";
import { NetworkingSearchCondition } from "@/types/networking/Networking";
import { NetworkingProjectCreateModal } from "@/components/networking/NetworkingProjectCreateModal";
import Spinner from "@/components/common/Spinner";
import { HiCircleStack } from "react-icons/hi2";
import { AxiosError } from "axios";
import { useAuthStore } from "@/stores/auth";

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
  "인터뷰1", 
  "인터뷰2", 
  "인터뷰3", 
  "for_interview",
  "NEGOTIATION", // 처우협의
  "NEGOTIATION_DENIED", // 처우결렬
  "EMPLOY_ABANDON", // 입사포기
  "EMPLOY_WAITING", // 입사 대기
  "EMPLOYED", // 입사
];

export default function ProjectDetailPage() {
  const [loading, setLoading] = useState<boolean>(false);
  const param = useParams();
  const projectId = Number(param.projectDetail);
  const [isPrivate, setIsPrivate] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [filteredNumArray, setFilteredNumArray] = useState<number[]>([]);
  const [isOpenProfileMultipleSection, setIsOpenProfileMultipleSelection] =
    useState(false);
  const [isProjectSelectionOpen, setIsOpenProjectSelection] =
    useState<boolean>(false);
  const [project, setProject] = useState<ProjectFull>();
  const [filteredProfiles, setFilteredProfiles] =
    useState<ProjectProfileCardProp[]>();
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
  const [isEditingTitle, setIsEditingTitle] = useState<boolean>(false);
  const baseSetting = async () => {
    const response = await localAxios.get<ProjectFull>(`/project/${projectId}`);
    setMaxCount(response.data.poolSize);
    // setMaxCount(response.data.count);
    const sum = response.data.filteredArray.reduce((acc, cur) => acc + cur, 0);
    const nextFilteredArray = [sum, ...response.data.filteredArray];
    setIsPrivate(response.data.isPrivate);
    setFilteredNumArray(nextFilteredArray);
    setProject(response.data);
    setProjectUpdate({
      title: response.data.title,
      targetMemberCount: response.data.targetMemberCount,
      targetYear: response.data.targetYear,
      isPrivate: response.data.isPrivate,
      projectType: response.data.projectType.toString(),
      targetCountry: response.data.targetCountry,
      responsibleMemberId: response.data.responsibleMemberId,
      projectMembers: response.data.projectMembers.map((pm) => pm.memberId),
      projectProfiles: response.data.projectProfilesPreviewDtos.map(
        (pp) => pp.profileId,
      ),
      targetJobRanks: response.data.targetJobRanksIds,
      targetDepartmentId: response.data.projectDepartment.departmentId,
    });
  };

  useEffect(() => {
    if (project && project.title) {
      setLoading(true);
    }
    const recentString = localStorage.getItem("recentProjects");
    let recent = recentString ? JSON.parse(recentString) : [];

    if (projectId) {
      const index = recent.indexOf(projectId);
      if (index !== -1) {
        recent.splice(index, 1);
      }
      recent.push(projectId);

      if (recent.length > 5) {
        recent = recent.slice(recent.length - 5);
      }

      localStorage.setItem("recentProjects", JSON.stringify(recent));
    }
  }, [project]);

  // const [flag, setFlag] = useState<boolean>(false);
  useEffect(() => {
    baseSetting();
  }, []);

  useEffect(() => {
    if (project?.projectProfilesPreviewDtos) {
      setFilteredProfiles(project.projectProfilesPreviewDtos);
    }
  }, [project?.projectProfilesPreviewDtos]);
  const toggleMemberSelectionDrawer = () => {
    setIsMemberSelectionOpen(!isMemberSelectionOpen);
  };
  const closeMemberSelectionModal = () => {
    setIsMemberSelectionOpen(false);
  };

  const [isMemberSelectionOpen, setIsMemberSelectionOpen] = useState(false);

  const filterButtons = useMemo(
    () => (
      <div className="h-fit w-full whitespace-nowrap rounded-md border-t bg-white p-2">
        {filterLabelArray.map(
          (label, i) =>
            i > 0 && (
              <button
                key={i}
                className="my-2 flex h-fit w-full flex-row justify-between rounded-md p-1 text-base hover:bg-yellow-100"
                onClick={() => filterProfile(i)}
              >
                <span>{label}</span>
                <span>{filteredNumArray[i]}</span>
              </button>
            ),
        )}
      </div>
    ),
    [filterLabelArray, filteredNumArray],
  );

  const filterProfile = (idx: number) => {
    if (project) {
      if (idx == 0) {
        const nextFilteredArray = [...project?.projectProfilesPreviewDtos];
        setFilteredProfiles(nextFilteredArray);
        return;
      } else {
        const nextFilteredArray = project.projectProfilesPreviewDtos.filter(
          (p) => {
            return idx == filterValueArray.indexOf(p.employStatus);
          },
        );
        setFilteredProfiles(nextFilteredArray);
      }
    }
  };

  const toggleIsOpenProfileMultipleSelection = () => {
    setIsOpenProfileMultipleSelection(!isOpenProfileMultipleSection);
  };

  const toggleProjectSelectionDrawer = () => {
    setIsOpenProjectSelection(!isProjectSelectionOpen);
  };
  const auth = useAuthStore();
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
  const updateProjectProfiles = async (nextProjectUpdate: ProjectUpdate) => {
    localAxios
      .put(`/project/profiles/${projectId}`, nextProjectUpdate)
      .then((response) => {
        if (response.status == 200) {
          alert("인재 변경에 성공하였습니다.");
          baseSetting();
        }
      });

    baseSetting();
  };
  const updateProjectMembers = async (nextProjectUpdate: ProjectUpdate) => {
    localAxios
      .put(`/project/members/${projectId}`, nextProjectUpdate)
      .then((response) => {
        if (response.status == 200) {
          alert("정보를 업데이트했습니다.");
          baseSetting();
        } else {
          alert("잠시 후 다시 시도 해주세요.");
        }
      });
    setSelectedIds([]);
    baseSetting();
  };
  const onCloseProfileSelection = async (selectedProfileIds: number[]) => {
    if (projectUpdate?.projectProfiles) {
      const nextProjectUpdate = {
        ...projectUpdate,
        projectProfiles:
          projectUpdate?.projectProfiles.concat(selectedProfileIds),
      };
      updateProjectProfiles(nextProjectUpdate);
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
      setIsMemberSelectionOpen(false);
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
      updateProjectProfiles(nextProjectUpdate);
    }

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
      updateProjectProfiles(nextProjectUpdate);
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
    if (
      auth &&
      auth.id != project?.responsibleMemberId &&
      auth.authLevel != undefined &&
      auth.authLevel > 1
    ) {
      alert("권한이 없습니다.");
      return;
    }
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
    openNetworkingCreateModal();
  };
  const [isNetworkingModalOpen, setIsNetworkingModalOpen] =
    useState<boolean>(false);
  const openNetworkingCreateModal = () => {
    setIsNetworkingModalOpen(true);
  };

  const closeNetworkingCreateModal = () => {
    setIsNetworkingModalOpen(false);
  };

  const handleOnTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (project) {
      setProject({
        ...project,
        title: e.target.value,
      });
    }
  };
  const handleEdit = () => {
    setIsEditingTitle(true);
  };

  const handleSave = async () => {
    if (confirm("프로젝트 이름을 변경하시겠습니까?")) {
      const titleResponse = await localAxios.put(
        `/project/title/${projectId}`,
        {
          title: project?.title,
        },
      );

      if (titleResponse.status == 200) {
        alert("변경에 성공하였습니다.");
      }
    }
    setIsEditingTitle(false);
  };

  const handleCancel = () => {
    setIsEditingTitle(false);
  };

  const handelExcelDownload = async () => {
    if (selectedIds.length > 0) {
      const response = await localAxios.post(
        `/project/excel-download`,
        {
          projectId: projectId,
          profileIds: selectedIds,
        },
        { responseType: "blob" },
      );

      const contentDisposition = response.headers["Content-Disposition"];
      let filename = "download.xlsx"; // 기본 파일명
      if (contentDisposition) {
        const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        const matches = filenameRegex.exec(contentDisposition);
        if (matches != null && matches[1]) {
          filename = matches[1].replace(/['"]/g, "");
        }
      }

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename);
      document.body.appendChild(link);
      link.click();
    } else {
      alert("선택한 항목이 없습니다.");
    }
  };

  return loading && filteredProfiles ? (
    <div className="">
      <div className="grid grid-cols-6">
        <div className="col-span-1">
          <div className="h-fit rounded-md border-b border-slate-200 bg-white p-6">
            <button
              className="m-auto my-2 flex w-full flex-row justify-between pb-1 text-xl font-bold"
              onClick={() => filterProfile(0)}
            >
              <span>전체</span>
              <span>{filteredNumArray[0]}</span>
            </button>
            <hr></hr>
            {filterButtons}
          </div>
        </div>
        <div className="col-span-5 rounded-md border border-slate-200 bg-white">
          <div className="m-5 mb-2 flex items-center justify-between gap-4 p-5 pb-0">
            <div className="flex items-center gap-1">
              {isEditingTitle ? (
                <>
                  <input
                    type="text"
                    value={project?.title}
                    className="flex rounded-md px-2 py-1 font-bold"
                    onChange={handleOnTitleChange}
                  />
                  <button
                    className="rounded-md bg-green-500 p-1 text-white"
                    onClick={handleSave}
                  >
                    <HiCheck className="h-5 w-5" />
                  </button>
                  <button
                    className="rounded-md bg-red-500 p-1 text-white"
                    onClick={handleCancel}
                  >
                    <HiX className="h-5 w-5" />
                  </button>
                </>
              ) : (
                <>
                  <h2 className="text-2xl font-bold">{project?.title}</h2>
                  <Button
                    color="light"
                    size="sm"
                    onClick={() => setIsEditingTitle(true)}
                    className="flex size-8 flex-row items-center"
                  >
                    <HiOutlinePencilAlt className="size-5" />
                  </Button>
                </>
              )}

              <div className="ml-10 flex items-center space-x-2 p-1">
                {project?.targetJobRanks.map((jr, index) => (
                  <Badge
                    className="text-md"
                    key={index}
                    color={coloredBadge[index % 5]}
                  >
                    {jr}
                  </Badge>
                ))}
              </div>
            </div>

            <div className="flex flex-row items-center gap-2 space-x-2">
              <Button
                className="h-10 bg-primary font-bold"
                onClick={toggleIsOpenProfileMultipleSelection}
              >
                프로젝트에 인재 추가하기
              </Button>
              {isPrivate ? (
                <Button
                  color="light"
                  className="flex size-10 items-center"
                  onClick={() => toggleLock()}
                >
                  <HiLockClosed className="size-5" />
                  {/* 잠금 */}
                </Button>
              ) : (
                <Button
                  color="light"
                  className="flex size-10 items-center"
                  onClick={() => toggleLock()}
                >
                  <HiLockOpen className="size-5" />
                  {/* 잠금 해제 */}
                </Button>
              )}
              <div className="flex flex-row items-center">
                {/* <Avatar.Group> */}
                {project?.projectMembers.map((pm) => (
                  <CustomAvatar
                    ownerId={pm.memberId}
                    key={pm.memberId}
                    member={pm}
                    onDelete={(id: number) => console.log(id)}
                  />
                ))}
                <Avatar
                  img={HiPlus}
                  size="sm"
                  className="ml-2 size-5 cursor-pointer self-center"
                  onClick={() => toggleMemberSelectionDrawer()}
                />
                {/* </Avatar.Group> */}
              </div>
            </div>
          </div>
          <div className="flex items-center justify-between gap-2 px-5">
            <div className="ml-10 flex items-center gap-3">
              <Checkbox checked={allIdSelected} onChange={handleSelectAllId} />
              <h5 className="text-md w-24 font-semibold">
                총 {project?.poolSize}명
              </h5>
            </div>
            <div className="flex flex-row gap-1">
              {/* <button className=" items-center whitespace-nowrap rounded-md border p-2">
                활용단계 변경 (<span>{selectedIds.length}</span>)
              </button> */}
              {/* <button className="whitespace-nowrap rounded-md border p-2">
                액셀 파일 저장(<span>{selectedIds.length}</span>)
              </button> */}
              {/* <button className="whitespace-nowrap rounded-md border p-2">
                워드 파일 저장(<span>{selectedIds.length}</span>)
              </button> */}
              <button
                className="whitespace-nowrap rounded-md border border-slate-300 p-2 font-bold"
                onClick={handleCopySelectedProfiles}
              >
                다른 프로젝트로 복사 (<span>{selectedIds.length}</span>)
              </button>
              <button
                className="whitespace-nowrap rounded-md border border-slate-300 p-2 font-bold"
                onClick={handleDeleteSelectedProfiles}
              >
                프로젝트에서 삭제 (<span>{selectedIds.length}</span>)
              </button>
              <Dropdown
                label={`저장(${selectedIds.length})`}
                theme={{
                  arrowIcon: "ml-2 inline-block size-5 self-center",
                  inlineWrapper:
                    "flex flex-row items-center whitespace-nowrap p-2 text-xl",
                }}
              >
                <DropdownItem onClick={() => handelExcelDownload()}>
                  엑셀로 저장
                </DropdownItem>
                {/* <DropdownItem onClick={() => console.log(selectedIds)}>
                워드로 저장
              </DropdownItem> */}
              </Dropdown>
            </div>
          </div>
          <div className="mt-4 bg-white">
            {filteredProfiles
              ?.slice((currentPage - 1) * 3, currentPage * 3)
              .map((fp) => (
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
                      handleOnAddingtechmapProject: () => {},
                      handleReload: () => baseSetting(),
                    }}
                  />
                </div>
              ))}
            <Pagination
              currentPage={currentPage}
              onPageChange={setCurrentPage}
              totalPages={Math.ceil(filteredProfiles?.length / 3)}
              showIcons={true}
              nextLabel=""
              previousLabel=""
              className="mx-auto w-fit pb-5"
            />
          </div>
        </div>
      </div>
      {project && (
        <>
          <MemberMultipleSelectModal
            title="프로젝트 담당자 추가"
            // size="2xl"
            openers={projectUpdate?.projectMembers}
            isOpen={isMemberSelectionOpen}
            toggleDrawer={closeMemberSelectionModal}
            onClose={onCloseMemberSelection}
          />
          <ProfileMutipleSelectionModal
            isOpen={isOpenProfileMultipleSection}
            onClose={onCloseProfileSelection}
            title={"프로젝트 인재 선택"}
            toggleDrawer={toggleIsOpenProfileMultipleSelection}
            size={"2xl"}
            domain="project"
            domainId={projectId}
          />
          <ProjectSelectionModal
            title="프로젝트 복사"
            size="2xl"
            isOpen={isProjectSelectionOpen}
            toggleDrawer={toggleProjectSelectionDrawer}
            onClose={onCloseProjectSelection}
            projectId={projectId}
          />
          <NetworkingProjectCreateModal
            showModal={isNetworkingModalOpen}
            closeModal={closeNetworkingCreateModal}
            size={"3xl"}
            profileId={networkTarget.current}
            searchCondition={networkingSearchCondition}
          />
        </>
      )}
    </div>
  ) : (
    <div>
      <Spinner size={60} color="#ff6347" />
    </div>
  );
}
