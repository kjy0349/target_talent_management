"use client";

import { Button, Modal } from "flowbite-react";
import { HiOutlineExclamationCircle } from "react-icons/hi";
import { useLocalAxios } from "@/app/api/axios";
import { useState, useEffect } from "react";

interface ModalProps {
  isModalOpen: boolean;
  id: CheckboxState;
  onClose: () => void;
  page: string;
}

interface CheckboxState {
  [key: string]: boolean;
}

export default function DeleteModal(props: ModalProps) {
  const localAxios = useLocalAxios();
  const [data, setData] = useState<number[]>([]);
  const deleteItem = async () => {
    try {
      let response;
      if (props.page === "project") {
        response = await localAxios.delete(`/techmap-project`, {
          data,
        });
      } else if (props.page === "techmap") {
        response = await localAxios.delete(`/techmap`, {
          data,
        });
      }

      if (response?.status === 200) {
        // 성공
        props.onClose();
      } else {
        alert("삭제 실패");
      }
    } catch (error) {
      console.error("삭제 에러", error);
      alert("삭제 중 에러 발생");
    }
  };

  useEffect(() => {
    const numberList: number[] = [];
    Object.keys(props.id).forEach((key) => {
      if (props.id[key]) numberList.push(Number(key));
    });
    setData(numberList);
  }, [props.isModalOpen]);

  return (
    <>
      {props.isModalOpen && (
        <Modal show={props.isModalOpen} size="md" onClose={props.onClose} popup>
          <Modal.Header />
          <Modal.Body>
            <div className="text-center">
              <HiOutlineExclamationCircle className="mx-auto mb-4 size-14 text-gray-400 dark:text-gray-200" />
              <h3 className="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
                삭제하시겠습니까?
              </h3>
              <div className="flex justify-center gap-4">
                <Button color="failure" onClick={() => deleteItem()}>
                  {"삭제"}
                </Button>
                <Button color="gray" onClick={props.onClose}>
                  취소
                </Button>
              </div>
            </div>
          </Modal.Body>
        </Modal>
      )}
    </>
  );
}
