import { useEffect, useState, useCallback } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { EmploymentHistory } from "@/types/talent/ProfileEmploymentHistory";
import CustomAddButton from "@/components/common/CustomAddButton";
import { Card } from "flowbite-react";
import {
  HiMinus,
  HiMinusCircle,
  HiOutlineDocumentRemove,
  HiOutlineMinusCircle,
  HiOutlinePencil,
  HiOutlineStar,
  HiStar
} from "react-icons/hi";
import EmploymentHistoryCreateModal from "@/components/profile/register/EmploymentHistoryCreateModal";

const EmploymentHistoryTypes = [
  { label: "활용도 검토", value: "USAGE_REVIEW" },
  { label: "면접", value: "INTERVIEW" },
  { label: "APPROVE_E", value: "APPROVE_E" },
  { label: "처우 협의", value: "NEGOTIATION" },
  { label: "처우 결렬", value: "NEGOTIATION_DENIED" },
  { label: "입사 포기", value: "EMPLOY_ABANDON" },
  { label: "입사 대기", value: "EMPLOY_WAITING" },
  { label: "입사", value: "EMPLOYED" }
];

const InterviewTypes = [
  { label: "대면", value: "FACE" },
  { label: "비대면", value: "NO_FACE" }
];

const InterviewResultTypes = [
  { label: "A+", value: "A_PLUS" },
  { label: "A", value: "A" },
  { label: "B+", value: "B_PLUS" },
  { label: "B", value: "B" },
  { label: "C+", value: "C_PLUS" },
  { label: "C", value: "C" },
  { label: "D", value: "D" },
  { label: "F", value: "F" }
];

interface RecruitInfoDivProps {
  profileId: number;
}

