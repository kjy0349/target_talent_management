import { Modal, Button } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { useRef, useState } from "react";
import Spinner from "@/components/common/Spinner";
interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
}

export default function ProfileExcelModal(props: ModalProps) {
  const localAxios = useLocalAxios();
  const ref = useRef<HTMLInputElement>(null);
  const [isLoading, setisLoading] = useState(false);
  const [isError, setisError] = useState(false);

  const handleClickUpload = () => {
    if (ref.current) {
      ref.current!.click();
    }
  };

  const handleUploadExcel = async (e: any) => {
    if (e.target.files) {
      const targetExcel = e.target.files[0];
      setisLoading(true);
      try {
        const formData = new FormData();
        formData.append("file", targetExcel);

        await localAxios.post("/profile/excel-upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }).then((Response) => {
          setisLoading(false);
          if (Response.status === 200) {
            alert(`총 ${Response.data.successCount}개의 프로필을 등록했습니다.(${Response.data.failCount}개 실패)`);
            props.onClose();
          } else {
            setisLoading(false);
            setisError(true);
            alert("실패");
          }
        })
      } catch (error) {
        setisLoading(false);
        setisError(true);
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
          인재 데이터 가져오기
        </Modal.Header>
        <Modal.Body className="px-2 py-1">
          {
            !isLoading && !isError && (
              <Button size="sm" className="mx-auto" onClick={handleClickUpload}>
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
            )
          }
          {
            isLoading && (
              <div
                className="animate-spin rounded-full border-4 border-solid border-current border-t-transparent size-20"
              ></div>
            )
          }
          {
            isError && (
              <div className="flex flex-col items-center">
                <p className="text-center">서버 오류가 발생했습니다.</p>
                <Button
                  onClick={() => {
                    setisError(false);
                    setisLoading(false)
                  }}
                >
                  다시 업로드하기
                </Button>
              </div>
            )
          }
        </Modal.Body>
      </Modal>
    </>
  );
}
