"use client";

import { TextInput, Button, Modal, Table, List, Label } from "flowbite-react";
import { useEffect, useState } from "react";
import { HiSearch } from "react-icons/hi";
import { useRouter, usePathname } from "next/navigation";
import { useLocalAxios } from "@/app/api/axios";
import { AutoCompleteInput } from "../common/AutoCompleteInput";
import axios from "axios";

interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  id: number;
  techDetail: string;
  type: string;
}

interface TargetInterface {
  id: number;
  data: string;
  major?: string;
  professor?: string;
  schoolName?: string;
}

interface CompanyInterface {
  name: string;
}

interface LabInterface {
  schoolId: number;
  major: string;
  professor: string;
  labName: string;
}

const tableColumn = {
  COMPANY: ["회사명"],
  LAB: ["학교명", "전공명", "교수명", "연구실명"],
};

export default function TargetModal(props: ModalProps) {
  const [list, setList] = useState<TargetInterface[]>([]);
  const [searchList, setSearchList] = useState<TargetInterface[]>([]);
  const [isVisible, setIsVisible] = useState(false);
  const [url, setUrl] = useState("");
  const userRouter = useRouter();
  const localAxios = useLocalAxios();
  const [isRegist, setIsRegist] = useState(false);
  const [formData, setFormData] = useState({});
  const handleRouterPage = (name: string, tech: string) => {
    props.onClose();
    // 검색 필터 추가해서 보낼 예정
    userRouter.push("/talent");
  };

  // 초기 접속 시
  const fetch = async (url: string) => {
    try {
      const response = await localAxios.get(`/target-${url}/${props.id}`);
      if (response.status === 200) {
        setList(response.data);
      } else {
        alert("받기 실패");
      }
    } catch (error) {
      alert("에러발생!");
    }
  };

  const updateURL = (type: string) => {
    let url = "";
    if (type === "COMPANY") {
      setUrl("company");
      url = "company";
    } else {
      setUrl("lab");
      url = "lab";
    }
    return url;
  };

  // 최초 접속 시
  useEffect(() => {
    setSearchList([]);
    if (props.id != 0) {
      const tempUrl = updateURL(props.type);
      fetch(tempUrl);
    }
  }, []);

  useEffect(() => {
    if (!props.isModalOpen) {
      setSearchList([]);
      setFormData([]);
    } else {
      const tempUrl = updateURL(props.type);
      fetch(tempUrl);
    }
  }, [props.isModalOpen]);

  const handleSearch = async (event: any) => {
    const { value } = event.target;

    if (props.type === "LAB") {
      try {
        const response = await localAxios.get("target-lab/search", {
          params: { word: value },
        });
        setSearchList(response.data);
      } catch (error) {
        console.error(error);
      }
    } else {
      try {
        const response = await localAxios.get("/keyword", {
          params: { type: props.type, query: value },
        });
        setSearchList(response.data);
      } catch (error) {
        console.error(error);
      }
    }
  };

  const toggleDropMenu = (event: any) => {
    event.stopPropagation(); // 이벤트 캡쳐링 방지
    setIsVisible(true);
  };

  // 선택할 시 배열에 넣는다.
  const handleSelected = (event: any) => {
    // id : name, value: id
    const { id, value } = event.target;
    // 기존 List에 있는지 확인
    function checkElement(element: TargetInterface) {
      return element.id === value;
    }
    // 안에 같은 내용이 있다면
    if (!list.some(checkElement)) {
      const data = JSON.parse(id);
      if (props.type === "COMPANY") {
        setList([...list, { id: value, data: data.data }]);
      } else {
        setList([
          ...list,
          {
            id: value,
            data: data.data,
            major: data.major,
            professor: data.professor,
            schoolName: data.schoolName,
          },
        ]);
      }
    }
  };

  // 내용 추가
  const handleUpdate = async () => {
    try {
      const response = await localAxios.post(
        `/target-${url}/${props.id}`,
        list.map((data) => data.id),
      );
      props.onClose();
    } catch (error) {
      console.error(error);
    }
  };

  const handleAutoCompleteInputChange = (item: any) => {
    setFormData((prevState) => ({
      ...prevState,
      ["schoolId"]: item.id,
    }));
  };

  const handleRegist = async () => {
    // 추가로직

    if (props.type === "COMPANY") {
      try {
        const response = await localAxios.post(`/company`, formData);
        if (response.status === 200) {
          setIsRegist(false);
          setList([
            ...list,
            { id: response.data.companyId, data: response.data.name },
          ]);
          alert("등록 완료");
        } else if (response.status === 400) {
          alert("이미 등록되어 있습니다.");
        }
      } catch (error) {
        if (axios.isAxiosError(error)) {
          error.response?.status === 400 && alert("이미 존재합니다.");
        }
      }
    } else {
      try {
        const response = await localAxios.post(`/lab`, formData);
        if (response.status === 200) {
          setIsRegist(false);
          setList([
            ...list,
            {
              id: response.data.labId,
              schoolName: response.data.schoolName,
              data: response.data.labName,
              major: response.data.major,
              professor: response.data.labProfessor,
            },
          ]);
          alert("등록 완료");
        } else if (response.status === 400) {
          alert("이미 등록되어 있습니다.");
        } else {
          alert("실패");
        }
      } catch (error) {
        if (axios.isAxiosError(error)) {
          error.response?.status === 400 && alert("이미 존재합니다.");
        }
      }
    }
  };

  const handleChange = (event: any) => {
    const { value, name } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // 삭제를 할 때 기존에 있는 데이터를 확인한 후 마지막 등록을 눌렀을 때 업데이트가 된다.
  const handleDelete = (index: number) => {
    setList((prev) => prev.filter((_, idx) => idx !== index));
  };

  return (
    <>
      {props.isModalOpen && (
        <Modal
          show={props.isModalOpen}
          onClose={props.onClose}
          onClick={() => setIsVisible(false)}
        // size={"md"}
        >
          <Modal.Header className="text-3xl font-bold">
            {props.type === "COMPANY" ? "techCompany 목록" : "techLab 목록"}
          </Modal.Header>
          {/* 검색창 */}
          <div className="relative flex w-full flex-col justify-center">
            <div className="flex justify-center">
              <TextInput
                className="w-2/3 justify-center pt-5"
                icon={HiSearch}
                id="search"
                name="search"
                placeholder="검색"
                onChange={handleSearch}
                inputMode="search"
                required
                type="search"
                autoComplete="off"
                onClick={toggleDropMenu}
              />
            </div>
            <div className="relative flex justify-center">
              <List
                unstyled
                className="absolute z-10 w-2/3 divide-y divide-gray-200 bg-gray-50"
                hidden={!isVisible}
                onClick={toggleDropMenu}
              >
                {searchList.length > 0 ? (
                  searchList?.map((company, index) =>
                    !list.some((element) => element.id === company.id) ? (
                      <List.Item
                        key={index}
                        className="p-2 text-sm hover:bg-gray-300"
                        id={JSON.stringify(company)}
                        value={company.id}
                        onClick={handleSelected}
                      >
                        {company.data}
                      </List.Item>
                    ) : (
                      <List.Item
                        key={index}
                        className="bg-gray-300 p-2 text-sm hover:bg-gray-300"
                        id={JSON.stringify(company)}
                        value={company.id}
                        onClick={handleSelected}
                      >
                        {company.data}
                      </List.Item>
                    ),
                  )
                ) : isRegist ? (
                  <>
                    <Modal
                      show={isRegist}
                      onClose={() => setIsRegist(false)}
                      size={props.type === "COMPANY" ? "sm" : "xl"}
                    >
                      <Modal.Header>
                        {props.type === "COMPANY" ? "기업 등록" : "연구실 등록"}
                      </Modal.Header>
                      <Modal.Body>
                        {props.type === "COMPANY" ? (
                          <div className="">
                            <Label>기업명</Label>
                            <TextInput
                              name="name"
                              onChange={handleChange}
                              autoComplete="off"
                            />
                          </div>
                        ) : (
                          <div className="flex min-h-52">
                            <div>
                              <Label>학교</Label>
                              <AutoCompleteInput
                                identifier="SCHOOL"
                                onChange={(item) => {
                                  handleAutoCompleteInputChange(item);
                                }}
                              />
                            </div>
                            <div>
                              <Label>전공명</Label>
                              <TextInput
                                name="major"
                                autoComplete="off"
                                onChange={handleChange}
                              />
                            </div>
                            <div>
                              <Label>지도교수</Label>
                              <TextInput
                                name="professor"
                                autoComplete="off"
                                onChange={handleChange}
                              />
                            </div>
                            <div>
                              <Label>연구실명</Label>
                              <TextInput
                                name="labName"
                                autoComplete="off"
                                onChange={handleChange}
                              />
                            </div>
                          </div>
                        )}
                      </Modal.Body>
                      <div className="flex justify-end space-x-2 p-2">
                        <Button onClick={handleRegist}>등록</Button>
                        <Button onClick={() => setIsRegist(false)}>취소</Button>
                      </div>
                    </Modal>
                  </>
                ) : (
                  // )
                  <List.Item className="text-sm">
                    <p>찾으시려는 기업명 정보가 없습니다. </p>
                    <p>
                      신규{" "}
                      <span
                        className="font-bold text-blue-500 hover:cursor-pointer"
                        onClick={() => setIsRegist(true)}
                      >
                        등록
                      </span>{" "}
                      하시겠습니까?
                    </p>
                  </List.Item>
                )}
              </List>
            </div>
          </div>
          <Modal.Body className="max-h-[500px] min-h-[300px] overflow-y-auto pt-6">
            <Table
              theme={{
                root: {
                  wrapper: "static",
                },
              }}
              className="w-full text-center text-sm text-gray-500 dark:text-gray-400"
            >
              <Table.Head className="bg-gray-50 text-xs uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
                {props.type === "COMPANY"
                  ? tableColumn.COMPANY.map((column, index) => (
                    <Table.HeadCell scope="col" className="p-2" key={index}>
                      {column}
                    </Table.HeadCell>
                  ))
                  : tableColumn.LAB.map((column, index) => (
                    <Table.HeadCell scope="col" className="p-2" key={index}>
                      {column}
                    </Table.HeadCell>
                  ))}

                <Table.HeadCell className="w-[50px] p-2">
                  <span className="sr-only">Edit</span>
                </Table.HeadCell>
              </Table.Head>
              <Table.Body>
                {props.type === "COMPANY"
                  ? list.map((data, index) => (
                    <Table.Row
                      key={index}
                      className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
                    >
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                          {data.data}
                        </p>
                      </Table.Cell>
                      <Table.Cell className="whitespace-normal p-2  dark:text-white">
                        <p
                          className="font-medium text-red-500 hover:cursor-pointer hover:text-red-700"
                          onClick={() => handleDelete(index)}
                        >
                          삭제
                        </p>
                      </Table.Cell>
                    </Table.Row>
                  ))
                  : list.map((data, index) => (
                    <Table.Row
                      key={index}
                      className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
                    >
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                          {data.schoolName}
                        </p>
                      </Table.Cell>
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                          {data.major}
                        </p>
                      </Table.Cell>
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                          {data.professor}
                        </p>
                      </Table.Cell>
                      <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                        <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                          {data.data}
                        </p>
                      </Table.Cell>
                      <Table.Cell className="whitespace-normal p-2  dark:text-white">
                        <p
                          className="font-medium text-red-500 hover:cursor-pointer hover:text-red-700"
                          onClick={() => handleDelete(index)}
                        >
                          삭제
                        </p>
                      </Table.Cell>
                    </Table.Row>
                  ))}
              </Table.Body>
            </Table>
          </Modal.Body>
          <Modal.Footer className="justify-end">
            <Button type="button" onClick={handleUpdate}>
              등록
            </Button>
            <Button onClick={props.onClose}>취소</Button>
          </Modal.Footer>
        </Modal>
      )}
    </>
  );
}
