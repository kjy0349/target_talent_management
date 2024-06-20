import { Checkbox, Label, Modal } from "flowbite-react";
import { useState } from "react";
import { useCookies } from "react-cookie";
interface ModalProps {
  isModalOpen: boolean;
  content: string;
  onClose: () => void;
}

export default function MainPopup(props: ModalProps) {
  const [isChecked, setIsChecked] = useState(false);
  const [cookies, setCookie, removeCookie] = useCookies(["checked"]);

  const handleCloseModal = () => {
    if (isChecked) {
      const expiration = new Date();
      expiration.setDate(expiration.getDate() + 7);
      setCookie("checked", true, { expires: expiration });
    }
    props.onClose();
  };
  return (
    <Modal popup show={props.isModalOpen} size={"xl"} onClose={props.onClose}>
      <Modal.Header>
        <div className="ml-3 mt-1 text-2xl font-bold">알림 메시지</div>
      </Modal.Header>
      <Modal.Body>
        <div className="max-h-[40vh] overflow-y-auto whitespace-pre-wrap p-10 text-lg">
          {props.content}
        </div>
        <div className="mt-4 flex flex-row items-center justify-between">
          <div className="flex flex-row items-center gap-2">
            <Checkbox
              id="24"
              onChange={() => setIsChecked(!isChecked)}
              checked={isChecked}
            />
            <Label htmlFor="24">24시간 보지 않기</Label>
          </div>
          <button
            className="rounded border p-2 px-4 hover:text-red-500"
            onClick={handleCloseModal}
          >
            닫기
          </button>
        </div>
      </Modal.Body>
    </Modal>
  );
}
