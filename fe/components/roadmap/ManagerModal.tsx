import { TextInput, Button, Modal, Table, List } from "flowbite-react";
import { HiSearch } from "react-icons/hi";
import { useLocalAxios } from "@/app/api/axios";
import { useEffect, useState } from "react";
interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  id: number;
}

interface TargetInterface {
  id: number;
  name: string;
  departmentName: string;
  teamName: string;
  authority: string;
  isSecuritySigned: boolean;
  changePassword: boolean;
  authLevel: number;
}

export default function ManagerModal(props: ModalProps) {
  const localAxios = useLocalAxios();
  const [searchList, setSearchList] = useState<TargetInterface[]>([]);
  const handleSearch = async (event: any) => {
    const { value } = event.target;
    try {
      const response = await localAxios.get(`/member/search/${props.id}`, {
        params: { word: value },
      });
      setSearchList(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (props.isModalOpen) {
      setSearchList([]);
    }
  }, [props.isModalOpen]);

  const handleChangeManager = async (managerId: number) => {
    try {
      const response = await localAxios.put(
        `/techmap-project/${props.id}/update-manager/${managerId}`,
      );
      if (response.status === 200) {
        alert("변경이 완료되었습니다.");
        props.onClose();
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      {props.isModalOpen && (
        <Modal show={props.isModalOpen} onClose={props.onClose} size={"md"}>
          <Modal.Header className="text-3xl font-bold">
            담당자 변경
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
              />
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
                <Table.HeadCell className="p-2">
                  <span className="">이름</span>
                </Table.HeadCell>
                <Table.HeadCell className="p-2">
                  <span className="">사업부</span>
                </Table.HeadCell>
                <Table.HeadCell className="p-2">
                  <span className="">부서이름</span>
                </Table.HeadCell>
                <Table.HeadCell className="p-2">
                  <span className="">권한</span>
                </Table.HeadCell>
              </Table.Head>
              <Table.Body>
                {searchList?.map((data, index) => (
                  <Table.Row
                    key={index}
                    className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
                    onClick={() => handleChangeManager(data.id)}
                  >
                    <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                      <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                        {data.name}
                      </p>
                    </Table.Cell>
                    <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                      <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                        {data.departmentName}
                      </p>
                    </Table.Cell>
                    <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                      <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                        {data.teamName}
                      </p>
                    </Table.Cell>
                    <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
                      <p className="inline-block hover:cursor-pointer hover:text-blue-700 hover:underline">
                        {data.authority}
                      </p>
                    </Table.Cell>
                  </Table.Row>
                ))}
              </Table.Body>
            </Table>
          </Modal.Body>
          <Modal.Footer className="justify-end">
            <Button onClick={props.onClose}>취소</Button>
          </Modal.Footer>
        </Modal>
      )}
    </>
  );
}
