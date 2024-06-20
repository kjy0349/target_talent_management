import { Modal, Button, Label, Radio } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { useEffect, useRef, useState } from "react";
import { CheckboxState } from "@/types/techmap/common";
interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  techmapId: number;
  checkListId: CheckboxState;
}

export default function ExcelModal(props: ModalProps) {
  const localAxios = useLocalAxios();
  const ref = useRef<HTMLInputElement>(null);
  const [selectedValue, setSelectedValue] = useState("techmap-project");
  const [data, setData] = useState<number[]>([]);
  const handleChecked = (e: any) => {
    setSelectedValue(e.target.value);
  };

  useEffect(() => {
    setSelectedValue("techmap-project");
    if (hasCheckedItems()) {
      const numberList: number[] = [];
      Object.keys(props.checkListId).forEach((key) => {
        if (props.checkListId[key]) numberList.push(Number(key));
      });
      setData(numberList);
    }
  }, [props.isModalOpen]);

  const handleClickUpload = () => {
    if (selectedValue === "target-lab" && !hasCheckedItems()) {
      alert("선택한 항목이 없습니다.");
      return;
    }
    if (ref.current) {
      ref.current!.click();
    }
  };
  const hasCheckedItems = () => {
    return Object.values(props.checkListId ?? {}).some((value) => value);
  };

  // 엑셀파일 업로드를 한다.
  const handleUploadExcel = async (e: any) => {
    // e.preventDefault();
    // target-lab인데 id가 없으면 alert를 한다.

    if (e.target.files) {
      const targetExcel = e.target.files[0];
      const formData = new FormData();
      let url = "";
      formData.append("file", targetExcel);
      if (selectedValue === "target-lab") {
        url = `/${selectedValue}/excel-upload`;
        formData.append(
          "id",
          new Blob([JSON.stringify(data)], { type: "application/json" }),
        );
      } else {
        url = `/${selectedValue}/excel-upload/${props.techmapId}`;
      }
      try {
        const response = await localAxios.post(url, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        if (response.status === 200) {
          alert(response.data);
          props.onClose();
        } else if (response.status === 400) {
          alert(response.data);
        } else {
          alert("실패");
        }
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <>
      <Modal popup show={props.isModalOpen} size={"sm"} onClose={props.onClose}>
        <Modal.Header
          theme={{
            title: "text-lg font-semibold",
          }}
        >
          엑셀 업로드 양식 유형을 선택하세요.
        </Modal.Header>
        <Modal.Body className="px-2 py-1">
          <fieldset
            className="flex flex-col space-y-4 px-5 "
            onChange={handleChecked}
          >
            <div className="flex space-x-2 ">
              <Radio
                id="techmap-list"
                name="upload"
                value="techmap-project"
                defaultChecked
              />
              <Label htmlFor="techmap-list">
                기술 인재Pool 현황 리스트(.xlsx)
              </Label>
            </div>
            <div className="flex space-x-2">
              <Radio id="target-lab" name="upload" value="target-lab" />
              <Label htmlFor="target-lab">techLab 리스트(.xlsx)</Label>
            </div>
          </fieldset>
        </Modal.Body>
        <Modal.Footer className="justify-end px-4 pb-3 pt-1">
          <Button size="sm" onClick={handleClickUpload}>
            파일 불러오기
            <input
              id="input-upload"
              type="file"
              className="hidden"
              accept=".xlsx"
              onChange={handleUploadExcel}
              ref={ref}
            />
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