export default function RecruitInfoDiv({ profileId }: RecruitInfoDivProps) {
  const localAxios = useLocalAxios();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [employmentHistories, setEmploymentHistories] = useState<EmploymentHistory[]>([]);
  const [selectedEmploymentHistory, setSelectedEmploymentHistory] = useState<EmploymentHistory>();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const response = await localAxios.get<EmploymentHistory[]>(`/profile/${profileId}/employment-history`);
    response.data.sort((a, b) => {
      if (a.isFavorite && !b.isFavorite) return -1;
      else if (!a.isFavorite && b.isFavorite) return 1;
      else return new Date(b.createdAt).valueOf() - new Date(a.createdAt).valueOf();
    });

    setEmploymentHistories(response.data);
  };

  const openModal = (employmentHistoryType?: string, employmentHistoryId?: number) => {
    if (employmentHistoryId) {
      setSelectedEmploymentHistory(employmentHistories.find(x => x.id === employmentHistoryId && x.type === employmentHistoryType))
    } else {
      setSelectedEmploymentHistory(undefined);
    }

    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedEmploymentHistory(undefined);
    setIsModalOpen(false);
  };

  const onSubmit = (employmentHistory: EmploymentHistory) => {
    fetchData();
  };

  const updateFavorite = async (index: number) => {
    if (employmentHistories[index].type === "INTERVIEW") {
      const response = await localAxios.put<EmploymentHistory>(`/profile/${profileId}/interview/${employmentHistories[index].id}/favorite`, !employmentHistories[index].isFavorite);
      fetchData();
    } else {
      const response = await localAxios.put<EmploymentHistory>(`/profile/${profileId}/employment-history/${employmentHistories[index].id}/favorite`, !employmentHistories[index].isFavorite);
      fetchData();
    }
  };

  const deleteEmploymentHistory = async (index: number) => {
    if (employmentHistories[index].type === "INTERVIEW") {
      await localAxios.delete(`/profile/${profileId}/interview/${employmentHistories[index].id}`);
      const newEmploymentHistories = [...employmentHistories];
      newEmploymentHistories.splice(index, 1);
      setEmploymentHistories(newEmploymentHistories);
    } else {
      await localAxios.delete(`/profile/${profileId}/employment-history/${employmentHistories[index].id}`);
      const newEmploymentHistories = [...employmentHistories];
      newEmploymentHistories.splice(index, 1);
      setEmploymentHistories(newEmploymentHistories);
    }
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()}`;
  };

  const formatDateWithDay = (dateString?: string) => {
    if (dateString) {
      const date = new Date(dateString);
      return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()} (${formatDay(date.getDay())})`;
    }
  };

  const formatDay = (day: number) => {
    switch (day) {
      case 0:
        return "일";
      case 1:
        return "월";
      case 2:
        return "화";
      case 3:
        return "수";
      case 4:
        return "목";
      case 5:
        return "금";
      case 6:
        return "토";
      default:
        return "Invalid day";
    }
  };

  const formatDateLabel = (type: string) => {
    switch (type) {
      case "USAGE_REVIEW": return "검토일";
      case "INTERVIEW": return "면접일";
      case "APPROVE_E": return "승인일";
      case "EMPLOYED": return "입사일";
      default: return "입사예정일";
    }
  }
  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">채용전형</p>
        </div>

        <CustomAddButton onClick={() => {
          openModal();
        }} />
      </div>
      <div className="flex flex-col gap-4">
        {
          employmentHistories.length <= 0 &&
          <div className="font-bold mx-auto pt-4 pb-2 text-lg">등록된 채용전형 기록이 존재하지 않습니다.</div>
        }
        {
          employmentHistories.map((employmentHistory, index) => (
            <Card key={employmentHistory.type + employmentHistory.id}>
              <div className="flex justify-between items-center mb-2">
                <button onClick={() => {
                  updateFavorite(index);
                }}>
                  {
                    employmentHistory.isFavorite
                      ? <HiStar className="text-3xl text-yellow-300" />
                      : <HiOutlineStar className="text-3xl" />
                  }
                </button>

                <div className="flex items-center gap-2">
                  <button onClick={() => {
                    if (confirm("정말 삭제하시겠습니까?")) {
                      deleteEmploymentHistory(index);
                    }
                  }}>
                    <HiOutlineMinusCircle className="text-3xl text-red-500" />
                  </button>
                  <button onClick={() => {
                    openModal(employmentHistory.type, employmentHistory.id);
                  }}>
                    <HiOutlinePencil className="text-3xl" />
                  </button>
                </div>
              </div>
              <div className="px-10 flex gap-4 items-start text-lg">
                <span className="font-bold whitespace-nowrap">채용전형</span>
                <div>{EmploymentHistoryTypes.find(x => x.value == employmentHistory.type)?.label}</div>
              </div>
              <div className="px-10 flex gap-4 items-start text-lg">
                {
                  ["INTERVIEW", "APPROVE_E", "NEGOTIATION"].includes(employmentHistory.type) &&
                  <>
                    <span className="font-bold whitespace-nowrap">단계</span>
                    <div>{employmentHistory.step}</div>
                  </>
                }
                <span className="font-bold">{formatDateLabel(employmentHistory.type)}</span>
                <div>{employmentHistory.type == "INTERVIEW" ? formatDateWithDay(employmentHistory.meetDate) : formatDateWithDay(employmentHistory.targetEmployDate)}</div>
                {
                  !["INTERVIEW", "APPROVE_E", "USAGE_REVIEW"].includes(employmentHistory.type) &&
                  <>
                    <span className="font-bold whitespace-nowrap">직급</span>
                    <div>{employmentHistory.targetJobRankName}</div>
                  </>
                }
                {
                  ["USAGE_REVIEW", "APPROVE_E"].includes(employmentHistory.type) &&
                  <>
                    <span className="font-bold whitespace-nowrap">결과</span>
                    <div>{employmentHistory.result}</div>
                  </>
                }
                {
                  employmentHistory.type == "INTERVIEW" &&
                  <>
                    <span className="font-bold whitespace-nowrap">면접유형</span>
                    <div>{InterviewTypes.find(x => x.value == employmentHistory.interviewType)?.label}</div>
                    <span className="font-bold whitespace-nowrap">면접장소</span>
                    <div>{employmentHistory.place}</div>
                  </>
                }
                {
                  employmentHistory.type == "USAGE_REVIEW" &&
                  <>
                    <span className="font-bold whitespace-nowrap">검토자</span>
                    <div>{employmentHistory.executiveName}</div>
                  </>
                }
              </div>
              {
                employmentHistory.type == "INTERVIEW" && employmentHistory.interviewResults && employmentHistory.interviewResults.length > 0 &&
                <>
                  <div className="font-bold px-10 text-lg">면접내용</div>
                  <ul>
                    {
                      employmentHistory.interviewResults?.map((result, index) => (
                        <li key={index} className="mx-12 flex gap-4 items-center">
                          <span className="font-bold">{result.executiveName}</span>
                          <span
                            className="text-sm font-bold">{InterviewResultTypes.find(x => x.value == result.interviewResultType)?.label}</span>
                        </li>
                      ))
                    }
                  </ul>
                </>
              }
              {
                employmentHistory.description.length > 0 &&
                <>
                  <div className="font-bold px-10">참고사항</div>
                  <div className="mx-10 -mt-1 border-2 rounded-md border-gray-400 p-4">
                    {employmentHistory.description}
                  </div>
                </>
              }

              <div className="w-full text-end mt-4">
                * 등록정보 : {employmentHistory.memberDepartment} {employmentHistory.memberName} 프로
                ({formatDate(employmentHistory.createdAt)})
              </div>
            </Card>
          ))
        }
      </div>

      {isModalOpen && <EmploymentHistoryCreateModal profileId={profileId} selected={selectedEmploymentHistory} onSubmit={onSubmit} onClose={closeModal} />}
    </div>
  );
}
