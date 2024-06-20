import { Modal, Pagination } from "flowbite-react"
import { useLocalAxios } from "@/app/api/axios";
import { useEffect, useState } from "react";
import { MemberAdminFull, AuthorityAdminSummary, DepartmentAdminSummary, MemberAdminSearchCondition } from "@/types/admin/Member";
import { MemberSearchResult } from "@/types/member/member";
import { Table } from "flowbite-react";
import MemberListRow from "../member/MemberRowSimple";
import { HiSearch } from "react-icons/hi";

export default function ChangeManageModal({
  showModal,
  closeModal,
  changeManagement,
}: {
  showModal: boolean;
  closeModal: () => void;
  changeManagement: (id: number) => void;
}) {
  const localAxios = useLocalAxios();
  const [currentPage, setCurrentPage] = useState(1);
  const [maxPage, setMaxPage] = useState<number>(1);
  const [size, setSize] = useState(10);
  const [members, setMembers] = useState<MemberAdminFull[]>([]);
  const [depts, setDepts] = useState<DepartmentAdminSummary[]>();
  const [filter, setFilter] = useState<MemberAdminSearchCondition>({
    keyword: "",
    departmentName: "",
    withDelete: false,
  });

  const handleKeywordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFilter({
      ...filter,
      keyword: e.target.value,
    });
  };

  const getDept = async () => {
    const deptResponse =
      await localAxios.get<DepartmentAdminSummary[]>(`/admin/department`);
    setDepts(deptResponse.data);
  };

  const handleChangeDept = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFilter({
      ...filter,
      departmentName: e.target.value

    })
  }

  const getMembers = async (page: number) => {
    const response = await localAxios.post<MemberSearchResult>(
      `/admin/member/search?page=${page - 1}&&size=4`,
      filter,
    );
    setMembers(response.data.memberAdminFullDtos);
    setMaxPage(response.data.count);
  };
  useEffect(() => {
    getDept();
    getMembers(currentPage);
  }, [currentPage]);

  return (
    <Modal show={showModal} onClose={closeModal}>
      <Modal.Header>관리자 목록</Modal.Header>
      <Modal.Body>
        <div className="relative w-full overflow-x-auto">
          <div className="p-3">
            <div className="relative flex flex-col overflow-x-auto">
              <div className="flex flex-row justify-between my-1">
                <select
                  className="mr-2 rounded border px-4 py-2 text-sm"
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
                    <option
                      key={dept.departmentId}
                      value={dept.description}
                    >
                      {dept.description}
                    </option>
                  ))}
                </select>
                <div className="flex flex-row w-fit mx-auto">
                  <input
                    type="search"
                    id="member-search"
                    className="w-96 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="직원 이름을 입력하세요"
                    value={filter.keyword}
                    onChange={handleKeywordChange}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                        getMembers(1);
                      }
                    }}
                  />
                  <button
                    type="button"
                    className="-ml-10 rounded-r-lg border border-blue-700 bg-blue-700 p-2.5 text-sm font-medium text-white hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300"
                    onClick={() => getMembers(1)}
                  >
                    <HiSearch className="h-5 w-5" />
                  </button>
                </div>
              </div>
              <Table
                theme={{
                  root: {
                    wrapper: "static",
                  },
                }}
                className="mt-4 w-full text-center text-sm text-gray-500 dark:text-gray-400"
              >
                <Table.Head className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    이름
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    사업부
                  </Table.HeadCell>
                  {/* <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    부서
                  </Table.HeadCell> */}
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    담당업무
                  </Table.HeadCell>
                  <Table.HeadCell
                    scope="col"
                    className="whitespace-nowrap px-4 py-3"
                  >
                    회원등급
                  </Table.HeadCell>
                </Table.Head>
                <Table.Body>
                  {members && members.length > 0 && members?.map((m) => (
                    <MemberListRow
                      key={m.id}
                      member={m}
                      onClick={() => changeManagement(m.id)}
                    />
                  ))}
                </Table.Body>
              </Table>
              {
                members.length === 0 &&
                <p className="w-full text-center">담당자가 없습니다.</p>
              }
              <div className="mt-2 flex w-full justify-center">
                <Pagination
                  currentPage={currentPage}
                  totalPages={maxPage}
                  layout="pagination"
                  onPageChange={(page) => setCurrentPage(page)}
                  className="mx-auto w-fit"
                  previousLabel="이전"
                  nextLabel="다음"
                />
              </div>
            </div>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
}
