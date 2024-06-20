"use client";

import {
  Badge,
  Button,
  Checkbox,
  Dropdown,
  Label,
  Pagination,
  Table,
  theme,
  Select,
  Popover,
} from "flowbite-react";
import { HiDotsHorizontal } from "react-icons/hi";
import { useEffect, useState } from "react";
import { twMerge } from "tailwind-merge";
import { useRouter, usePathname } from "next/navigation";
import techmapRegisterForm from "./techmapRegisterForm";
import DeleteModal from "./DeleteModal";
import { useLocalAxios } from "@/app/api/axios";
import { techmapRequest, techmapList } from "@/types/techmap/techmap";
import { CheckboxState, Department, PageState } from "@/types/techmap/common";
import { useAuthStore } from "@/stores/auth";

const viewList = [10, 15, 20];

// 기술수준 우세
export default function techmapTable() {
  const [openProjectModal, setOpenProjectModal] = useState(false);
  const [isRegister, setIsRegister] = useState(false);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const localAxios = useLocalAxios();
  const [techmapList, settechmapList] = useState<techmapRequest>();
  const [techmapData, settechmapData] = useState<techmapList>();
  const [checkListId, setCheckListId] = useState<CheckboxState>({});
  const [deleteId, setDeleteId] = useState({});
  const [departmentList, setDepartmentList] = useState<Department[]>([]);
  const [checkedAll, setCheckedAll] = useState(false);
  const [pageStateData, setPageStateData] = useState<PageState>({
    pageNumber: 0,
    size: 10,
  });

  const level = ["관리자", "운영진"];
  const userStore = useAuthStore();
  const makeDate = (date: string) => {
    const newDate = new Date(date);

    return (
      newDate.getFullYear() +
      "-" +
      (newDate.getMonth() + 1) +
      "-" +
      newDate.getDate()
    );
  };
  const fetch = async () => {
    try {
      const response = await localAxios.post("/techmap/list", pageStateData);
      if (response.status === 200) {
        settechmapList(response.data);
        setCheckedAll(false);
        const newState: CheckboxState = {};
        response.data.techmaps.map((data: techmapList) => {
          newState[data.techmapId] = false; // 모든 키의 값을 true 또는 false로 설정
        });
        setCheckListId(newState);
      } else {
        alert("로그인 실패");
      }
    } catch (error) {
      console.error("로그인 에러", error);
      alert("로그인 중 에러 발생");
    }
  };
  useEffect(() => {
    localAxios
      .get("/keyword", {
        params: {
          type: "DEPARTMENT",
        },
      })
      .then((response) => {
        setDepartmentList(response.data);
      })
      .catch((err) => {
        console.error(err);
      });
  }, []);

  useEffect(() => {
    fetch();
  }, [openProjectModal, openDeleteModal, pageStateData]);

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

  const handleDelete = (id: number, multi: boolean) => {
    if (multi) {
      if (hasCheckedItems()) {
        setDeleteId(checkListId);
      } else {
        alert("선택한 항목이 없습니다.");
        return;
      }
    } else {
      const newState: CheckboxState = {};
      newState[id] = true;
      setDeleteId(newState);
    }
    setOpenDeleteModal(true);
  };

  const handleEdit = async (data: techmapList) => {
    settechmapData(data);
    setIsRegister(false);
    setOpenProjectModal(true);
  };
  const handleAdd = () => {
    setIsRegister(true);
    setOpenProjectModal(true);
  };

  const handleCheckBox = (event: any) => {
    const { name, checked } = event.target;
    setCheckListId((prevState) => ({
      ...prevState,
      [name]: checked,
    }));
  };
  const handleAllChecked = (event: any) => {
    const isChecked = event.target.checked;
    setCheckedAll(isChecked);
    setCheckListId((prevState) => {
      const newState: CheckboxState = {};
      Object.keys(prevState).forEach((key) => {
        newState[key] = isChecked; // 모든 키의 값을 true 또는 false로 설정
      });
      setCheckListId(newState);
      return newState;
    });
  };

  const hasCheckedItems = () => {
    return Object.values(checkListId).some((value) => value);
  };

  const router = useRouter();
  const pathName = usePathname();

  const handleRouterPage = (id: number) => {
    router.push(pathName + "/" + id);
  };
  return (
    <div className=" bg-white p-10 shadow-md sm:rounded-lg">
      <h5 className="pb-5 text-3xl font-semibold dark:text-white">
        tech 목록
      </h5>
      <div className="mx-4 dark:border-gray-700">
        <div className="flex flex-col-reverse items-center justify-between py-3 md:flex-row md:space-x-3">
          <div className="flex space-x-3">
            <div className="flex items-center justify-center space-x-3">
              <p>총 {techmapList?.totalElements}개</p>

              <Select id="size" name="size" sizing="sm" onChange={handlePage}>
                {viewList.map((view, index) => (
                  <option key={index} value={view}>
                    {view}개씩 보기
                  </option>
                ))}
              </Select>
            </div>
            <div className="flex flex-row items-center justify-center space-x-2">
              <Label htmlFor="last-name">발행연도</Label>
              <Select
                id="targetYear"
                name="targetYear"
                onChange={handlePage}
                sizing="sm"
              >
                <option key={-1} value={"null"}>
                  전체
                </option>
                {techmapList?.targetYear?.map((year, index) => (
                  <option key={index} value={year.targetYear}>
                    {year.targetYear}
                  </option>
                ))}
              </Select>
            </div>
          </div>
          {level.includes(userStore.authority ?? "") ? (
            <div className="flex space-x-3">
              <div>
                <div className="flex flex-row items-center justify-center space-x-2">
                  <Label htmlFor="departmentId">사업부명</Label>
                  <Select
                    id="departmentId"
                    name="departmentId"
                    onChange={handlePage}
                    sizing="sm"
                  >
                    <option key={-1} value={"null"}>
                      전체
                    </option>
                    {departmentList.map((department) => (
                      <option key={department.id} value={department.id}>
                        {department.data}
                      </option>
                    ))}
                  </Select>
                </div>
              </div>
              <div className="flex space-x-2">
                <Button size="sm" onClick={handleAdd}>
                  신규
                </Button>
                <Button size="sm" onClick={() => handleDelete(0, true)}>
                  삭제
                </Button>
              </div>
            </div>
          ) : null}
        </div>
      </div>

      <div className="overflow-x-auto">
        <Table
          theme={{
            root: {
              wrapper: "static",
            },
          }}
          className="w-full text-center text-sm text-gray-500 dark:text-gray-400"
        >
          <Table.Head className="bg-gray-50 text-base font-bold uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <Table.HeadCell scope="col" className="w-[40px] p-2">
              <div className="flex items-center justify-center">
                <Checkbox
                  id="checkbox-all"
                  name="checkbox-all"
                  checked={checkedAll}
                  onChange={handleAllChecked}
                />
                <Label htmlFor="checkbox-all" className="sr-only">
                  Check all
                </Label>
              </div>
            </Table.HeadCell>
            {/* column받아오기 */}

            <Table.HeadCell scope="col" className="p-2">
              적용연도
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="p-2">
              사업부
            </Table.HeadCell>
            {/* <Table.HeadCell scope="col" className="p-2">
              키워드
            </Table.HeadCell> */}
            <Table.HeadCell scope="col" className="p-2">
              현황
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="p-2">
              등록기한
            </Table.HeadCell>
            <Table.HeadCell scope="col" className="p-2">
              생성일
            </Table.HeadCell>
            {level.includes(userStore.authority ?? "") ? (
              <Table.HeadCell scope="col" className=""></Table.HeadCell>
            ) : null}
          </Table.Head>
          <Table.Body>
            {techmapList?.techmaps.map((data, index) => (
              <Table.Row
                key={index}
                className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700 "
              >
                <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
                  <div className="flex items-center justify-center ">
                    <Checkbox
                      id="checkbox-table-search-1"
                      name={String(data.techmapId)}
                      checked={checkListId[data.techmapId] ?? false}
                      onChange={handleCheckBox}
                    />
                  </div>
                </Table.Cell>
                {/* 적용연도 */}
                <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-normal p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white"
                >
                  {data.targetYear}
                </Table.Cell>
                {/* 사업부 */}
                <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-normal  p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white"
                >
                  <div className="flex justify-center space-x-1">
                    {level.includes(userStore.authority ?? "") ? (
                      <>
                        <div className="flex flex-row gap-2 bg-none">
                          {data.departments.map((department, index) => (
                            <Badge key={index}>{department}</Badge>
                          ))}
                        </div>
                      </>
                    ) : (
                      <Badge>{userStore.departmentName}</Badge>
                    )}
                  </div>
                </Table.Cell>
                {/* 키워드 */}
                {/* <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white"
                >
                  <div className="flex justify-start space-x-1">
                    {data.keywords.slice(0, 3).map((keyword, index) => (
                      <Badge key={index}>{keyword}</Badge>
                    ))}
                    {data.keywords.length > 3 && (
                      <Popover
                        trigger="hover"
                        placement="top"
                        arrow={false}
                        content={
                          <div className="flex flex-row gap-2 bg-none">
                            {data.keywords.slice(2).map((keyword, index) => (
                              <Badge key={index}>{keyword}</Badge>
                            ))}
                          </div>
                        }
                      >
                        <div className="hover:cursor-pointer">
                          <Badge>•••</Badge>
                        </div>
                      </Popover>
                    )}
                  </div>
                </Table.Cell> */}
                {/* 설명 */}
                <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-pre-line p-2 text-center font-medium text-gray-900 hover:cursor-pointer dark:text-white"
                >
                  {data.info}
                  {/* <Popover
                    trigger="hover"
                    placement="top"
                    arrow={false}
                    content={
                      <div className="flex flex-row gap-2 bg-none">
                        {data.info}
                      </div>
                    }
                  >
                    <div className="hover:cursor-pointer">
                      <Button size={"sm"} className="">
                        더보기
                      </Button>
                    </div>
                  </Popover> */}
                </Table.Cell>

                {/* 등록기한 */}
                <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-normal p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white"
                >
                  {makeDate(data.dueDate)}
                </Table.Cell>
                {/* 생성일 */}
                <Table.Cell
                  onClick={() => handleRouterPage(data.techmapId)}
                  className="whitespace-normal p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white"
                >
                  {makeDate(data.createDate)}
                </Table.Cell>
                {level.includes(userStore.authority ?? "") ? (
                  <Table.Cell className="px-4 py-2">
                    <Dropdown
                      inline
                      label={<HiDotsHorizontal className="size-5" />}
                      theme={{
                        arrowIcon: "hidden",
                        floating: {
                          base: twMerge(theme.dropdown.floating.base, "w-40"),
                        },
                      }}
                    >
                      <Dropdown.Item onClick={() => handleEdit(data)}>
                        설정 변경
                      </Dropdown.Item>
                      <Dropdown.Item
                        className="text-red-500"
                        onClick={() => handleDelete(data.techmapId, false)}
                      >
                        삭제
                      </Dropdown.Item>
                    </Dropdown>
                  </Table.Cell>
                ) : null}
              </Table.Row>
            ))}
            <techmapRegisterForm
              isModalOpen={openProjectModal}
              onClose={() => setOpenProjectModal(false)}
              mode={isRegister}
              data={techmapData}
              departments={departmentList}
            />
            <DeleteModal
              isModalOpen={openDeleteModal}
              onClose={() => setOpenDeleteModal(false)}
              id={deleteId}
              page={"techmap"}
            />
          </Table.Body>
        </Table>
      </div>
      <nav
        className="flex flex-col items-start justify-center space-y-3 pb-2 pt-4 md:flex-row md:items-center md:space-y-0"
        aria-label="Table navigation"
      >
        <Pagination
          currentPage={pageStateData.pageNumber + 1}
          nextLabel=""
          onPageChange={handlePage}
          previousLabel=""
          showIcons
          totalPages={techmapList?.totalPages ?? 2}
          theme={{
            pages: {
              base: twMerge(theme.pagination.pages.base, "mt-0"),
              next: {
                base: twMerge(theme.pagination.pages.next.base, "w-10 p-1.5"),
                icon: "size-6",
              },
              previous: {
                base: twMerge(
                  theme.pagination.pages.previous.base,
                  "w-10 p-1.5",
                ),
                icon: "size-6",
              },
              selector: {
                base: twMerge(
                  theme.pagination.pages.selector.base,
                  "w-9 py-2 text-sm",
                ),
              },
            },
          }}
        />
      </nav>
    </div>
  );
}
