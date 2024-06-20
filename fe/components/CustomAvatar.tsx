import { ComponentProps, ReactElement, useState } from "react";
import {
  Avatar,
  AvatarColors,
  AvatarSizes,
  Button,
  FlowbiteAvatarTheme,
  FlowbitePositions,
  Modal,
} from "flowbite-react";
import { ProjectFull, ProjectMemberSummary } from "@/types/admin/Project";

interface CustomAvatarProps {
  member: ProjectMemberSummary;
  ownerId: number;
  onDelete: (id: number) => void;
}
const CustomAvatar = ({ member, onDelete, ownerId }: CustomAvatarProps) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleAvatarClick = () => {
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <Avatar
        img={member.profileImage}
        rounded={true}
        size="sm"
        onClick={handleAvatarClick}
        className="mx-1 cursor-pointer"
      />
      <Modal
        show={isModalOpen}
        onClose={handleModalClose}
        size="lg"
        popup={true}
      >
        <Modal.Header />
        <Modal.Body>
          <div className="space-y-6 p-6 text-center">
            <Avatar
              img={member.profileImage}
              size="xl"
              rounded={true}
              className="mx-auto mb-4"
            />
            <h3 className="mb-2 text-lg font-medium">{member.name}</h3>
            <p className="mb-1">
              <span className="font-medium">이메일:</span>{" "}
              {"wntjrdbs@gmail.com"}
            </p>
            <p className="mb-1">
              <span className="font-medium">전화번호:</span> {"010-3141-5278"}
            </p>
            <p>
              <span className="font-medium">부서:</span> {"DX"}
            </p>
            <div className="flex justify-center">
              {ownerId != member.memberId && (
                <Button
                  className="bg-red-500"
                  onClick={() => onDelete(member.memberId)}
                >
                  삭제
                </Button>
              )}
            </div>
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
};
export default CustomAvatar;
