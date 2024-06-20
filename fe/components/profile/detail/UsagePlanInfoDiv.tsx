import { useEffect, useState, useCallback } from 'react';
import { HiPlus, HiPencil, HiStar, HiOutlineStar, HiOutlinePencil, HiOutlineMinusCircle } from 'react-icons/hi';
import { useLocalAxios } from '@/app/api/axios';
import { UsagePlan } from "@/types/talent/ProfileUsagePlan";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
import { Button, Card } from "flowbite-react";
import CustomAddButton from "@/components/common/CustomAddButton";
import UsagePlanCreateModal from "@/components/profile/register/UsagePlanCreateModal";

interface UsagePlanInfoDivProps {
  profileId: number;
}

const UsagePlanInfoDiv = ({ profileId }: UsagePlanInfoDivProps) => {
  const localAxios = useLocalAxios();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [usagePlans, setUsagePlans] = useState<UsagePlan[]>([]);
  const [selectedUsagePlan, setSelectedUsagePlan] = useState<UsagePlan>();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const response = await localAxios.get<UsagePlan[]>(`/profile/${profileId}/usage-plan`);
    response.data.sort((a, b) => {
      if (a.isFavorite && !b.isFavorite) return -1;
      else if (!a.isFavorite && b.isFavorite) return 1;
      else return new Date(b.createdAt).valueOf() - new Date(a.createdAt).valueOf();
    });

    setUsagePlans(response.data);
  };

  const updateFavorite = async (index: number) => {
    const response = await localAxios.put<UsagePlan>(`/profile/${profileId}/usage-plan/${usagePlans[index].id}/favorite`, !usagePlans[index].isFavorite);
    fetchData();
  };

  const deleteUsagePlan = async (index: number) => {
    await localAxios.delete(`/profile/${profileId}/usage-plan/${usagePlans[index].id}`);
    const newUsagePlans = [...usagePlans];
    newUsagePlans.splice(index, 1);
    setUsagePlans(newUsagePlans);
  }

  const openModal = (usagePlanId?: number) => {
    if (usagePlanId) {
      setSelectedUsagePlan(usagePlans.find(x => x.id === usagePlanId));
    } else {
      setSelectedUsagePlan(undefined);
    }

    setIsModalOpen(true);
  }

  const closeModal = () => {
    setSelectedUsagePlan(undefined);
    setIsModalOpen(false);
  }

  const onSubmit = (usagePlan: UsagePlan) => {
    fetchData();
  }
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()}`;
  };

  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">활용계획</p>
        </div>

        <CustomAddButton onClick={() => { openModal(); }} />
      </div>
      <div className="flex flex-col gap-4">
        {
          usagePlans.length <= 0 &&
          <div className="font-bold mx-auto pt-4 pb-2 text-lg">등록된 활용계획이 존재하지 않습니다.</div>
        }
        {
          usagePlans.map((usagePlan, index) => (
            <Card key={usagePlan.id}>
              <div className="flex justify-between items-center mb-2">
                <button onClick={() => {
                  updateFavorite(index);
                }}>
                  {
                    usagePlan.isFavorite
                      ? <HiStar className="text-3xl text-yellow-300" />
                      : <HiOutlineStar className="text-3xl" />
                  }
                </button>

                <div className="flex items-center gap-2">
                  <button onClick={() => {
                    if (confirm("정말 삭제하시겠습니까?")) {
                      deleteUsagePlan(index);
                    }
                  }}>
                    <HiOutlineMinusCircle className="text-3xl text-red-500" />
                  </button>
                  <button onClick={() => {
                    openModal(usagePlan.id);
                  }}>
                    <HiOutlinePencil className="text-3xl" />
                  </button>
                </div>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="text-lg font-bold whitespace-nowrap">활용부서</span>
                <span className="text-lg">{usagePlan.targetDepartmentName}</span>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="text-lg font-bold whitespace-nowrap">주요설명</span>
                <span className="text-lg">{usagePlan.mainDescription}</span>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="text-lg font-bold whitespace-nowrap">상세내용</span>
                <span className="text-lg whitespace-pre-wrap">{usagePlan.detailDescription}</span>
              </div>
              <div className="px-10 flex">
                <div className="flex gap-4 basis-1/2">
                  <span className="text-lg font-bold whitespace-nowrap">활용직급</span>
                  <span className="text-lg">{usagePlan.jobRank}</span>
                </div>
                <div className="flex gap-4 basis-1/2">
                  <span className="text-lg font-bold whitespace-nowrap">입사목표일</span>
                  <span className="text-lg">{formatDate(usagePlan.targetEmployDate)}</span>
                </div>
              </div>
              <div className="w-full text-end mt-4 whitespace-nowrap">
                * 등록정보 : {usagePlan.memberDepartment} {usagePlan.memberName} 프로
                ({formatDate(usagePlan.createdAt)})
              </div>
            </Card>
          ))
        }
      </div>

      {isModalOpen && <UsagePlanCreateModal profileId={profileId} selected={selectedUsagePlan} onSubmit={onSubmit}
        onClose={closeModal} />}
    </div>
  );
};

export default UsagePlanInfoDiv;
