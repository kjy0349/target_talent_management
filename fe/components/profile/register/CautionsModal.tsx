"use client";
import { Modal, Button } from "flowbite-react";
import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
export default function CautionsModal() {
  const [openModal, setOpenModal] = useState(true);
  const router = useRouter();
  const closeModal = () => {
    setOpenModal(false);

    router.back();
  };
  return (
    <>
      <Modal show={openModal} onClose={() => closeModal()}>
        <Modal.Header>프로필 등록 주의사항</Modal.Header>
        <Modal.Body>
          <span className="space-y-1 text-base leading-relaxed text-gray-500 dark:text-gray-400">
            <p>
              후보자 프로필 정보수집 관련 합법적인 근거 마련을 위해 정보 출처를
              등록해주시고,
            </p>
            <p>
              영입 목적과 무관한 개인정보는 등록되지 않도록 각별한 주의를
              요청드립니다.
            </p>
            <br />
            <p>
              아래의 각 국가별 제한되는 민감 정보는 등록하지 않도록 바랍니다.
            </p>

            <p>
              · 등록분야 정보 : 공통(고유식별정보), 미국(나이/column1),
              일본(사진)
            </p>
          </span>
        </Modal.Body>
        <Modal.Footer className="justify-end">
          <Button onClick={() => setOpenModal(false)}>확인</Button>
          <Button color="gray" onClick={() => closeModal()}>
            취소
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
