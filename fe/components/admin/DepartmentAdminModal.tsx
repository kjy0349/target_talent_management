"use client";
import React, { useEffect, useState } from "react";
import { Button, Modal, TextInput, Textarea } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { DepartmentAdminSummary } from "@/types/admin/Member";
import { HiCheck, HiX } from "react-icons/hi";
import { MemberSelectModal } from "../member/MemberSelectModal";

interface DepartmentModalProps {
  showModal: boolean;
  closeModal: () => void;
  size: string;
}

interface NewDepartment {
  name: string;
  description: string;
  managerName: string;
}

export function DepartmentModal({
  showModal,
  closeModal,
  size,
}: DepartmentModalProps) {
  const [departments, setDepartments] = useState<DepartmentAdminSummary[]>([]);
  const localAxios = useLocalAxios();
  const [isAddingRow, setIsAddingRow] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [addRow, setAddRow] = useState<NewDepartment>({
    name: "",
    description: "",
    managerName: "",
  });

  const handleAddRowInputChange = (
    field: keyof NewDepartment,
    value: string,
  ) => {
    setAddRow((prevState) => ({
      ...prevState,
      [field]: value,
    }));
  };

  const handleAddDepartment = async () => {
    if (confirm("등록하시겠습니까?")) {
      const result = await localAxios.post<DepartmentAdminSummary>(
        `/admin/department`,
        addRow,
      );
      if (result.status === 200) {
        const nextDepartments = [...departments, result.data];
        setDepartments(nextDepartments);
        setAddRow({
          name: "",
          description: "",
          managerName: "",
        });
        setIsAddingRow(false);
      } else {
        alert("등록에 실패하였습니다. 잠시 후 다시 시도 해주세요.");
      }
    }
  };

  const [editingDepartmentIndex, setEditingDepartmentIndex] = useState<
    number | null
  >(null);

  const handleAddRow = () => {
    setIsAddingRow(!isAddingRow);
    setEditingDepartmentIndex(null);
  };

  useEffect(() => {
    const fetchData = async () => {
      await baseSetting();
    };

    fetchData();
  }, []);

  const baseSetting = async () => {
    const result = await getDepartmentAdminSummary();
    setDepartments(result);
  };

  async function getDepartmentAdminSummary(): Promise<
    DepartmentAdminSummary[]
  > {
    try {
      const response =
        await localAxios.get<DepartmentAdminSummary[]>("/admin/department");
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  const handleSelected = (index: number) => {
    setSelectedIndex(index);
  };

  const handleUpdateSelectedRow = () => {
    if (selectedIndex !== null) {
      setIsAddingRow(false);
      setEditingDepartmentIndex(selectedIndex);
    }
  };

  const handleSaveUpdatedRow = async () => {
    if (confirm("정말 수정하시겠습니까?") && selectedIndex !== null) {
      const url = `/admin/department/${departments[selectedIndex].departmentId}`;
      const result = await localAxios.put(url, departments[selectedIndex]);
      if (result.status === 200) {
        setEditingDepartmentIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const handleCancelUpdate = () => {
    setEditingDepartmentIndex(null);
  };

  const handleInputChange = (
    index: number,
    field: keyof DepartmentAdminSummary,
    value: string,
  ) => {
    setDepartments((prevDepartments) =>
      prevDepartments.map((department, i) =>
        i === index ? { ...department, [field]: value } : department,
      ),
    );
  };

  const handleDeleteSelectedRow = async () => {
    if (confirm("정말 삭제하시겠습니까?") && selectedIndex !== null) {
      const url = `/admin/department/${departments[selectedIndex].departmentId}`;
      const result = await localAxios.delete(url);
      if (result.status === 200) {
        const nextDepartments = departments.filter(
          (d) => d.departmentId !== departments[selectedIndex].departmentId,
        );
        setDepartments(nextDepartments);
        setEditingDepartmentIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const [showMemberSelectModal, setShowMemberSelectModal] = useState(false);

  const handleOpenMemberSelectModal = (index: number) => {
    setSelectedIndex(index);
    setShowMemberSelectModal(true);
  };

  const handleCloseMemberSelectModal = () => {
    setShowMemberSelectModal(false);
  };

  const handleSelectMember = (memberId: number, memberName: string) => {
    if (selectedIndex !== null) {
      if (selectedIndex === departments.length) {
        setAddRow((prevState) => ({
          ...prevState,
          managerId: memberId,
          managerName: memberName,
        }));
      } else {
        setDepartments((prevDepartments) =>
          prevDepartments.map((department, i) =>
            i === selectedIndex
              ? { ...department, managerId: memberId, managerName: memberName }
              : department,
          ),
        );
      }
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>
        <div className="flex w-full items-center justify-between">
          <h3 className="w-36">부서 관리</h3>
        </div>
      </Modal.Header>
      <Modal.Body>
        <div className="relative overflow-x-auto">
          <div className="flex items-center justify-between">
            <div className="flex flex-col space-y-5 p-5">
              <h3 className="text-xl">부서 목록</h3>
            </div>
            <div className="flex flex-row gap-1">
              <Button
                className="h-full bg-primary text-center"
                onClick={handleAddRow}
              >
                부서 추가
              </Button>
              <Button
                className="h-full bg-primary text-center"
                onClick={handleUpdateSelectedRow}
              >
                부서 수정
              </Button>
              <Button
                className="mr-2 h-full bg-primary text-center"
                onClick={handleDeleteSelectedRow}
              >
                부서 제거
              </Button>
            </div>
          </div>

          <table className="w-full text-left text-sm text-gray-500">
            <thead className="bg-gray-50 text-xs uppercase text-gray-700">
              <tr>
                <th scope="col" className="px-6 py-3">
                  <HiCheck />
                </th>
                <th scope="col" className="px-6 py-3">
                  ID
                </th>
                <th scope="col" className="w-1/6 px-6 py-3">
                  부서명
                </th>
                <th scope="col" className="px-6 py-3">
                  설명
                </th>
                <th scope="col" className="px-6 py-3">
                  관리자
                </th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {departments.map((department, index) => (
                <tr key={department.departmentId} className="border-b bg-white">
                  <td className="px-6 py-4">
                    <input
                      type="radio"
                      name="row"
                      className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                      onClick={() => handleSelected(index)}
                    />
                  </td>
                  <td className="px-6 py-4">{department.departmentId}</td>
                  <td className="w-1/6 px-6 py-4">
                    {editingDepartmentIndex === index ? (
                      <input
                        type="text"
                        value={department.name}
                        onChange={(e) =>
                          handleInputChange(index, "name", e.target.value)
                        }
                        className="w-full border border-gray-300 p-2"
                      />
                    ) : (
                      department.name
                    )}
                  </td>
                  <td className="px-6 py-4">
                    {editingDepartmentIndex === index ? (
                      <Textarea
                        value={department.description}
                        onChange={(e) =>
                          handleInputChange(
                            index,
                            "description",
                            e.target.value,
                          )
                        }
                        className="w-full border border-gray-300 p-2 text-xs"
                      />
                    ) : (
                      department.description
                    )}
                  </td>
                  <td className="px-6 py-4 text-xs">
                    {editingDepartmentIndex === index ? (
                      <div
                        className="cursor-pointer text-blue-500 underline"
                        onClick={() => handleOpenMemberSelectModal(index)}
                      >
                        {department.managerName || "멤버 선택하기"}
                      </div>
                    ) : (
                      department.managerName
                    )}
                  </td>
                  {editingDepartmentIndex === index && (
                    <td className="flex gap-1 px-1 py-4">
                      <Button
                        className="w-10 text-xs"
                        onClick={handleSaveUpdatedRow}
                      >
                        <HiCheck />
                      </Button>
                      <Button
                        className="w-10 text-xs"
                        onClick={handleCancelUpdate}
                      >
                        <HiX />
                      </Button>
                    </td>
                  )}
                </tr>
              ))}
              {isAddingRow && (
                <tr className="border-b bg-white">
                  <td className="px-6 py-4">
                    <input
                      type="radio"
                      disabled
                      className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                    />
                  </td>
                  <td className="px-6 py-4">
                    <input
                      type="number"
                      disabled
                      hidden
                      name="row"
                      className="w-full border border-gray-300 p-2"
                    />
                  </td>
                  <td className="w-1/6 px-6 py-4">
                    <input
                      type="text"
                      value={addRow.name}
                      onChange={(e) =>
                        handleAddRowInputChange("name", e.target.value)
                      }
                      className="w-full border border-gray-300 p-2"
                    />
                  </td>
                  <td className="px-6 py-4">
                    <input
                      type="text"
                      value={addRow.description}
                      onChange={(e) =>
                        handleAddRowInputChange("description", e.target.value)
                      }
                      className="w-full border border-gray-300 p-2"
                    />
                  </td>
                  <td className="px-6 py-4">
                    <div
                      className="cursor-pointer text-blue-500 underline"
                      onClick={() =>
                        handleOpenMemberSelectModal(departments.length)
                      }
                    >
                      {addRow.managerName || "멤버 선택하기"}
                    </div>
                  </td>
                  <td className="flex gap-1 px-1 py-4">
                    <Button
                      className="w-10 text-xs"
                      onClick={handleAddDepartment}
                    >
                      <HiCheck />
                    </Button>
                    <Button className="w-10 text-xs" onClick={handleAddRow}>
                      <HiX />
                    </Button>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </Modal.Body>
      <Modal.Footer></Modal.Footer>

      <MemberSelectModal
        showModal={showMemberSelectModal}
        closeModal={handleCloseMemberSelectModal}
        onSelectMember={handleSelectMember}
      />
    </Modal>
  );
}
