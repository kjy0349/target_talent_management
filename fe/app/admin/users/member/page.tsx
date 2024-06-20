"use client";
import { Badge, Button, Pagination, Select, theme } from "flowbite-react";
import MemberListRow from "@/components/member/MemberListRow";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { twMerge } from "tailwind-merge";
import {
  AuthorityAdminSummary,
  DepartmentAdminSummary,
  MemberAdminFull,
  MemberAdminSearchCondition,
  MemberAdminSummary,
} from "@/types/admin/Member";
import { useLocalAxios } from "@/app/api/axios";
import { MemberCreateModal } from "@/components/member/MemberCreateModal";
import { HiCheck, HiDocument, HiX } from "react-icons/hi";
import { MemberSearchResult } from "@/types/member/member";
export default function AdminMemberPage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [maxPage, setMaxPage] = useState<number>(1);
  const [size, setSize] = useState(10);
  const router = useRouter();
  const [members, setMembers] = useState<MemberAdminFull[]>([]);
  const localAxios = useLocalAxios();
  const [showMemberCreateModal, setShowMemberCreateModal] = useState(false);
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];
  const [filter, setFilter] = useState<MemberAdminSearchCondition>({
    keyword: "",
    departmentName: "",
    withDelete: false,
  });
  const [depts, setDepts] = useState<DepartmentAdminSummary[]>();
  const [authorities, setAuthorities] = useState<AuthorityAdminSummary[]>();

  useEffect(() => {
    const getDept = async () => {
      const deptResponse =
        await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);
      setDepts(deptResponse.data);
    };
    const getAuth = async () => {
      const authResponse =
        await localAxios.get<AuthorityAdminSummary[]>(`/admin/authority`);
      setAuthorities(authResponse.data);
    };
    getDept();
    getAuth();
  }, []);

  const handleOpenMemberShowModal = () => {
    setShowMemberCreateModal(true);
  };

  const handleCloseMemberShowModal = () => {
    setShowMemberCreateModal(false);
  };
  const getMembers = async (page: number) => {
    const response = await localAxios.post<MemberSearchResult>(
      `/admin/member/search?page=${page}&&size=${size}`,
      filter,
    );
    setMembers(response.data.memberAdminFullDtos);
    setMaxPage(response.data.count);
  };

  useEffect(() => {
    // getMemberCount();
    getMembers(currentPage - 1);
  }, [currentPage]);

  useEffect(() => {
    // getMemberCount();
    getMembers(currentPage - 1);
  }, [size]);
  useEffect(() => {
    // getMemberCount();
    getMembers(currentPage - 1);
  }, [filter]);

  const deleteMember = (id: number) => {
    const nextMember = members.filter((m) => m.id !== id);
    setMembers(nextMember);
  };
  const formDate = (date: Date) => {
    const newDate = new Date(date);
    return `${newDate.getFullYear()}-${newDate.getMonth() + 1}-${newDate.getUTCDate()}`;
  };

  const selected = useRef<number[]>([]);
  const selectedAuth = useRef<number>();

  const handleOnChangeAuth = (e: React.ChangeEvent<HTMLSelectElement>) => {
    selectedAuth.current = Number(e.target.value);
  };
  const handleDeleteMembers = async () => {
    if (selected.current.length == 0) {
      alert("선택된 회원이 없습니다.");
      return;
    }
    const deleteResponse = await localAxios.put(`/admin/member`, {
      memberIds: selected.current,
    });
    if (deleteResponse.status == 200) {
      alert("회원탈퇴에 성공하였습니다.");
      getMembers(0);
    } else {
      alert("회원탈퇴에 실패하였습니다.");
    }
  };

  const handleUpdateMembersAuthority = async () => {
    if (selectedAuth.current == 0) {
      alert("권한을 선택해주세요.");
      return;
    }
    const updateResponse = await localAxios.put(`/admin/member/authority`, {
      memberIds: selected.current,
      authorityId: selectedAuth.current,
    });
    if (updateResponse.status == 200) {
      alert("갱신에 성공하였습니다.");
      getMembers(0);
    } else {
      alert("갱신에 실패하였습니다.");
    }
  };

  return (
    <div className="flex min-h-screen flex-col space-y-5 bg-white p-10">
      <h3 className="my-5 text-3xl font-bold">회원 관리 현황</h3>
      <div className="flex space-x-5">
        <div className="relative w-full overflow-x-auto">
          <div className="relative flex flex-col overflow-x-auto">
            <div className="flex">
              <span className="ml-2 text-lg font-bold">
                총원
                {" " + members.length}명
              </span>
            </div>
            <div className="mb-4 flex justify-between">
              <div>
                <div className="mt-2 flex items-center gap-4">
                  <span className="m-2 text-base">정렬</span>
                  <Select
                    onChange={(e) => {
                      setFilter({
                        ...filter,
                        departmentName: e.target.value,
                      });
                    }}
                    name="businessUnit"
                  >
                    <option value={""}>사업부</option>
                    {depts?.map((dept) => (
                      <option key={dept.departmentId} value={dept.name}>
                        {dept.name}
                      </option>
                    ))}
                    {/* {memberFilter?.businessUnits.map((bu, index) => (
                        <option key={index} value={bu}>
                          {bu}
                        </option>
                      ))} */}
                  </Select>
                  <Select
                    name="sizes"
                    onChange={(e) => setSize(Number(e.target.value))}
                  >
                    <option value={10}>10명씩 보기</option>
                    <option value={5}>5명씩 보기</option>
                  </Select>
                  <div className="flex flex-row items-center gap-2">
                    <input
                      id="showWithDeleted"
                      type="checkbox"
                      checked={filter.withDelete}
                      onChange={(e) =>
                        setFilter({
                          ...filter,
                          withDelete: e.target.checked,
                        })
                      }
                    />
                    <label htmlFor="showWithDeleted">탈퇴 회원 같이 보기</label>
                  </div>
                </div>
              </div>
              <div className="mt-2">
                <Button
                  className="font-bold "
                  onClick={handleOpenMemberShowModal}
                >
                  신규 회원 가입
                </Button>
              </div>
            </div>
            {members?.length ? (
              <table className="w-full border-collapse text-left text-sm dark:text-gray-400 rtl:text-right">
                <thead className="bg-gray-100 uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  <tr>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      <HiCheck />
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      이름
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      사업부
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      부서
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      담당업무
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      Knox ID
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      회원 등급
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      가입일
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      방문횟수
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      최종접속일
                    </th>
                    <th scope="col" className="px-4 py-3 text-xs font-semibold">
                      보안서약서
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {/* {members?.map((m) => (
                      // <MemberListRow key={m.id} member={m} />
                    ))} */}
                  {members?.map((m) => (
                    <tr
                      key={m.id}
                      className={`${m.lastDeletedAt ? "bg-zinc-100" : "hover:bg-slate-50 "}`}
                    >
                      <td className={`px-4 py-3 text-xs font-semibold `}>
                        <input
                          type="checkbox"
                          onChange={(e) => {
                            if (e.target.checked) {
                              const nextSelected = [...selected.current, m.id];
                              selected.current = nextSelected;
                            } else {
                              const nextSelected = selected.current.filter(
                                (id) => id != m.id,
                              );
                              selected.current = nextSelected;
                            }
                          }}
                        />
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.name}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.departmentName}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.teamName}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.roleName}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.knoxId}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.authority}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {formDate(m.createdAt)}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        {m.visitCount ? m.visitCount : 0}
                      </td>
                      <td className="px-4 py-3 text-xs font-semibold">
                        <div>
                          {m.lastAccessDate ? formDate(m.lastAccessDate) : "-"}
                        </div>
                        <div className="text-">
                          {m.lastDeletedAt
                            ? `(${formDate(m.lastDeletedAt)})`
                            : ""}
                        </div>
                      </td>
                      <td className="flex gap-1 px-4 py-3 text-lg font-semibold">
                        <HiDocument />

                        {m.isSecuritySigned ? (
                          <HiCheck className="text-green-300" />
                        ) : (
                          <HiX className="text-red-500" />
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div>해당하는 데이터가 없습니다.</div>
            )}
            <div className="pagenation-part mt-2 flex w-full justify-center">
              <Pagination
                currentPage={currentPage}
                nextLabel=""
                onPageChange={(page) => setCurrentPage(page)}
                previousLabel=""
                showIcons
                totalPages={maxPage}
                theme={{
                  pages: {
                    base: twMerge(theme.pagination.pages.base, "mt-0"),
                    next: {
                      base: twMerge(
                        theme.pagination.pages.next.base,
                        "w-[2.5rem] px-1.5 py-1.5",
                      ),
                      icon: "h-6 w-6",
                    },
                    previous: {
                      base: twMerge(
                        theme.pagination.pages.previous.base,
                        "w-[2.5rem] px-1.5 py-1.5",
                      ),
                      icon: "h-6 w-6",
                    },
                    selector: {
                      base: twMerge(
                        theme.pagination.pages.selector.base,
                        "w-[2.25rem] py-2 text-sm focus:border-primary",
                      ),
                    },
                  },
                }}
              />
            </div>
            <div className="flex w-full items-center justify-end gap-1">
              선택 회원 권한 변경
              <select
                className="m-2 rounded border px-4 py-2 text-sm"
                onChange={handleOnChangeAuth}
              >
                <option value={0}>권한명</option>
                {authorities?.map((a) => (
                  <option key={a.id} value={a.id}>
                    {a.authName}
                  </option>
                ))}
              </select>
              으로
              <Button onClick={handleUpdateMembersAuthority}>변경</Button>
              <Button color="failure" onClick={handleDeleteMembers}>
                탈퇴
              </Button>
            </div>
          </div>
          <MemberCreateModal
            showModal={showMemberCreateModal}
            closeModal={handleCloseMemberShowModal}
            size={"3xl"}
            rebase={getMembers}
          />
        </div>
      </div>
    </div>
  );
}
