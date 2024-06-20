"use client";
import { Badge, Pagination, theme } from "flowbite-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { twMerge } from "tailwind-merge";
import NetworkingListRow from "@/components/networking/NetworkingListRow";
import { HiCheck } from "react-icons/hi";
import {
  NetworkingFilterFull,
  NetworkingSummary,
  NetworkingUpdate,
  NetworkingSearchCondition,
  NetworkingSearchResult,
} from "@/types/networking/Networking";
import { useLocalAxios } from "@/app/api/axios";
import { NetworkingCreateModal } from "@/components/networking/NetworkingCreateModal";
import { AxiosError } from "axios";
import Spinner from "@/components/common/Spinner";

export default function NetworkingTable() {
  const [currentPage, setCurrentPage] = useState(1);
  const [networkings, setNetworkings] = useState<NetworkingSummary[]>();
  const localAxios = useLocalAxios();
  const [isAvaliableSubmit, setIsAvailableSubmit] = useState<boolean>(false);
  const [maxCount, setMaxCount] = useState(1);
  const [selected, setSelected] = useState<number[]>([]);
  const [isMounted, setIsMounted] = useState<boolean>(false);
  const [searchCondition, setSearchCondition] =
    useState<NetworkingSearchCondition>({
      targetYear: 0,
      departmentId: 0,
      category: "",
    });
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];
  const [isNetworkingModalOpen, setIsNetworkingModalOpen] =
    useState<boolean>(false);
  const openCreateModal = () => {
    setIsNetworkingModalOpen(true);
  };
  const closeCreateModal = () => {
    setIsNetworkingModalOpen(false);
  };

  const handleOnCheckedNetworking = (checked: boolean, networkId: number) => {
    const nextSelected = checked
      ? [...selected, networkId]
      : selected.filter((id) => id != networkId);
    setSelected(nextSelected);
  };
  // const handle
  const [networkingFilter, setNetworkingFilter] =
    useState<NetworkingFilterFull>();
  useEffect(() => {
    const baseSetting = async () => {
      const response = await localAxios.get(`/networking/filters`);

      setNetworkingFilter(response.data);
      getNetworkings(currentPage, searchCondition);
    };
    baseSetting();
  }, []);

  const onChangeFilterValue = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    const nextSearchCondition = {
      ...searchCondition,
      [name]: value,
    };
    setSearchCondition(nextSearchCondition);

    // const params = {
    //   page: currentPage - 1,
    //   departmentId: nextSearchCondition.departmentId,
    //   targetYear: nextSearchCondition.targetYear,
    //   category: nextSearchCondition.category,
    // };
    getNetworkings(currentPage, nextSearchCondition);
  };

  const onChangePageValue = (page: number) => {
    setCurrentPage(page);

    getNetworkings(page, searchCondition);
  };

  const getNetworkings = async (
    page: number,
    searchCondition: NetworkingSearchCondition,
  ) => {
    setIsMounted(false);
    const response = await localAxios.post<NetworkingSearchResult>(
      `/networking/search?page=${page - 1}`,
      searchCondition,
    );

    const nextNetworkings = response.data.networkingSummaryDtos;
    setNetworkings(nextNetworkings);
    setMaxCount(response.data.count);

    setIsMounted(true);
  };

  const getTag = () => {
    return networkings && networkings.length > 0 ? (
      <table className="w-fit border-collapse text-left dark:text-gray-400 rtl:text-right">
        <thead className="bg-gray-100 uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400 whitespace-nowrap text-center">
          <tr>
            <th
              scope="col"
              className="px-4 py-3 font-semibold"
              style={{ width: "100px" }}
            >
              <HiCheck />
            </th>
            <th
              scope="col"
              className="px-4 py-3 font-semibold"
              style={{ width: "400px" }}
            >
              분야
            </th>
            <th
              scope="col"
              className="px-4 py-3 font-semibold"
              style={{ width: "150px" }}
            >
              담당자
            </th>
            <th
              scope="col"
              className="px-4 py-3 font-semibold"
              style={{ width: "150px" }}
            >
              네트워킹 담당자
            </th>
            <th
              scope="col"
              className="h-full px-4 py-3 text-center font-semibold"
              style={{ width: "600px" }}
            >
              후보자 현황
            </th>
            <th
              scope="col"
              className="px-4 py-3 text-center font-semibold"
              style={{ width: "200px" }}
            >
              후보자 추가
            </th>
            <th
              scope="col"
              className="px-4 py-3 font-semibold"
              style={{ width: "200px" }}
            >
              관리 현황
            </th>
          </tr>
        </thead>
        <tbody>
          {networkings?.map((n) => (
            <NetworkingListRow
              key={n.networkingId}
              networking={n}
              onClose={baseSetting}
              searchCondition={searchCondition}
              onChangeChecked={handleOnCheckedNetworking}
            />
          ))}
          {/* <NetworkingListRow /> */}
        </tbody>
      </table>
    ) : (
      <div>데이터가 없습니다.</div>
    );
  };
  const deleteNetworkings = async () => {
    const baseSetting = async () => {
      const response = await localAxios.get(`/networking/filters`);
      setNetworkingFilter(response.data);
      getNetworkings(1, searchCondition);
    };
    try {
      if (confirm("정말 삭제하시겠습니까?")) {
        const response = await localAxios.put(`/networking/all`, selected);
        if (response.status == 200) {
          alert("삭제에 성공하였습니다.");
          baseSetting();
          setSelected([]);
        }
      }
    } catch (e) {
      const err = e as AxiosError;
      if (err.response?.status == 403) {
        alert("권한이 없습니다.");
        baseSetting();
      } else {
        alert("새로고침 후 다시 시도해주세요.");
      }
    }
  };

  const handleOnSelectedIdsSubmit = async () => {
    try {
      const isAvaliableSubmit = await localAxios.post<boolean>(
        `/networking/submittable`,
        {
          networkingIds: selected,
        },
      );

      if (isAvaliableSubmit.data) {
        if (confirm("네트워킹 승인 신청을 하시겠습니까?")) {
          try {
            const submitResponse = await localAxios.post(`/networking/submit`, {
              networkingIds: selected,
            });
            if (submitResponse.status == 200) {
              alert("등록에 성공하였습니다.");
              getNetworkings(currentPage, searchCondition);
            }
          } catch (err) {
            const e = err as AxiosError;
            console.error(e);
            alert("송신간 에러가 발생했습니다. 잠시후 다시 시도해주세요.");
          }
        }
      }
    } catch (err) {
      const e = err as AxiosError;
      console.error(e)
      alert("승인 신청에 부적합한 네트워킹이 존재합니다..");
    }
  };
  const baseSetting = async () => {
    setNetworkings([]);
    const response = await localAxios.get(`/networking/filters`);
    setNetworkingFilter(response.data);
    setCurrentPage(1);
    getNetworkings(1, searchCondition);
  };

  const handelExcelDownload = async () => {
    if (selected.length > 0) {
      const response = await localAxios.post(
        `/networking/excel-download`,
        {
          networkingIds: selected,
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
  return (
    <div className="flex min-h-screen flex-col space-y-5 bg-white p-10 m-5 shadow-md">
      <h3 className="text-3xl font-bold mb-3">네트워킹 관리 현황</h3>
      <div className="flex space-x-5">
        <div className="relative w-full overflow-x-auto">
          <div className="p-3">
            <div className="relative flex flex-col overflow-x-auto">
              <div className="flex">
                <span className="ml-2 flex">
                  <Badge className="mr-2" color={coloredBadge[0]}>
                    분야
                  </Badge>
                  {networkingFilter?.categoryCount}개
                </span>
                <span className="ml-4 flex">
                  <Badge className="mr-2" color={coloredBadge[1]}>
                    담당자
                  </Badge>
                  {networkingFilter?.executiveCount}명
                </span>
                <span className="ml-4 flex">
                  <Badge className="mr-2" color={coloredBadge[2]}>
                    후보자
                  </Badge>
                  {networkingFilter?.profileCount}명
                </span>
              </div>
              <div className="mb-4 flex justify-between">
                <div>
                  <div className="mt-2 flex items-center">
                    <span className="ml-2 mr-2">
                      <Badge className="mr-2 py-2" color={coloredBadge[0]}>
                        정렬
                      </Badge>
                    </span>
                    <select
                      className="mr-2 rounded border px-4 py-2 text-sm"
                      onChange={onChangeFilterValue}
                      name="category"
                    >
                      <option value={""}>분야</option>
                      {networkingFilter?.categories.map((s, index) => (
                        <option key={index} value={s}>
                          {s}
                        </option>
                      ))}
                    </select>
                    <select
                      className="mr-2 rounded border px-4 py-2 text-sm"
                      name="targetYear"
                      onChange={onChangeFilterValue}
                    >
                      <option value="">발생연도</option>
                      <option value={2024}>2024</option>
                    </select>
                    <select
                      className="rounded border px-4 py-2 text-sm"
                      name="departmentId"
                      onChange={onChangeFilterValue}
                    >
                      <option value="">사업부 명</option>
                      {networkingFilter?.departmentSummaryDtos.map((d) => (
                        <option key={d.departmentId} value={d.departmentId}>
                          {d.name}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
                <div className="mt-2">
                  <button
                    className="mr-2 rounded bg-blue-600 px-4 py-2 text-sm font-bold text-white hover:bg-blue-700"
                    onClick={handelExcelDownload}
                  >
                    리포트 출력
                  </button>
                  <button
                    className="mr-2 rounded bg-blue-600 px-4 py-2 text-sm font-bold text-white hover:bg-blue-700"
                    onClick={openCreateModal}
                  >
                    신규
                  </button>
                  <button
                    className="mr-2 rounded bg-blue-600 px-4 py-2 text-sm font-bold text-white hover:bg-blue-700"
                    onClick={handleOnSelectedIdsSubmit}
                    disabled={isAvaliableSubmit}
                  >
                    제출
                  </button>
                  {/* <button className="mr-2 rounded bg-blue-500 px-4 py-2 text-sm font-bold text-white hover:bg-blue-700">
                    수정
                  </button> */}
                  <button
                    className="rounded bg-red-600 px-4 py-2 text-sm font-bold text-white hover:bg-blue-700"
                    onClick={deleteNetworkings}
                  >
                    삭제
                  </button>
                </div>
              </div>
              {isMounted ? (
                getTag()
              ) : (
                <div>
                  <Spinner size={60} color="#ff6347" />
                </div>
              )}

              <div className="pagenation-part mt-2 flex w-full justify-center">
                <Pagination
                  currentPage={currentPage}
                  nextLabel=""
                  onPageChange={onChangePageValue}
                  previousLabel=""
                  showIcons
                  totalPages={maxCount}
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
            </div>
          </div>
          <NetworkingCreateModal
            showModal={isNetworkingModalOpen}
            closeModal={closeCreateModal}
            size={"3xl"}
            onClose={baseSetting}
            searchCondition={searchCondition}
          />
        </div>
      </div>
    </div>
  );
}
