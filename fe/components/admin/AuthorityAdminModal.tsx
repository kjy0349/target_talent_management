"use client";
import React, { useEffect, useState } from "react";
import { Button, Modal } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { AuthorityAdminSummary } from "@/types/admin/Member";
import { HiCheck, HiX } from "react-icons/hi";
import { HiCircleStack } from "react-icons/hi2";

interface AuthorityModalProps {
  showModal: boolean;
  closeModal: () => void;
}
interface NewAuthority {
  authName: string;
  authLevel: number;
}
export function AuthorityModal({ showModal, closeModal }: AuthorityModalProps) {
  const baseUrl = "http://localhost:8080";
  const [authorities, setAuthorities] = useState<AuthorityAdminSummary[]>([]);
  const localAxios = useLocalAxios();
  const [isAddingRow, setIsAddingRow] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [addRow, setAddRow] = useState<NewAuthority>({
    authName: "",
    authLevel: 0,
  });
  const handleAddRowInputChange = (
    field: keyof NewAuthority,
    value: string | number,
  ) => {
    setAddRow((prevState) => ({
      ...prevState,
      [field]: value,
    }));
  };
  const handleAddAuthority = async () => {
    if (confirm("등록하시겠습니까?")) {
      const result = await localAxios.post<AuthorityAdminSummary>(
        `${baseUrl}/admin/authority`,
        addRow,
      );
      if (result.status == 200) {
        const nextAuthorities = [...authorities, result.data];
        setAuthorities(nextAuthorities);
        setAddRow({
          authName: "",
          authLevel: 0,
        });
        setIsAddingRow(false);
      } else {
        alert("등록에 실패하였습니다. 잠시 후 다시 시도 해주세요.");
      }
    }
  };

  const [editingAuthorityIndex, setEditingAuthorityIndex] = useState<
    number | null
  >(null);

  const handleAddRow = () => {
    setIsAddingRow(!isAddingRow);
    setEditingAuthorityIndex(null);
  };

  useEffect(() => {
    const fetchData = async () => {
      await baseSetting();
    };

    fetchData();
  }, []);

  const baseSetting = async () => {
    const result = await getAuthorityAdminSummary();
    setAuthorities(result);
  };

  async function getAuthorityAdminSummary(): Promise<AuthorityAdminSummary[]> {
    try {
      const response = await localAxios.get<AuthorityAdminSummary[]>(
        baseUrl + "/admin/authority",
      );
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
      setEditingAuthorityIndex(selectedIndex);
    }
  };

  const handleSaveUpdatedRow = async () => {
    if (confirm("정말 삭제하시겠습니까?") && selectedIndex != null) {
      if (hasOnlyOneLevel1AuthorityAndSelectedAdmin()) {
        alert(
          "어드민은 최소 1개 이상 존재해야 하며, 추가되기 전엔 수정이 불가능합니다.",
        );
        return;
      }
      const url = `${baseUrl}/admin/authority/${authorities[selectedIndex].id}`;
      const result = await localAxios.put(url, authorities[selectedIndex]);
      if (result.status == 200) {
        setEditingAuthorityIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const handleCancelUpdate = () => {
    setEditingAuthorityIndex(null);
  };

  const handleInputChange = (
    index: number,
    field: keyof AuthorityAdminSummary,
    value: string | number,
  ) => {
    setAuthorities((prevAuthorities) =>
      prevAuthorities.map((authority, i) =>
        i === index ? { ...authority, [field]: value } : authority,
      ),
    );
  };
  const hasOnlyOneLevel1AuthorityAndSelectedAdmin = () => {
    const level1Authorities = authorities.filter(
      (authority) => authority.authLevel === 1,
    );
    const selectedAuthority = authorities[selectedIndex!];
    return level1Authorities.length === 1 && selectedAuthority?.authLevel === 1;
  };
  const handleDeleteSelectedRow = async () => {
    if (confirm("정말 삭제하시겠습니까?") && selectedIndex != null) {
      if (hasOnlyOneLevel1AuthorityAndSelectedAdmin()) {
        alert("어드민은 최소 1개 이상 존재해야 합니다.");
        return;
      }
      const url = `${baseUrl}/admin/authority/${authorities[selectedIndex].id}`;
      const result = await localAxios.delete(url);
      if (result.status == 200) {
        const nextAuthorities = authorities.filter(
          (a) => a.id !== authorities[selectedIndex].id,
        );
        setAuthorities(nextAuthorities);
        setEditingAuthorityIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal}>
      <Modal.Header>
        <div className="flex w-full items-center justify-between">
          <h3 className="w-36">권한 관리</h3>
        </div>
      </Modal.Header>
      <Modal.Body>
        <div className="relative overflow-x-auto">
          <div className="flex items-center justify-between">
            <div className="flex flex-col space-y-5 p-5">
              <h3 className="text-xl">권한 목록</h3>
            </div>
            <div className="flex flex-row gap-1">
              <Button
                className="h-full bg-primary text-center"
                onClick={handleAddRow}
              >
                권한 추가
              </Button>
              <Button
                className="h-full bg-primary text-center"
                onClick={handleUpdateSelectedRow}
              >
                권한 수정
              </Button>
              <Button
                className="mr-2 h-full bg-primary text-center"
                onClick={handleDeleteSelectedRow}
              >
                권한 제거
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
                <th scope="col" className="px-6 py-3">
                  권한 이름
                </th>
                <th scope="col" className="px-6 py-3">
                  권한 레벨
                </th>
              </tr>
            </thead>
            <tbody>
              {authorities.map((authority, index) => (
                <tr key={authority.id} className="border-b bg-white">
                  <td className="px-6 py-4">
                    <input
                      type="radio"
                      name="row"
                      className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                      onClick={() => handleSelected(index)}
                    />
                  </td>
                  <td className="px-6 py-4">{authority.id}</td>
                  <td className="px-6 py-4">
                    {editingAuthorityIndex === index ? (
                      <input
                        type="text"
                        value={authority.authName}
                        onChange={(e) =>
                          handleInputChange(index, "authName", e.target.value)
                        }
                        className="w-full border border-gray-300 p-2"
                      />
                    ) : (
                      authority.authName
                    )}
                  </td>
                  <td className="px-6 py-4">
                    {editingAuthorityIndex === index ? (
                      <input
                        type="number"
                        value={authority.authLevel}
                        onChange={(e) =>
                          handleInputChange(
                            index,
                            "authLevel",
                            Number(e.target.value),
                          )
                        }
                        className="w-full border border-gray-300 p-2"
                      />
                    ) : (
                      authority.authLevel
                    )}
                  </td>
                  {editingAuthorityIndex === index && (
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
                  <td className="px-6 py-4">
                    <input
                      type="text"
                      value={addRow.authName}
                      onChange={(e) =>
                        handleAddRowInputChange("authName", e.target.value)
                      }
                      className="w-full border border-gray-300 p-2"
                    />
                  </td>
                  <td className="px-6 py-4">
                    <input
                      type="number"
                      value={addRow.authLevel}
                      onChange={(e) =>
                        handleAddRowInputChange(
                          "authLevel",
                          Number(e.target.value),
                        )
                      }
                      className="w-full border border-gray-300 p-2"
                    />
                  </td>
                  <td className="flex gap-1 px-1 py-4">
                    <Button
                      className="w-10 text-xs"
                      onClick={handleAddAuthority}
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
    </Modal>
  );
}
