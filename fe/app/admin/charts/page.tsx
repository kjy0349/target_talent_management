"use client";

import {
  Badge,
  Button,
  Table,
  Checkbox,
  Label,
  TextInput,
  Popover,
  Select,
} from "flowbite-react";
import { HiCheck, HiX } from "react-icons/hi";
import { useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { AxiosError, AxiosResponse, isAxiosError } from "axios";
import {
  AuthorityAdminSummary,
  DepartmentAdminFull,
  DepartmentAdminSummary,
  JobRankAdminSummary,
  RoleAdminSummary,
  SchoolFull,
  TeamAdminFull,
  TeamAdminSummary,
} from "@/types/admin/Member";
import { MemberSelectModal } from "@/components/member/MemberSelectModal";
import MemberSelectionModal from "@/components/member/MemberSelectionModal";
import { DepartmentModal } from "@/components/admin/DepartmentAdminModal";
import DepartmentSelectionModal from "@/components/member/DepartmentSelectionModal";

const category = [
  { id: "authority", name: "권한" },
  { id: "department", name: "사업부명" },
  { id: "team", name: "부서명" },
  { id: "role", name: "담당 업무" },
  // { id: "popupMessage", name: "팝업 메시지 설정" },
  // { id: "menu", name: "메뉴 관리" },
  { id: "school", name: "학교 관리" },
  { id: "lab", name: "랩실 관리" },
  // { id: "profileColumn", name: "프로필 항목 관리" },
  { id: "jobrank", name: "직급 관리" },
];

interface Category {
  id: string;
  name: string;
}

interface School {
  id: number;
  schoolName: string;
  country: string;
}
interface Lab {
  id: number;
  labName: string;
  labProfessor: string;
  researchDescription: string;
  researchType: string;
  researchResult: string;
  major: string;
  schoolId: number;
  schoolName: string;
}
interface JobRank {
  id: number;
  description: string;
}

type DataType = School[] | Lab[] | JobRank[];
const headCell = ["ID", "권한 이름", "권한 레벨", "메모"];
const schoolHeadCell = ["ID", "학교이름", "국가"];
const jobRankHeadCell = ["ID", "설명"];
const labHeadCell = [
  // "ID",
  "학교",
  "전공",
  "연구실명",
  "지도교수",
  "연구설명",
  "연구타입",
  "연구결과",
];

type FormData = {
  labId?: number;
  labName?: string;
  labProfessor?: string;
  researchDescription?: string;
  researchType?: string;
  researchResult?: string;
  major?: string;
  schoolId?: number;
  schoolName?: string;
  country?: string;
  id?: number;
  description?: string;
};

interface SchoolTableProps {
  schools: School[];
  handleSelected: (index: number, data: School) => void;
  isEditing: boolean;
  selectedIndex: number | null;
  isAdding: boolean;
  newSchool?: FormData;
  handleNewSchoolChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handleEditSchoolChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

interface LabTableProps {
  labs: Lab[];
  schools: School[];
  selectedTypeId: number;
  handleSelected: (index: number, data: Lab) => void;
  isEditing: boolean;
  selectedIndex: number | null;
  isAdding: boolean;
  newLab?: FormData;
  handleNewLabChange: (event: any) => void;
  handleEditLabChange: (event: any) => void;
}
interface JobRankTableProps {
  jobRanks: JobRank[];
  handleSelected: (index: number, data: JobRank) => void;
  isEditing: boolean;
  selectedIndex: number | null;
  isAdding: boolean;
  newJobRank?: FormData;
  handleNewJobRankChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  handleEditJobRankChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const ChartsManageTable = () => {
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];
  const [selectedCategoryId, setSelectedCategoryId] = useState("0");
  const [selectedTypeId, setSelectedTypeId] = useState(-1);
  const [selectedColumnId, setSelectedColumnId] = useState(0);
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const [formData, setFormData] = useState<DataType | null>(null);
  const [schoolData, setSchoolData] = useState<School[]>([]);
  const [jobRankData, setJobRanksData] = useState<JobRank[]>([]);
  const [authorityData, setAuthorityData] = useState<AuthorityAdminSummary[]>(
    [],
  );
  const [departmentData, setDepartmentData] = useState<DepartmentAdminFull[]>(
    [],
  );
  const [teamData, setTeamData] = useState<TeamAdminFull[]>([]);
  const [roleData, setRoleData] = useState<RoleAdminSummary[]>([]);
  const [newData, setNewData] = useState({});
  const [editData, setEditData] = useState({});
  const localAxios = useLocalAxios();
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [isAdding, setIsAdding] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isLabEditing, setIsLabEditing] = useState<boolean>(false);

  const handleCagegory = (category: Category) => {
    setSelectedCategoryId(category.id);
    setSelectedIndex(null);
    setIsAdding(false);
    setIsEditing(false);
    fetch(category.id);
    // 여기서 fetch를 한다.
  };

  const handleType = (index: number, id: number) => {
    setSelectedTypeId(index);
    setSelectedIndex(null);
    setIsAdding(false);
    setIsEditing(false);
    if (id === -1) {
      fetch(selectedCategoryId);
    } else {
      fetchLab(id);
      setNewData({ schoolId: id });
    }
  };

  const fetchLab = async (id: number) => {
    setFormData(null);
    const response = await localAxios
      .get(`/admin/school/${id}/lab`)
      .then((data) => {
        return data.data;
      })
      .then((res) => {
        setFormData(res);
      })
      .catch((error) => {
        alert(error);
      });
  };

  // 데이터 받기
  const fetch = async (category: string) => {
    // lab인 경우
    setFormData(null);
    if (category === "lab") {
      try {
        // lab인 경우 일단 학교 정보를 받아와야 한다.
        setIsLoading(true);
        const response = await localAxios.get(`/admin/school`);
        if (response.status === 200) {
          setSelectedIndex(null);
          setSchoolData(response.data);
        }

        localAxios
          .get(`/admin/lab`)
          .then((data) => {
            return data.data;
          })
          .then((res) => {
            setFormData(res);
          })
          .catch((error) => {
            console.error("에러가 발생했습니다.", error);
          });
      } catch (error) {
        if (isAxiosError(error)) {
          if (error.response?.status === 500) {
            alert("서버와 연결할 수 없습니다.");
          }
        }
      }
    } else {
      try {
        const response = await localAxios.get(`/admin/${category}`);
        if (response.status === 200) {
          if (category == "jobrank") {
            setJobRanksData(response.data);
          } else if (category == "authority") {
            setAuthorityData(response.data);
          } else if (category == "department") {
            setDepartmentData(response.data);
          } else if (category == "team") {
            setTeamData(response.data);
          } else if (category == "role") {
            setRoleData(response.data);
          } else if (category == "school") {
            setSchoolData(response.data);
          }

          setFormData(response.data);
          setSelectedIndex(null);
        }
      } catch (error) {
        if (isAxiosError(error)) {
          if (error.response?.status === 500) {
            alert("서버와 연결할 수 없습니다.");
          }
        }
      }
    }
  };
  const handleAdd = () => {
    setIsAdding(true);
    setSelectedIndex(null); // Reset selected index when adding a new item
  };

  const handleEdit = async () => {
    if (selectedColumnId !== null) {
      if (selectedCategoryId == "lab") {
        if (selectedIndex !== null) {
          setIsEditing(true);
          // 이제 수정할 값들을 불러 저장해야 한다.
        } else {
          alert("수정할 항목을 선택하세요.");
        }
        // setIsEditing(true);
      } else {
        switch (selectedCategoryId) {
          case "authority":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                auth,
              );
              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
          case "department":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                dept,
              );

              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              if (err.status == 500) {
                alert("이미 부서장으로 할당된 인원입니다.");
              }
              console.error(err);
            }
            break;
          case "team":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                team,
              );

              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
          case "role":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                role,
              );

              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
          case "school":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                school,
              );
              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
          case "lab":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedIndex}`,
              );
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
          case "jobrank":
            try {
              const response = await localAxios.put(
                `/admin/${selectedCategoryId}/${selectedColumnId}`,
                jobRank,
              );
              if (response.status == 200) {
                alert("수정에 성공하였습니다.");
                fetch(selectedCategoryId);
              }
            } catch (e) {
              const err = e as AxiosError;
              console.error(err);
            }
            break;
        }
      }

      // if (selectedIndex !== null) {
      //   setIsEditing(true);
      // 이제 수정할 값들을 불러 저장해야 한다.
      // } else {
      //   alert("수정할 항목을 선택하세요.");
      // }
    }
  };

  const handleSave = async () => {
    // 신규
    if (isAdding) {
      if (selectedCategoryId === "authority") {
        try {
          const response = await localAxios.post(
            `/admin/authority`,
            newAuthData,
          );
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
            setNewAuthData(initAuth);
          }
        } catch (e) {
          const err = e as AxiosError;
          if (err.response?.status == 406) {
            alert("중복된 권한명 입니다.");
          }
        }
      } else if (selectedCategoryId === "department") {
        try {
          const response = await localAxios.post(
            `/admin/department`,
            newDeptData,
          );
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
            setNewDeptData(initDepartment);
          }
        } catch (e) {
          const err = e as AxiosError;
          if (err.response?.status == 406) {
            alert("중복된 사업부명 입니다.");
          }
        }
      } else if (selectedCategoryId === "team") {
        const response = await localAxios.post(`/admin/team`, newTeam);
        if (response.status === 200) {
          alert("저장 완료!");
          fetch(selectedCategoryId);
          setNewTeam(initTeam);
        }
      } else if (selectedCategoryId === "role") {
        const response = await localAxios.post(`/admin/role`, newRole);
        if (response.status === 200) {
          alert("저장 완료!");
          fetch(selectedCategoryId);
          setNewRole(initRole);
        }
      } else if (selectedCategoryId === "school") {
        try {
          const response = await localAxios.post(`/admin/school`, newSchool);
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
          }
        } catch (e) {
          const err = e as AxiosError;
          if (err.response?.status == 406) {
            alert("중복된 학교명 입니다.");
          }
        }
      } else if (selectedCategoryId === "lab") {
        try {
          const response = await localAxios.post(`/admin/lab`, newData);
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
          }
        } catch (e) {
          const err = e as AxiosError;
          if (err.response?.status == 406) {
            alert("중복된 연구실명 입니다.");
          }
        }
      } else if (selectedCategoryId === "jobrank") {
        try {
          const response = await localAxios.post(`/admin/jobrank`, newJobRank);
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
          }
        } catch (e) {
          const err = e as AxiosError;
          if (err.response?.status == 406) {
            alert("중복된 직급명 입니다.");
          }
        }
      }
      setIsAdding(false);
      setNewData({});
    } else {
      // 수정
      setIsEditing(false);
      setEditData({});
      if (selectedCategoryId === "jobrank") {
        try {
          const response = await localAxios.put(
            `/admin/${selectedCategoryId}/${selectedColumnId}`,
            jobRank,
          );
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
          }
        } catch (error) {
          alert(error);
        }
      } else {
        try {
          const response = await localAxios.put(
            `/admin/${selectedCategoryId}/${selectedIndex}`,
            editData,
          );
          if (response.status === 200) {
            alert("저장 완료!");
            fetch(selectedCategoryId);
          }
        } catch (error) {
          alert(error);
        }
      }
    }
  };
  const handleDelete = async () => {
    if (selectedColumnId !== null) {
      try {
        const response = await localAxios.delete(
          `/admin/${selectedCategoryId}/${selectedColumnId}`,
        );
        if (response.status === 200) {
          alert("삭제를 성공했습니다.");
          setSelectedTypeId(-1);

          // setSelectedIndex(setSelectedIndex);
          // setSelectedColumnId(selectedColumnId);
          fetch(selectedCategoryId);
        }
      } catch (error) {
        alert("삭제를 실패했습니다.");
        console.error(error);
      }
    } else {
      alert("삭제할 항목을 선택하세요.");
    }
  };
  const handleChange = (event: any) => {
    const { name, value } = event.target;
    const val = value;
    setNewData((prevState) => ({
      ...prevState,
      [name]: val,
    }));
  };

  const handleEditChange = (event: any) => {
    const { name, value } = event.target;
    setEditData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const isSchoolArray = (data: DataType): data is School[] => {
    return true;
    return Array.isArray(data) && data.length > 0 && "schoolName" in data[0];
  };

  const isLabArray = (data: DataType): data is Lab[] => {
    return true;
    return Array.isArray(data) && data.length > 0 && "labName" in data[0];
  };

  const isJobRankArray = (data: DataType): data is JobRank[] => {
    return true;
    // return Array.isArray(data) && data.length > 0 && "id" in data[0];
  };

  // 셀렉트 버튼 누를 경우
  const handleSelected = (id: number, data: any) => {
    setSelectedIndex(id);
    setSelectedColumnId(id);
    if (selectedCategoryId === "school") {
      const { id, ...newData } = data;
      setEditData(newData);
    } else if (selectedCategoryId === "lab") {
      const { id, schoolName, ...newData } = data;
      setEditData(newData);
    } else if (selectedCategoryId === "jobrank") {
      const { id, ...newData } = data;
      setEditData(newData);
    }
  };
  const initAuth: AuthorityAdminSummary = {
    id: 0,
    authName: "",
    authDescription: "",
    authLevel: 6,
  };
  const initDepartment: DepartmentAdminFull = {
    departmentId: 0,
    name: "",
    description: "",
    managerName: "",
    managerId: 0,
  };

  const initTeam: TeamAdminFull = {
    teamId: 0,
    name: "",
    description: "",
    departmentName: "",
    departmentId: 0,
  };

  const initRole: RoleAdminSummary = {
    roleId: 0,
    description: "",
  };

  const initJobRank: JobRankAdminSummary = {
    id: 0,
    description: "",
  };

  const initSchool: SchoolFull = {
    id: 0,
    schoolName: "",
    country: "",
  };

  const [newAuthData, setNewAuthData] =
    useState<AuthorityAdminSummary>(initAuth);
  const [auth, setAuth] = useState<AuthorityAdminSummary>(initAuth);
  const [newDeptData, setNewDeptData] =
    useState<DepartmentAdminFull>(initDepartment);
  const [dept, setDept] = useState<DepartmentAdminFull>(initDepartment);
  const [openDeptMemberUpdateModal, setOpenDeptMemberUpdateModal] =
    useState<boolean>(false);
  const [openDeptMemberCreateModal, setOpenDeptMemberCreateModal] =
    useState<boolean>(false);

  const [team, setTeam] = useState<TeamAdminFull>(initTeam);
  const [newTeam, setNewTeam] = useState<TeamAdminFull>(initTeam);
  const [openTeamDeptUpdateModal, setOpenTeamDeptUpdateModal] =
    useState<boolean>(false);
  const [openTeamDeptCreateModal, setOpenTeamDeptCreateModal] =
    useState<boolean>(false);

  const [role, setRole] = useState<RoleAdminSummary>(initRole);
  const [newRole, setNewRole] = useState<RoleAdminSummary>(initRole);

  const [jobRank, setJobRank] = useState<JobRankAdminSummary>(initJobRank);
  const [newJobRank, setNewJobRank] =
    useState<JobRankAdminSummary>(initJobRank);

  const [school, setSchool] = useState<SchoolFull>(initSchool);
  const [newSchool, setNewSchool] = useState<SchoolFull>(initSchool);

  const onCloseDeptMemberSelect = (memberId: number, memberName: string) => {
    if (isAdding) {
      setNewDeptData({
        ...newDeptData,
        managerId: memberId,
        managerName: memberName,
      });
    } else {
      setDept({
        ...dept,
        managerId: memberId,
        managerName: memberName,
      });
    }
  };

  const onCloseTeamDeptSelect = (
    departmentId: number,
    departmentName: string,
  ) => {
    if (isAdding) {
      setNewTeam({
        ...newTeam,
        departmentId: departmentId,
        departmentName: departmentName,
      });
    } else {
      setTeam({
        ...team,
        departmentId: departmentId,
        departmentName: departmentName,
      });
    }
  };

  const handleColumn = (id: number, category: string) => {
    setSelectedColumnId(id);
    switch (category) {
      case "authority":
        const temp = authorityData.find((a) => a.id == id);
        if (temp != undefined) {
          const nextAuth = {
            id: temp.id ?? initAuth.id,
            authName: temp.authName ?? initAuth.authName,
            authDescription: temp.authDescription ?? initAuth.authDescription,
            authLevel: temp.authLevel ?? initAuth.authLevel,
          };
          setAuth(nextAuth);
        }
        break;
      case "department":
        const dtemp = departmentData.find((d) => d.departmentId == id);
        if (dtemp != undefined) {
          const nextDept = {
            departmentId: dtemp.departmentId ?? initDepartment.departmentId,
            name: dtemp.name ?? initDepartment.name,
            description: dtemp.description ?? initDepartment.description,
            managerName: dtemp.managerName ?? initDepartment.managerName,
            managerId: dtemp.managerId ?? initDepartment.managerId,
          };
          setDept(nextDept);
        }
        break;
      case "team":
        const ttemp = teamData.find((t) => t.teamId == id);
        if (ttemp != undefined) {
          const nextTeam = {
            teamId: ttemp.teamId ?? initTeam.teamId,
            name: ttemp.name ?? initTeam.name,
            description: ttemp.description ?? initTeam.description,
            departmentName: ttemp.departmentName ?? initTeam.departmentName,
            departmentId: ttemp.departmentId ?? initTeam.departmentId,
          };
          setTeam(nextTeam);
        }

        break;
      case "role":
        const rtemp = roleData.find((r) => r.roleId == id);
        if (rtemp != undefined) {
          const nextRole = {
            roleId: rtemp.roleId ?? initRole.roleId,
            description: rtemp.description ?? initRole.description,
          };
          setRole(nextRole);
        }
        break;
      case "jobrank":
        const jtemp = jobRankData.find((j) => j.id == id);
        if (jtemp != undefined) {
          const nextJobRank = {
            id: jtemp.id ?? initJobRank.id,
            description: jtemp.description ?? initJobRank.description,
          };
          setJobRank(nextJobRank);
        }
        break;
      case "school":
        const stemp = schoolData.find((s) => s.id == id);
        if (stemp != undefined) {
          const nextSchool = {
            id: stemp.id ?? initSchool.id,
            schoolName: stemp.schoolName ?? initSchool.schoolName,
            country: stemp.country ?? initSchool.country,
          };
          setSchool(nextSchool);
        }
    }
  };

  const handleAuthInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAuth({
      ...auth,
      [e.target.name]: e.target.value,
    });
  };

  const handleSchoolInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSchool({
      ...school,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewAuthDataInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setNewAuthData({
      ...newAuthData,
      [e.target.name]: e.target.value,
    });
  };

  const handleDeptInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setDept({
      ...dept,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewDeptInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewDeptData({
      ...newDeptData,
      [e.target.name]: e.target.value,
    });
  };

  const handleTeamInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTeam({
      ...team,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewTeamInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewTeam({
      ...newTeam,
      [e.target.name]: e.target.value,
    });
  };

  const handleRoleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRole({
      ...role,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewRoleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewRole({
      ...newRole,
      [e.target.name]: e.target.value,
    });
  };

  const handleJobRankInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setJobRank({
      ...jobRank,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewJobRankInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setNewJobRank({
      ...newJobRank,
      [e.target.name]: e.target.value,
    });
  };

  const handleNewSchoolInputChange = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setNewSchool({
      ...newSchool,
      [e.target.name]: e.target.value,
    });
  };

  const renderTable = () => {
    if (formData === null) {
      return null;
    }
    switch (selectedCategoryId) {
      case "authority":
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="authName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="authName"
                type="text"
                name="authName"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한 이름을 적어주세요."
                required
                value={newAuthData.authName}
                onChange={handleNewAuthDataInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="authDescription"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="authDescription"
                name="authDescription"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한에 대한 메모가 필요하시면 적어주세요."
                value={newAuthData.authDescription}
                onChange={handleNewAuthDataInputChange}
                required
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="authLevel"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                권한 등급
              </label>
              <input
                type="number"
                id="authLevel"
                name="authLevel"
                value={newAuthData.authLevel}
                onChange={handleNewAuthDataInputChange}
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한 등급을 설정해주세요 1 : 어드민 레벨 2 : 운영진 레벨 3 이하부터는 사용자 등급 차등 권한"
                required
              />
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="authName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="authName"
                type="text"
                name="authName"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한 이름을 적어주세요."
                required
                value={auth.authName}
                onChange={handleAuthInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="authDescription"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="authDescription"
                name="authDescription"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한에 대한 메모가 필요하시면 적어주세요."
                value={auth.authDescription}
                onChange={handleAuthInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="authLevel"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                권한 등급
              </label>
              <input
                type="number"
                id="authLevel"
                name="authLevel"
                value={auth.authLevel}
                onChange={handleAuthInputChange}
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="권한 등급을 설정해주세요 1 : 어드민 레벨 2 : 운영진 레벨 3 이하부터는 사용자 등급 차등 권한"
                required
              />
            </div>
          </div>
        );
      case "department":
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="name"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                사업부명
              </label>
              <input
                id="name"
                type="text"
                name="name"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="사업부 이름을 적어주세요."
                required
                value={newDeptData.name}
                onChange={handleNewDeptInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="description"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="사업부에 대한 메모가 필요하시면 적어주세요."
                value={newDeptData.description}
                onChange={handleNewDeptInputChange}
                required
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="managerName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                관리자 명
              </label>
              <div className="flex items-center gap-1">
                <input
                  type="text"
                  id="managerName"
                  name="managerName"
                  value={newDeptData.managerName}
                  onChange={handleNewDeptInputChange}
                  className="block rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                  disabled
                />
                <Button
                  className="w-32"
                  onClick={() => setOpenDeptMemberCreateModal(true)}
                >
                  관리자 선택
                </Button>
                <MemberSelectionModal
                  title="사업부 관리자 선택"
                  isOpen={openDeptMemberCreateModal}
                  toggleDrawer={() => {
                    setOpenDeptMemberCreateModal(false);
                  }}
                  size={"4xl"}
                  onClose={onCloseDeptMemberSelect}
                />
              </div>
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="name"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                사업부명
              </label>
              <input
                id="name"
                type="text"
                name="name"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="사업부 이름을 적어주세요."
                required
                value={dept.name}
                onChange={handleDeptInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="description"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="사업부에 대한 메모가 필요하시면 적어주세요."
                value={dept.description}
                onChange={handleDeptInputChange}
                required
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="managerName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                관리자 명
              </label>
              <div className="flex items-center gap-1">
                <input
                  type="text"
                  id="managerName"
                  name="managerName"
                  value={dept.managerName}
                  onChange={handleDeptInputChange}
                  className="block rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                  disabled
                />
                <Button
                  className="w-32"
                  onClick={() => setOpenDeptMemberUpdateModal(true)}
                >
                  관리자 변경
                </Button>
                <MemberSelectionModal
                  isOpen={openDeptMemberUpdateModal}
                  toggleDrawer={() => {
                    setOpenDeptMemberUpdateModal(false);
                  }}
                  size={"4xl"}
                  title="사업부 관리자 변경"
                  onClose={onCloseDeptMemberSelect}
                />
              </div>
            </div>
          </div>
        );
      case "team":
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="name"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                부서명
              </label>
              <input
                id="name"
                type="text"
                name="name"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="부서 이름을 적어주세요."
                required
                value={newTeam.name}
                onChange={handleNewTeamInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="description"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="부서에 대한 메모가 필요하시면 적어주세요."
                value={newTeam.description}
                onChange={handleNewTeamInputChange}
              />
            </div>
            <div className="mb-5">
              {/* <label
                htmlFor="authLevel"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                소속 사업부
              </label> */}
              {/* <div className="flex items-center gap-1">
                <input
                  type="text"
                  id="departmentName"
                  name="departmentName"
                  value={newTeam.departmentName}
                  onChange={handleNewTeamInputChange}
                  className="block rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                  disabled
                />
                <Button
                  className="w-32"
                  onClick={() => setOpenTeamDeptCreateModal(true)}
                >
                  사업부 선택
                </Button>
                <DepartmentSelectionModal
                  title="사업부 관리자 선택"
                  isOpen={openTeamDeptCreateModal}
                  toggleDrawer={() => {
                    setOpenTeamDeptCreateModal(false);
                  }}
                  size={"4xl"}
                  onClose={onCloseTeamDeptSelect}
                />
              </div> */}
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="name"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                부서명
              </label>
              <input
                id="name"
                type="text"
                name="name"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="부서 이름을 적어주세요."
                required
                value={team.name}
                onChange={handleTeamInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                상세 설명
              </label>
              <input
                type="text"
                id="description"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="부서에 대한 메모가 필요하시면 적어주세요."
                value={team.description}
                onChange={handleTeamInputChange}
              />
            </div>
            <div className="mb-5">
              <div className="mb-5">
                {/* <label
                  htmlFor="authLevel"
                  className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
                >
                  소속 사업부
                </label>
                <div className="flex items-center gap-1">
                  <input
                    type="text"
                    id="departmentName"
                    name="departmentName"
                    value={team.departmentName}
                    onChange={handleTeamInputChange}
                    className="block rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                    disabled
                  />
                  <Button
                    className="w-32"
                    onClick={() => setOpenTeamDeptUpdateModal(true)}
                  >
                    사업부 선택
                  </Button>
                  <DepartmentSelectionModal
                    title="사업부 관리자 선택"
                    isOpen={openTeamDeptUpdateModal}
                    toggleDrawer={() => {
                      setOpenTeamDeptUpdateModal(false);
                    }}
                    size={"4xl"}
                    onClose={onCloseTeamDeptSelect}
                  />
                </div> */}
              </div>
            </div>
          </div>
        );
      case "role":
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="description"
                type="text"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="담당 업무 설명을 적어주세요."
                required
                value={newRole.description}
                onChange={handleNewRoleInputChange}
              />
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="description"
                type="text"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="담당 업무 설명을 적어주세요."
                required
                value={role.description}
                onChange={handleRoleInputChange}
              />
            </div>
          </div>
        );

      case "school":
        // if (isSchoolArray(formData)) {
        //   return (
        //     <SchoolTable
        //       schools={formData}
        //       handleSelected={handleSelected}
        //       isEditing={isEditing}
        //       selectedIndex={selectedIndex}
        //       isAdding={isAdding}
        //       newSchool={newData}
        //       handleNewSchoolChange={handleChange}
        //       handleEditSchoolChange={handleEditChange}
        //     />
        //   );
        // }
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="schoolName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                학교 이름
              </label>
              <input
                id="schoolName"
                type="text"
                name="schoolName"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="학교 이름을 적어주세요."
                required
                value={newSchool.schoolName}
                onChange={handleNewSchoolInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="country"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                국가
              </label>
              <input
                id="country"
                type="text"
                name="country"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="학교와 관련된 국가를 입력해주세요."
                required
                value={newSchool.country}
                onChange={handleNewSchoolInputChange}
              />
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="schoolName"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                학교 이름
              </label>
              <input
                id="schoolName"
                type="text"
                name="schoolName"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="학교 이름을 적어주세요."
                required
                value={school.schoolName}
                onChange={handleSchoolInputChange}
              />
            </div>
            <div className="mb-5">
              <label
                htmlFor="country"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                국가
              </label>
              <input
                id="country"
                type="text"
                name="country"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="학교와 관련된 국가를 입력해주세요."
                required
                value={school.country}
                onChange={handleSchoolInputChange}
              />
            </div>
          </div>
        );
      case "lab":
        if (isLabArray(formData)) {
          return (
            <LabTable
              labs={formData}
              schools={schoolData}
              selectedTypeId={selectedTypeId}
              handleSelected={handleSelected}
              isEditing={isEditing}
              selectedIndex={selectedIndex}
              isAdding={isAdding}
              newLab={newData}
              handleNewLabChange={handleChange}
              handleEditLabChange={handleEditChange}
            />
          );
        }

      case "jobrank":
        return isAdding ? (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="description"
                type="text"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="직급을 적어주세요."
                required
                value={newJobRank.description}
                onChange={handleNewJobRankInputChange}
              />
            </div>
          </div>
        ) : (
          <div className="mx-auto w-1/2">
            <div className="mb-5">
              <label
                htmlFor="description"
                className="mb-2 block text-start text-sm font-medium text-gray-900 dark:text-white"
              >
                표기
              </label>
              <input
                id="description"
                type="text"
                name="description"
                className="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-blue-500 focus:ring-blue-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-blue-500 dark:focus:ring-blue-500"
                placeholder="직급을 적어주세요."
                required
                value={jobRank.description}
                onChange={handleJobRankInputChange}
              />
            </div>
          </div>
        );
      default:
        return null;
    }
  };

  const renderList = () => {
    switch (selectedCategoryId) {
      case "authority":
        return (
          <>
            {authorityData.map((data, index) => (
              <div
                key={index}
                onClick={() => handleColumn(data.id, selectedCategoryId)}
                // onClick={() => handleType(index, data.id)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.id ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.authName}
              </div>
            ))}
          </>
        );
      case "department":
        return (
          <>
            {departmentData.map((data, index) => (
              <div
                key={index}
                onClick={() =>
                  handleColumn(data.departmentId, selectedCategoryId)
                }
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.departmentId ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.name}
              </div>
            ))}
          </>
        );
      case "team":
        return (
          <>
            {teamData.map((data, index) => (
              <div
                key={data.teamId}
                onClick={() => handleColumn(data.teamId, selectedCategoryId)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.teamId ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.name}
              </div>
            ))}
          </>
        );
      case "role":
        return (
          <>
            {roleData.map((data, index) => (
              <div
                key={index}
                onClick={() => handleColumn(data.roleId, selectedCategoryId)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.roleId ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.description}
              </div>
            ))}
          </>
        );
      case "school":
        return (
          <>
            {schoolData.map((data, index) => (
              <div
                key={data.id}
                onClick={() => handleColumn(data.id, selectedCategoryId)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.id ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.schoolName}
              </div>
            ))}
          </>
        );
      case "lab":
        return (
          <>
            <div
              onClick={() => handleType(-1, -1)}
              className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedTypeId === -1 ? "bg-slate-300" : "hover:from-neutral-100"} `}
            >
              전체
            </div>
            {schoolData.map((data, index) => (
              <div
                key={index}
                onClick={() => handleType(index, data.id)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedTypeId === index ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.schoolName}
              </div>
            ))}
          </>
        );

      case "jobrank":
        return (
          <>
            {jobRankData.map((data, index) => (
              <div
                key={data.id}
                onClick={() => handleColumn(data.id, selectedCategoryId)}
                // onClick={() => handleType(index, data.id)}
                className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedColumnId === data.id ? "bg-slate-300" : "hover:from-neutral-100"} `}
              >
                {data.description}
              </div>
            ))}
          </>
        );
    }
  };
  return (
    <>
      <div className="flex min-h-screen flex-col space-y-5 bg-white p-5">
        <h3 className="text-xl">기본 정보 및 운영 정보 관리</h3>
        <div className="flex flex-col space-y-4 text-center">
          <div className="flex items-center bg-gray-100 px-4 py-3 text-base uppercase text-gray-700 dark:bg-gray-700 dark:text-gray-400">
            <div className="w-1/12 font-semibold">항목 값</div>
            <div className="w-2/12 font-semibold">구분</div>
            <div className="w-9/12 font-semibold">상세 내용</div>
          </div>

          <div className="flex px-4">
            <div className="flex w-1/12 flex-col">
              {category.map((data, index) => (
                <div
                  key={index}
                  onClick={() => handleCagegory(data)}
                  className={`flex min-h-12 cursor-pointer items-center justify-center text-xs font-semibold hover:bg-slate-200 ${selectedCategoryId === data.id ? "bg-slate-300" : "hover:from-neutral-100"} `}
                >
                  {data.name}
                </div>
              ))}
            </div>
            <div className="flex h-screen w-2/12 flex-col overflow-y-auto">
              {renderList()}
            </div>
            <div className="w-9/12">
              <div className="relative overflow-x-auto">
                <div className="flex items-center justify-between">
                  <div className="flex flex-col space-y-5 p-5"></div>
                  <div className="flex flex-row gap-1">
                    {/* 체크박스를 누른 상태에서는 추가를 못하게 막는다. */}

                    {isEditing || isAdding ? (
                      <>
                        <Button
                          className="h-full bg-primary text-center"
                          onClick={handleSave}
                        >
                          저장
                        </Button>
                        <Button
                          className="h-full bg-primary text-center"
                          onClick={() => {
                            setIsEditing(false);
                            setIsAdding(false);
                          }}
                        >
                          취소
                        </Button>
                      </>
                    ) : (
                      <>
                        <Button
                          className="h-full bg-primary text-center"
                          onClick={handleAdd}
                        >
                          추가
                        </Button>
                        <Button
                          className="h-full bg-primary text-center"
                          onClick={handleEdit}
                        >
                          수정
                        </Button>
                      </>
                    )}
                    <Button
                      className="mr-2 h-full bg-primary text-center"
                      onClick={handleDelete}
                    >
                      제거
                    </Button>
                  </div>
                </div>
                <div className="overflow-x-auto">{renderTable()}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

const SchoolTable = ({
  schools,
  handleSelected,
  isEditing,
  selectedIndex,
  isAdding,
  newSchool,
  handleNewSchoolChange,
  handleEditSchoolChange,
}: SchoolTableProps) => {
  return (
    <Table
      theme={{
        root: {
          wrapper: "static",
        },
      }}
      className="w-full text-center text-sm text-gray-500"
    >
      <Table.Head className="bg-gray-50 text-base font-bold uppercase text-gray-700 ">
        <Table.HeadCell scope="col" className="w-[40px] p-2">
          <div className="flex items-center justify-center">
            <HiCheck />
          </div>
        </Table.HeadCell>
        {schoolHeadCell.map((data, index) => (
          <Table.HeadCell key={index} scope="col" className="p-2">
            {data}
          </Table.HeadCell>
        ))}
      </Table.Head>
      <Table.Body>
        {schools.map((school, index) => (
          <Table.Row key={index} className="border-b hover:bg-gray-100 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div key={index} className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="text-bclue-600 h-4 w-4 rounded border-gray-300 bg-gray-100 focus:ring-2 focus:ring-blue-500"
                  onChange={() => handleSelected(school.id, school)}
                  checked={selectedIndex === school.id}
                />
              </div>
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900">
              {school.id}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900">
              {isEditing && selectedIndex === school.id ? (
                <TextInput
                  type="text"
                  name="schoolName"
                  defaultValue={school.schoolName}
                  onChange={handleEditSchoolChange}
                />
              ) : (
                school.schoolName
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              {isEditing && selectedIndex === school.id ? (
                <TextInput
                  type="text"
                  name="country"
                  defaultValue={school.country}
                  onChange={handleEditSchoolChange}
                />
              ) : (
                school.country
              )}
            </Table.Cell>
          </Table.Row>
        ))}
        {isAdding && newSchool && (
          <Table.Row className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                  disabled
                />
              </div>
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              New
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="schoolName"
                value={newSchool.schoolName || ""}
                onChange={handleNewSchoolChange}
              />
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="country"
                value={newSchool.country || ""}
                onChange={handleNewSchoolChange}
              />
            </Table.Cell>
          </Table.Row>
        )}
      </Table.Body>
    </Table>
  );
};

const LabTable = ({
  labs,
  schools,
  handleSelected,
  selectedTypeId,
  isEditing,
  selectedIndex,
  isAdding,
  newLab,
  handleNewLabChange,
  handleEditLabChange,
}: LabTableProps) => {
  return (
    <Table
      theme={{
        root: {
          wrapper: "static",
        },
      }}
      className="w-full table-fixed text-center text-sm text-gray-500 "
    >
      <Table.Head className="bg-gray-50 text-base font-bold uppercase text-gray-700 ">
        <Table.HeadCell scope="col" className="w-[30px] p-2">
          <div className="flex items-center justify-center">
            <HiCheck />
          </div>
        </Table.HeadCell>
        {labHeadCell.map((data, index) => (
          <Table.HeadCell key={index} scope="col" className="p-1">
            {data}
          </Table.HeadCell>
        ))}
      </Table.Head>
      <Table.Body>
        {labs.map((lab, index) => (
          <Table.Row key={index} className="border-b hover:bg-gray-100 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                  onChange={() => handleSelected(lab.id, lab)}
                  checked={selectedIndex === lab.id}
                />
              </div>
            </Table.Cell>
            {/* <Table.Cell className="whitespace-normal p-1 font-medium text-gray-900 dark:text-white">
              {lab.id}
            </Table.Cell> */}
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <Select
                  name="schoolId"
                  onChange={handleEditLabChange}
                  defaultValue={lab.schoolId}
                  required
                >
                  {schools.map((school, index) => (
                    <option key={index} value={school.id}>
                      {school.schoolName}
                    </option>
                  ))}
                </Select>
              ) : (
                lab.schoolName
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="major"
                  defaultValue={lab.major}
                  onChange={handleEditLabChange}
                />
              ) : (
                lab.major
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="labName"
                  defaultValue={lab.labName}
                  onChange={handleEditLabChange}
                />
              ) : (
                lab.labName
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="labProfessor"
                  defaultValue={lab.labProfessor}
                  onChange={handleEditLabChange}
                />
              ) : (
                lab.labProfessor
              )}
            </Table.Cell>
            <Table.Cell className=" p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="researchDescription"
                  defaultValue={lab.researchDescription}
                  onChange={handleEditLabChange}
                />
              ) : (
                <Popover
                  trigger="hover"
                  placement="top"
                  arrow={false}
                  content={
                    <div className="max-w-72 whitespace-pre-line">
                      {lab.researchDescription}
                    </div>
                  }
                >
                  <div className="truncate hover:cursor-pointer">
                    {lab.researchDescription}
                  </div>
                </Popover>
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="researchType"
                  defaultValue={lab.researchType}
                  onChange={handleEditLabChange}
                />
              ) : (
                lab.researchType
              )}
            </Table.Cell>
            <Table.Cell className=" p-2 font-medium text-gray-900 dark:text-white">
              {isEditing && selectedIndex === lab.id ? (
                <TextInput
                  type="text"
                  name="researchResult"
                  defaultValue={lab.researchResult}
                  onChange={handleEditLabChange}
                />
              ) : (
                <Popover
                  trigger="hover"
                  placement="top"
                  arrow={false}
                  content={
                    <div className="max-w-72 whitespace-pre-line">
                      {lab.researchResult}
                    </div>
                  }
                >
                  <div className="truncate hover:cursor-pointer">
                    {lab.researchResult}
                  </div>
                </Popover>
              )}
            </Table.Cell>
          </Table.Row>
        ))}
        {isAdding && newLab && (
          <Table.Row className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                  disabled
                />
              </div>
            </Table.Cell>
            {/* <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              New
            </Table.Cell> */}
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              {selectedTypeId === -1 ? (
                <Select
                  name="schoolId"
                  onChange={handleNewLabChange}
                  value={newLab.schoolId}
                >
                  {schools.map((school, index) => (
                    <option key={index} value={school.id}>
                      {school.schoolName}
                    </option>
                  ))}
                </Select>
              ) : (
                schools[selectedTypeId].schoolName
              )}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="major"
                value={newLab.major || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="labName"
                value={newLab.labName || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="labProfessor"
                value={newLab.labProfessor || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
            <Table.Cell className=" p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="researchDescription"
                value={newLab.researchDescription || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900  dark:text-white">
              <TextInput
                type="text"
                name="researchType"
                value={newLab.researchType || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
            <Table.Cell className="p-2 font-medium text-gray-900 dark:text-white">
              <TextInput
                type="text"
                name="researchResult"
                value={newLab.researchResult || ""}
                onChange={handleNewLabChange}
              />
            </Table.Cell>
          </Table.Row>
        )}
      </Table.Body>
    </Table>
  );
};

const JobRankTable = ({
  jobRanks,
  handleSelected,
  isEditing,
  selectedIndex,
  isAdding,
  newJobRank,
  handleNewJobRankChange,
  handleEditJobRankChange,
}: JobRankTableProps) => {
  return (
    <Table
      theme={{
        root: {
          wrapper: "static",
        },
      }}
      className="w-full text-center text-sm text-gray-500"
    >
      <Table.Head className="bg-gray-50 text-base font-bold uppercase text-gray-700 ">
        <Table.HeadCell scope="col" className="w-[40px] p-2">
          <div className="flex items-center justify-center">
            <HiCheck />
          </div>
        </Table.HeadCell>
        {jobRankHeadCell.map((data, index) => (
          <Table.HeadCell key={index} scope="col" className="p-2">
            {data}
          </Table.HeadCell>
        ))}
      </Table.Head>
      <Table.Body>
        {jobRanks.map((jobRank, index) => (
          <Table.Row key={index} className="border-b hover:bg-gray-100 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div key={index} className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                  onChange={() => handleSelected(jobRank.id, jobRank)}
                  checked={selectedIndex === jobRank.id}
                />
              </div>
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 ">
              {jobRank.id}
            </Table.Cell>
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 ">
              {isEditing && selectedIndex === jobRank.id ? (
                <TextInput
                  type="text"
                  name="description"
                  defaultValue={jobRank.description}
                  onChange={handleEditJobRankChange}
                />
              ) : (
                jobRank.description
              )}
            </Table.Cell>
          </Table.Row>
        ))}
        {isAdding && newJobRank && (
          <Table.Row className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700 ">
            <Table.Cell className="w-4 whitespace-nowrap px-2 py-1">
              <div className="flex items-center justify-center ">
                <input
                  type="radio"
                  name="row"
                  className="h-4 w-4 rounded border-gray-300 bg-gray-100 text-blue-600 focus:ring-2 focus:ring-blue-500"
                  disabled
                />
              </div>
            </Table.Cell>
            {/* <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white">
              New
            </Table.Cell> */}
            <Table.Cell className="whitespace-normal p-2 font-medium text-gray-900 hover:cursor-pointer dark:text-white">
              <TextInput
                type="text"
                name="description"
                value={newJobRank.description || ""}
                onChange={handleNewJobRankChange}
              />
            </Table.Cell>
          </Table.Row>
        )}
      </Table.Body>
    </Table>
  );
};

export default ChartsManageTable;
