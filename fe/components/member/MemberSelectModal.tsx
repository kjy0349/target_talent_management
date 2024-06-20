"use client";
import { useLocalAxios } from "@/app/api/axios";
import { MemberAdminSummary } from "@/types/admin/Member";
import { Button, Modal } from "flowbite-react";
import { useEffect, useState } from "react";

export function MemberSelectModal({
  showModal,
  closeModal,
  onSelectMember,
}: {
  showModal: boolean;
  closeModal: () => void;
  onSelectMember: (memberId: number, memberName: string) => void;
}) {
  const [members, setMembers] = useState<MemberAdminSummary[]>([]);
  const localAxios = useLocalAxios();
  useEffect(() => {
    const fetchMembers = async () => {
      const response =
        await localAxios.get<MemberAdminSummary[]>(`/admin/member`);
      setMembers(response.data);
    };

    fetchMembers();
  }, []);

  const handleMemberSelect = (memberId: number, memberName: string) => {
    onSelectMember(memberId, memberName);
    closeModal();
  };

  return (
    <Modal show={showModal} onClose={closeModal}>
      <Modal.Header>멤버 선택</Modal.Header>
      <Modal.Body>
        {members.map((member) => (
          <div
            key={member.id}
            className="cursor-pointer p-2 hover:bg-gray-100"
            onClick={() => handleMemberSelect(member.id, member.name)}
          >
            {member.name}
          </div>
        ))}
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={closeModal}>닫기</Button>
      </Modal.Footer>
    </Modal>
  );
}
