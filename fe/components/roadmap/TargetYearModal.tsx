"use client";
import { Popover, Modal, Table, Badge } from "flowbite-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useLocalAxios } from "@/app/api/axios";
import { techmapMoveData } from "@/types/techmap/common";
import { techmapRequest } from "@/types/techmap/techmap";
interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  data?: techmapMoveData;
}
import { useAuthStore } from "@/stores/auth";
const level = ["관리자", "운영진"];

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

export default function TargetModal(props: ModalProps) {
  // 기존 리스트, 새로운 리스트
  const userStore = useAuthStore();

  const [techmapList, settechmapList] = useState<techmapRequest>();
  const userRouter = useRouter();

  const localAxios = useLocalAxios();

  // 초기 접속 시
  const fetch = async () => {
    try {
      const response = await localAxios.post("/techmap/list", {
        pageNumber: 0,
        size: 10,
        targetYear: props.data?.year,
      });
      if (response.status === 200) {
        settechmapList(response.data);
      } else {
        alert("로그인 실패");
      }
    } catch (error) {
      console.error("로그인 에러", error);
      alert("로그인 중 에러 발생");
    }
  };

  useEffect(() => {
    if (!props.isModalOpen) {
    } else {
      fetch();
    }
  }, [props.isModalOpen]);

  const handelMovetechmap = async (techmapId: number) => {
    try {
      const response = await localAxios.post(`techmap-project/move`, {
        techmapId: techmapId,
        moveStatus: props.data?.type,
        techmapProjectId: props.data?.projectId,
      });
      if (response.status === 200) {
        alert("성공");
        props.onClose();
      } else if (response.status === 400) {
        alert("중복 데이터가 있어 실패했습니다.");
      } else {
        alert("실패");
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      {props.isModalOpen && (
        <Modal show={props.isModalOpen} onClose={props.onClose}>
          <Modal.Header className="text-3xl font-bold">
            {props.data?.type === "MOVE"
              ? "tech 이동"
              : "tech 복사"}
          </Modal.Header>
          {/* 검색창 */}
          <div className="relative flex w-full flex-col justify-center">
            <div className="overflow-x-auto">
              <Table
                theme={{
                  root: {
                    wrapper: "static",
                  },
                }}
                className="w-full text-center text-sm text-gray-500 dark:text-gray-400"
              >
                <Table.Head className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                  {/* column받아오기 */}

                  <Table.HeadCell scope="col" className="p-2">
                    적용연도
                  </Table.HeadCell>
                  <Table.HeadCell scope="col" className="p-2">
                    사업부
                  </Table.HeadCell>
                  <Table.HeadCell scope="col" className="p-2">
                    설명
                  </Table.HeadCell>
                  {/* <Table.HeadCell scope="col" className="p-2">
                    키워드
                  </Table.HeadCell> */}
                  <Table.HeadCell scope="col" className="p-2">
                    등록기한
                  </Table.HeadCell>
                  <Table.HeadCell scope="col" className="p-2">
                    생성일
                  </Table.HeadCell>
                </Table.Head>
                <Table.Body>
                  {techmapList?.techmaps.map((data, index) => (
                    <Table.Row
                      key={index}
                      className="border-b hover:cursor-pointer hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
                      onClick={() => handelMovetechmap(data.techmapId)}
                    >
                      {/* 적용연도 */}
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        {data.targetYear}
                      </Table.Cell>
                      {/* 사업부 */}
                      <Table.Cell className="whitespace-normal  p-2 font-medium text-gray-900 dark:text-white">
                        <div className="flex justify-center space-x-1">
                          {level.includes(userStore.authority ?? "") ? (
                            <>
                              {data.departments
                                .slice(0, 2)
                                .map((department, index) => (
                                  <Badge key={index}>{department}</Badge>
                                ))}
                              {data.departments.length > 2 && (
                                <Popover
                                  trigger="hover"
                                  placement="top"
                                  arrow={false}
                                  content={
                                    <div className="flex flex-row gap-2 bg-none">
                                      {data.departments
                                        .slice(2)
                                        .map((department, index) => (
                                          <Badge key={index}>
                                            {department}
                                          </Badge>
                                        ))}
                                    </div>
                                  }
                                >
                                  <div className="hover:cursor-pointer">
                                    <Badge>•••</Badge>
                                  </div>
                                </Popover>
                              )}
                            </>
                          ) : (
                            <Badge>{userStore.departmentName}</Badge>
                          )}
                        </div>
                      </Table.Cell>
                      {/* 설명 */}
                      <Table.Cell
                        // onClick={() => handleRouterPage(data.techmapId)}
                        className="whitespace-pre-line p-2 font-medium text-gray-900 dark:text-white"
                      >
                        {data.description}
                      </Table.Cell>

                      {/* 키워드 */}
                      {/* <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <div className="flex justify-center space-x-1">
                          {data.keywords.slice(0, 2).map((keyword, index) => (
                            <Badge key={index}>{keyword}</Badge>
                          ))}
                          {data.keywords.length > 2 && (
                            <Popover
                              trigger="hover"
                              placement="top"
                              arrow={false}
                              content={
                                <div className="flex flex-row gap-2 bg-none">
                                  {data.keywords
                                    .slice(2)
                                    .map((keyword, index) => (
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
                      {/* 등록기한 */}
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        {makeDate(data.dueDate)}
                      </Table.Cell>
                      {/* 생성일 */}
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        {makeDate(data.createDate)}
                      </Table.Cell>
                    </Table.Row>
                  ))}
                </Table.Body>
              </Table>
            </div>
          </div>
          <Modal.Body className="max-h-[500px] min-h-[300px] overflow-y-auto p-0"></Modal.Body>
          {/* <Modal.Footer className="justify-end">
            <Button type="button" onClick={handleUpdate}>
              등록
            </Button>
            <Button onClick={props.onClose}>취소</Button>
          </Modal.Footer> */}
        </Modal>
      )}
    </>
  );
}
