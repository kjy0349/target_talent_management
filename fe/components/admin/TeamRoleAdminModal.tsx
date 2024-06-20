"use client";
import React, { useEffect, useState } from "react";
import { Button, Modal, Tabs } from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { TeamAdminSummary, RoleAdminSummary } from "@/types/admin/Member";
import { HiCheck, HiX } from "react-icons/hi";
import { HiCircleStack } from "react-icons/hi2";

interface TeamRoleModalProps {
  showModal: boolean;
  closeModal: () => void;
  size: string;
}

interface NewTeam {
  name: string;
  description: string;
}

interface NewRole {
  description: string;
}

export function TeamRoleModal({
  showModal,
  closeModal,
  size,
}: TeamRoleModalProps) {
  const baseUrl = "http://localhost:8080";
  const [teams, setTeams] = useState<TeamAdminSummary[]>([]);
  const [roles, setRoles] = useState<RoleAdminSummary[]>([]);
  const localAxios = useLocalAxios();
  const [isAddingRow, setIsAddingRow] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [addTeamRow, setAddTeamRow] = useState<NewTeam>({
    name: "",
    description: "",
  });
  const [addRoleRow, setAddRoleRow] = useState<NewRole>({
    description: "",
  });

  const handleAddRowInputChange = (
    field: keyof NewTeam | keyof NewRole,
    value: string,
    type: "team" | "role",
  ) => {
    if (type === "team") {
      setAddTeamRow((prevState) => ({
        ...prevState,
        [field]: value,
      }));
    } else {
      setAddRoleRow((prevState) => ({
        ...prevState,
        [field]: value,
      }));
    }
  };

  const handleAddTeam = async () => {
    if (confirm("팀을 등록하시겠습니까?")) {
      const result = await localAxios.post<TeamAdminSummary>(
        `${baseUrl}/admin/team`,
        addTeamRow,
      );
      if (result.status === 200) {
        const nextTeams = [...teams, result.data];
        setTeams(nextTeams);
        setAddTeamRow({
          name: "",
          description: "",
        });
        setIsAddingRow(false);
      } else {
        alert("등록에 실패하였습니다. 잠시 후 다시 시도 해주세요.");
      }
    }
  };

  const handleAddRole = async () => {
    if (confirm("역할을 등록하시겠습니까?")) {
      const result = await localAxios.post<RoleAdminSummary>(
        `${baseUrl}/admin/role`,
        addRoleRow,
      );
      if (result.status === 200) {
        const nextRoles = [...roles, result.data];
        setRoles(nextRoles);
        setAddRoleRow({
          description: "",
        });
        setIsAddingRow(false);
      } else {
        alert("등록에 실패하였습니다. 잠시 후 다시 시도 해주세요.");
      }
    }
  };

  const [editingIndex, setEditingIndex] = useState<number | null>(null);

  const handleAddRow = () => {
    setIsAddingRow(!isAddingRow);
    setEditingIndex(null);
  };

  useEffect(() => {
    const fetchData = async () => {
      await baseSetting();
    };

    fetchData();
  }, []);

  const baseSetting = async () => {
    const teamResult = await getTeamAdminSummary();
    setTeams(teamResult);
    const roleResult = await getRoleAdminSummary();
    setRoles(roleResult);
  };

  async function getTeamAdminSummary(): Promise<TeamAdminSummary[]> {
    try {
      const response = await localAxios.get<TeamAdminSummary[]>(
        baseUrl + "/admin/team",
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  }

  async function getRoleAdminSummary(): Promise<RoleAdminSummary[]> {
    try {
      const response = await localAxios.get<RoleAdminSummary[]>(
        baseUrl + "/admin/role",
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
      setEditingIndex(selectedIndex);
    }
  };

  const handleSaveUpdatedTeam = async () => {
    if (confirm("정말 수정하시겠습니까?") && selectedIndex !== null) {
      const url = `${baseUrl}/admin/team/${teams[selectedIndex].teamId}`;
      const result = await localAxios.put(url, teams[selectedIndex]);
      if (result.status === 200) {
        setEditingIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const handleSaveUpdatedRole = async () => {
    if (confirm("정말 수정하시겠습니까?") && selectedIndex !== null) {
      const url = `${baseUrl}/admin/role/${roles[selectedIndex].roleId}`;
      const result = await localAxios.put(url, roles[selectedIndex]);
      if (result.status === 200) {
        setEditingIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const handleCancelUpdate = () => {
    setEditingIndex(null);
  };

  const handleTeamInputChange = (
    index: number,
    field: keyof TeamAdminSummary,
    value: string | number,
  ) => {
    setTeams((prevTeams) =>
      prevTeams.map((team, i) =>
        i === index ? { ...team, [field]: value } : team,
      ),
    );
  };

  const handleRoleInputChange = (
    index: number,
    field: keyof RoleAdminSummary,
    value: string,
  ) => {
    setRoles((prevRoles) =>
      prevRoles.map((role, i) =>
        i === index ? { ...role, [field]: value } : role,
      ),
    );
  };

  const handleDeleteSelectedTeam = async () => {
    if (confirm("정말 삭제하시겠습니까?") && selectedIndex !== null) {
      const url = `${baseUrl}/admin/team/${teams[selectedIndex].teamId}`;
      const result = await localAxios.delete(url);
      if (result.status === 200) {
        const nextTeams = teams.filter(
          (t) => t.teamId !== teams[selectedIndex].teamId,
        );
        setTeams(nextTeams);
        setEditingIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  const handleDeleteSelectedRole = async () => {
    if (confirm("정말 삭제하시겠습니까?") && selectedIndex !== null) {
      const url = `${baseUrl}/admin/role/${roles[selectedIndex].roleId}`;
      const result = await localAxios.delete(url);
      if (result.status === 200) {
        const nextRoles = roles.filter(
          (r) => r.roleId !== roles[selectedIndex].roleId,
        );
        setRoles(nextRoles);
        setEditingIndex(null);
        setIsAddingRow(false);
        setSelectedIndex(null);
      } else {
        alert("잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <Modal show={showModal} onClose={closeModal} size={size}>
      <Modal.Header>
        <div className="flex w-full items-center justify-between">
          <h3 className="w-36">팀/역할 관리</h3>
        </div>
      </Modal.Header>
      <Modal.Body>
        <Tabs>
          <Tabs.Item title="팀 관리">
            {/* 팀 관리 탭 내용 */}
            <div className="relative overflow-x-auto">
              <div className="flex items-center justify-between">
                <div className="flex flex-col space-y-5 p-5">
                  <h3 className="text-xl">팀 목록</h3>
                </div>
                <div className="flex flex-row gap-1">
                  <Button
                    className="h-full bg-primary text-center"
                    onClick={handleAddRow}
                  >
                    팀 추가
                  </Button>
                  <Button
                    className="h-full bg-primary text-center"
                    onClick={handleUpdateSelectedRow}
                  >
                    팀 수정
                  </Button>
                  <Button
                    className="mr-2 h-full bg-primary text-center"
                    onClick={handleDeleteSelectedTeam}
                  >
                    팀 제거
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
                      팀명
                    </th>
                    <th scope="col" className="px-6 py-3">
                      설명
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {teams.map((team, index) => (
                    <tr key={team.teamId} className="border-b bg-white">
                      <td className="px-6 py-4">
                        <input
                          type="radio"
                          name="row"
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                          onClick={() => handleSelected(index)}
                        />
                      </td>
                      <td className="px-6 py-4">{team.teamId}</td>
                      <td className="px-6 py-4">
                        {editingIndex === index ? (
                          <input
                            type="text"
                            value={team.name}
                            onChange={(e) =>
                              handleTeamInputChange(
                                index,
                                "name",
                                e.target.value,
                              )
                            }
                            className="w-full border border-gray-300 p-2"
                          />
                        ) : (
                          team.name
                        )}
                      </td>
                      <td className="px-6 py-4">
                        {editingIndex === index ? (
                          <input
                            type="text"
                            value={team.description}
                            onChange={(e) =>
                              handleTeamInputChange(
                                index,
                                "description",
                                e.target.value,
                              )
                            }
                            className="w-full border border-gray-300 p-2"
                          />
                        ) : (
                          team.description
                        )}
                      </td>
                      {editingIndex === index && (
                        <td className="flex gap-1 px-1 py-4">
                          <Button
                            className="w-10 text-xs"
                            onClick={handleSaveUpdatedTeam}
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
                          value={addTeamRow.name}
                          onChange={(e) =>
                            handleAddRowInputChange(
                              "name",
                              e.target.value,
                              "team",
                            )
                          }
                          className="w-full border border-gray-300 p-2"
                        />
                      </td>
                      <td className="px-6 py-4">
                        <input
                          type="text"
                          value={addTeamRow.description}
                          onChange={(e) =>
                            handleAddRowInputChange(
                              "description",
                              e.target.value,
                              "team",
                            )
                          }
                          className="w-full border border-gray-300 p-2"
                        />
                      </td>
                      <td className="flex gap-1 px-1 py-4">
                        <Button
                          className="w-10 text-xs"
                          onClick={handleAddTeam}
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
          </Tabs.Item>
          <Tabs.Item title="역할 관리">
            {/* 역할 관리 탭 내용 */}
            <div className="relative overflow-x-auto">
              <div className="flex items-center justify-between">
                <div className="flex flex-col space-y-5 p-5">
                  <h3 className="text-xl">역할 목록</h3>
                </div>
                <div className="flex flex-row gap-1">
                  <Button
                    className="h-full bg-primary text-center"
                    onClick={handleAddRow}
                  >
                    역할 추가
                  </Button>
                  <Button
                    className="h-full bg-primary text-center"
                    onClick={handleUpdateSelectedRow}
                  >
                    역할 수정
                  </Button>
                  <Button
                    className="mr-2 h-full bg-primary text-center"
                    onClick={handleDeleteSelectedRole}
                  >
                    역할 제거
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
                      역할 설명
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {roles.map((role, index) => (
                    <tr key={role.roleId} className="border-b bg-white">
                      <td className="px-6 py-4">
                        <input
                          type="radio"
                          name="row"
                          className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                          onClick={() => handleSelected(index)}
                        />
                      </td>
                      <td className="px-6 py-4">{role.roleId}</td>
                      <td className="px-6 py-4">
                        {editingIndex === index ? (
                          <input
                            type="text"
                            value={role.description}
                            onChange={(e) =>
                              handleRoleInputChange(
                                index,
                                "description",
                                e.target.value,
                              )
                            }
                            className="w-full border border-gray-300 p-2"
                          />
                        ) : (
                          role.description
                        )}
                      </td>
                      {editingIndex === index && (
                        <td className="flex gap-1 px-1 py-4">
                          <Button
                            className="w-10 text-xs"
                            onClick={handleSaveUpdatedRole}
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
                          value={addRoleRow.description}
                          onChange={(e) =>
                            handleAddRowInputChange(
                              "description",
                              e.target.value,
                              "role",
                            )
                          }
                          className="w-full border border-gray-300 p-2"
                        />
                      </td>
                      <td className="flex gap-1 px-1 py-4">
                        <Button
                          className="w-10 text-xs"
                          onClick={handleAddRole}
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
          </Tabs.Item>
        </Tabs>
      </Modal.Body>
      <Modal.Footer></Modal.Footer>
    </Modal>
  );
}
