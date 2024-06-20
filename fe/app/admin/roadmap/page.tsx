"use client";
import {
  Badge,
  Button,
  Checkbox,
  Dropdown,
  Label,
  Pagination,
  Table,
  TextInput,
  theme,
  Select,
} from "flowbite-react";

// import tableData from "@/data/techmap/tableData.json";
import { useState } from "react";
import { HiSearch } from "react-icons/hi";
import { twMerge } from "tailwind-merge";
import Link from "next/link";

// const tableColumn = [
//   "대분류",
//   "기술분야",
//   "세부기술",
//   "기술개요 및 필요 역량",
//   "기술 구분",
//   "당사 수준",
//   "판단 근거",
//   "타겟 분야",
//   "확보 목표",
//   "인재 Pool",
//   "techLab",
//   "techCompany",
//   "담당자",
// ];
const departmentList = [
  "VD",
  "생활가전",
  "의료기기",
  "MX",
  "네트워크",
  "한국총괄",
  "SR",
  "DPC",
  "글로벌마케팅실",
  "디자인",
  "생기연",
  "G.CS",
  "전장사업팀",
  "EHS센터",
  "지속가능경영추진센터",
  "인재개발원",
  "법무실",
  "경영지원실(BDC)",
  "경영지원실(경영혁신)",
  "경영지원실",
];
const levelList = ["열세", "동등", "우세"];
const yearList = [2024, 2023, 2022, 2021];
const viewList = [10, 15, 20];
// 기술수준 우세
export default function AdmintechmapPage() {
  const [currentPage, setCurrentPage] = useState(1);
  return (
    <div className="flex flex-col space-y-5 p-5 ">
      <h3 className="text-xl">tech 관리 현황</h3>
      <div className="flex space-x-5  ">
        <div className="relative overflow-x-auto">
          <section className="py-3">
            <div className=" mx-auto max-w-screen-2xl">
              <div className="relative  overflow-hidden bg-white p-10 shadow-md sm:rounded-lg">
                <div className="mx-4  dark:border-gray-700">
                  <div className="flex flex-col-reverse items-center justify-between py-3 md:flex-row md:space-x-3">
                    <div className="flex">
                      <div className="flex items-center justify-center space-x-3 text-xs">
                        <p>총 80개</p>
                        <Select
                          id="settings-language"
                          name="settings-language"
                          sizing="sm"
                        >
                          {levelList.map((level) => (
                            <option key={level}>{level}</option>
                          ))}
                        </Select>
                        <Select
                          id="settings-language"
                          name="settings-language"
                          sizing="sm"
                        >
                          {viewList.map((view) => (
                            <option key={view}>{view}개씩 보기</option>
                          ))}
                        </Select>
                      </div>
                    </div>

                    <div className="flex space-x-3 text-xs">
                      <div className="flex flex-row items-center justify-center space-x-2">
                        <Label htmlFor="last-name">발행연도</Label>
                        <Select
                          id="settings-language"
                          name="settings-language"
                          sizing="sm"
                        >
                          {yearList.map((year) => (
                            <option key={year}>{year}</option>
                          ))}
                        </Select>
                      </div>
                      <div className="flex flex-row items-center justify-center space-x-2">
                        <Label htmlFor="last-name">사업부명</Label>
                        <Select
                          id="settings-language"
                          name="settings-language"
                          sizing="sm"
                        >
                          {departmentList.map((department) => (
                            <option key={department}>{department}</option>
                          ))}
                        </Select>
                      </div>
                    </div>
                    <div className="flex space-x-2">
                      <Button size="sm">엑셀 다운</Button>
                      <Button size="sm">엑셀 일괄 업로드</Button>
                    </div>
                    <div className="flex space-x-2">
                      <Link href={"/techmap/register"}>
                        <Button size="sm">신규</Button>
                      </Link>
                      <Button size="sm">수정</Button>
                      <Button size="sm">삭제</Button>
                    </div>
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <Table
                    // theme={{
                    //   root: {
                    //     wrapper: "static",
                    //   },
                    // }}
                    className="table-fixed text-center text-sm text-gray-500 dark:text-gray-400"
                  >
                    <Table.Head className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                      <Table.HeadCell scope="col" className="w-[40px] p-2">
                        <div className="flex items-center justify-center">
                          <Checkbox id="checkbox-all" name="checkbox-all" />
                          <Label htmlFor="checkbox-all" className="sr-only">
                            Check all
                          </Label>
                        </div>
                      </Table.HeadCell>
                      {/* column받아오기 */}

                      <Table.HeadCell scope="col" className="w-[60px] p-2">
                        대분류
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[80px] p-2">
                        기술 분야
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[100px] p-2">
                        세부기술
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[380px] p-2">
                        기술개요 및 필요 역량
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[50px] p-2">
                        기술 구분
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[50px] p-2">
                        당사 수준
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[200px] p-2">
                        판단 근거
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[50px] p-2">
                        타겟 분야
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[50px] p-2">
                        확보인재
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[50px] p-2">
                        인재 Pool
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[60px] p-2">
                        techLab
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[60px] p-2">
                        techCompany
                      </Table.HeadCell>
                      <Table.HeadCell scope="col" className="w-[70px] p-2">
                        담당자
                      </Table.HeadCell>
                    </Table.Head>
                    <Table.Body>
                      {/* {tableData.map((data, index) => ( */}
                      <Table.Row
                        // key={index}
                        className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
                      >
                        <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
                          <div className="flex items-center justify-center">
                            <Checkbox
                              id="checkbox-table-search-1"
                              name="checkbox-table-search-1"
                            />
                          </div>
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          S/W
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          Vision
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          On Device AI
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 text-left text-xs font-medium text-gray-900 dark:text-white">
                          <ol>
                            <li>
                              □ On Device AI 학습 파이프라인 자동화를 위한
                              플랫폼 기술
                            </li>
                            <li>
                              개발되는 모델의 데이터 , 코드 , 학습 Deploy
                              pipeline 자동화를 통한 생산성 향상
                            </li>
                            <li>
                              데이터 , 모델에 대한 visibility 확보 및 편의성
                              개선
                            </li>
                            <li>
                              대용량 모델의 경량화 양자화를 통한 AI 기능 On
                              device 화
                            </li>
                          </ol>

                          {/* {data.need} */}
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          신규
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          우세
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 text-left text-xs font-medium text-gray-900 dark:text-white">
                          <ol>
                            <li>
                              당사 ○○○ 사업 경쟁력 확보를 위한 지속적인 AI 인력
                              채용 필요
                            </li>
                          </ol>
                          {/* {data.relativeLevelReason} */}
                        </Table.Cell>
                        <Table.Cell className="whitespace-nowrap p-2 font-medium text-gray-900 dark:text-white">
                          &quot아니요&quot
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          7
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          15
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          20
                        </Table.Cell>
                        <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                          3
                        </Table.Cell>
                        <Table.Cell className="whitespace-pre-wrap p-2 font-medium text-gray-900 dark:text-white">
                          김삼성 프로
                        </Table.Cell>
                      </Table.Row>
                      {/* ))} */}
                    </Table.Body>
                  </Table>
                </div>
                <nav
                  className="flex flex-col items-start justify-center space-y-3 pb-2 pt-4 md:flex-row md:items-center md:space-y-0"
                  aria-label="Table navigation"
                >
                  <Pagination
                    currentPage={currentPage}
                    nextLabel=""
                    onPageChange={(page) => setCurrentPage(page)}
                    previousLabel=""
                    showIcons
                    totalPages={20}
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
                </nav>
                <div className="flex flex-row items-center justify-end space-x-2">
                  <Label htmlFor="last-name">선택항목을</Label>
                  <Select
                    id="settings-language"
                    name="settings-language"
                    sizing="sm"
                  >
                    {yearList.map((year) => (
                      <option key={year}>{year}</option>
                    ))}
                  </Select>
                  <span>으로</span>
                  <Button size="sm">복사</Button>
                  <Button size="sm">이동</Button>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}
