import { useEffect, useState } from 'react';
import { HiPlus, HiPencil, HiStar, HiOutlineStar, HiOutlinePencil, HiOutlineMinusCircle } from 'react-icons/hi';
import { useLocalAxios } from "@/app/api/axios";
import CustomAddButton from "@/components/common/CustomAddButton";
import { Card } from "flowbite-react";
import { Specialization } from "@/types/talent/ProfileSpecialization";
import SpecializationCreateModal from "@/components/profile/register/SpecializationCreateModal";

interface ProfessionInfoDivProps {
  profileId: number;
}

export default function ProfessionInfoDiv({ profileId }: ProfessionInfoDivProps) {
  const localAxios = useLocalAxios();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [specializations, setSpecializations] = useState<Specialization[]>([]);
  const [selectedSpecialization, setSelectedSpecialization] = useState<Specialization>();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const response = await localAxios.get<Specialization[]>(`/profile/${profileId}/specialization`);
    response.data.sort((a, b) => {
      if (a.isFavorite && !b.isFavorite) return -1;
      else if (!a.isFavorite && b.isFavorite) return 1;
      else return new Date(b.createdAt).valueOf() - new Date(a.createdAt).valueOf();
    });

    setSpecializations(response.data);
  };

  const openModal = (specializationId?: number) => {
    if (specializationId) {
      setSelectedSpecialization(specializations.find(x => x.id === specializationId));
    } else {
      setSelectedSpecialization(undefined);
    }

    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedSpecialization(undefined);
    setIsModalOpen(false);
  };

  const onSubmit = (specialization: Specialization) => {
    fetchData();
  };

  const updateFavorite = async (index: number) => {
    const response = await localAxios.put<Specialization>(`/profile/${profileId}/specialization/${specializations[index].id}/favorite`, !specializations[index].isFavorite);
    fetchData();
  };

  const deleteSpecialization = async (index: number) => {
    const response = await localAxios.delete(`/profile/${profileId}/specialization/${specializations[index].id}`);
    const newSpecializations = [...specializations];
    newSpecializations.splice(index, 1);
    setSpecializations(newSpecializations);
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return `${date.getFullYear()}. ${date.getMonth() + 1}. ${date.getDate()}`;
  };

  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">special</p>
        </div>

        <CustomAddButton onClick={() => { openModal(); }} />
      </div>
      <div className="flex flex-col gap-4">
        {
          specializations.length <= 0 &&
          <div className="font-bold mx-auto pt-4 pb-2 text-lg">등록된 special가 존재하지 않습니다.</div>
        }
        {
          specializations.map((specialization, index) => (
            <Card key={specialization.id}>
              <div className="flex justify-between items-center mb-2">
                <button onClick={() => { updateFavorite(index); }}>
                  {
                    specialization.isFavorite
                      ? <HiStar className="text-3xl text-yellow-300" />
                      : <HiOutlineStar className="text-3xl" />
                  }
                </button>

                <div className="flex items-center gap-2">
                  <button onClick={() => {
                    if (confirm("정말 삭제하시겠습니까?")) {
                      deleteSpecialization(index);
                    }
                  }}>
                    <HiOutlineMinusCircle className="text-3xl text-red-500" />
                  </button>
                  <button onClick={() => {
                    openModal(specialization.id);
                  }}>
                    <HiOutlinePencil className="text-3xl" />
                  </button>
                </div>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="text-lg font-bold whitespace-nowrap">special</span>
                <div className="text-lg">{specialization.specialPoint}</div>
              </div>
              <div className="px-10 flex gap-4 items-start">
                <span className="text-lg font-bold whitespace-nowrap">상세내용</span>
                <div className="text-lg whitespace-pre-wrap">{specialization.description}</div>
              </div>
              <div className="w-full text-end mt-4 whitespace-nowrap">
                * 등록정보 : {specialization.memberDepartment} {specialization.memberName} 프로
                ({formatDate(specialization.createdAt)})
              </div>
            </Card>
          ))
        }
      </div>

      {isModalOpen && <SpecializationCreateModal profileId={profileId} selected={selectedSpecialization} onSubmit={onSubmit} onClose={closeModal} />}
    </div>
  );
};