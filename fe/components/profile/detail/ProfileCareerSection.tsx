import { HiOutlineMinusCircle, HiOutlinePencil, HiPencil, HiPlus } from "react-icons/hi";
import { useEffect, useMemo, useState } from "react";
import { Career } from "@/types/talent/ProfileDetailResponse";
import CustomAddButton from "@/components/common/CustomAddButton";
import CareerCreateModal from "@/components/profile/register/CareerCreateModal";
import { useLocalAxios } from "@/app/api/axios";

interface ProfileCareerSectionProps {
  profileId: number;
  careers: Career[];
  onUpdate: (career: Career) => void;
  reload: () => void;
}

export default function ProfileCareerSection(props: ProfileCareerSectionProps) {
  const localAxios = useLocalAxios();
  const [careers, setCareers] = useState<Career[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCareer, setSelectedCareer] = useState<Career>();

  useEffect(() => {
    setCareers(props.careers);
  }, [props.careers]);

  const totalExperience = useMemo(() => {
    const totalExperiencedMonth = props.careers.reduce((acc, cur) => {
      const periodMonth = cur.careerPeriodMonth + 1 || 0;
      return acc + periodMonth;
    }, 0);

    const years = Math.floor(totalExperiencedMonth / 12);
    const months = totalExperiencedMonth % 12;

    return {
      year: years,
      month: months
    };
  }, [props.careers]);

  const formatDate = (dateString?: string) => {
    if (dateString) {
      const date = new Date(dateString);
      return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}`;
    } else {
      return "현재";
    }
  };

  const formatEmploymentType = (employType: string) => {
    switch (employType) {
      case "FULL_TIME":
        return "정규직";
      case "CONTRACT":
        return "계약직";
    }
  };

  const openModal = (careerId?: number) => {
    if (careerId) {
      setSelectedCareer(careers.find(x => x.id === careerId));
    } else {
      setSelectedCareer(undefined);
    }

    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedCareer(undefined);
    setIsModalOpen(false);
  }

  const onSubmit = (career: Career) => {
    if (selectedCareer) {
      const newCareers = [...careers];
      newCareers[careers.findIndex(x => x.id === career.id)] = career;
      setCareers(newCareers);
    } else {
      setCareers(prev => [career, ...prev]);
    }
  };

  const deleteCareer = async (index: number) => {
    await localAxios.delete(`/profile/${props.profileId}/career/${careers[index].id}`);
    props.reload();
  }

  const formatCareerSummary = (career: Career) => {
    const firstLineElements = [];

    if (career.level && career.level.trim().length > 0) firstLineElements.push(career.level);
    if (career.dept && career.dept.trim().length > 0) firstLineElements.push(career.dept);
    if (career.role && career.role.trim().length > 0) firstLineElements.push(career.role);

    const secondLineElements = [];

    if (career.employType && career.employType.trim().length > 0) secondLineElements.push(formatEmploymentType(career.employType));
    if (career.country && career.country.trim().length > 0) secondLineElements.push(career.country);
    if (career.region && career.region.trim().length > 0) secondLineElements.push(career.region);

    return (
        <div className="mt-1 flex flex-col gap-1">
          <div className="flex items-center gap-2 text-lg">
            <span>{ firstLineElements.join(', ') }</span>
            { career.isManager && <div className="text-white text-sm font-bold p-2 bg-teal-400 size-7 flex items-center justify-center rounded-full">M</div> }
          </div>
          <div className="text-lg">{ secondLineElements.join(', ') }</div>
          <div className="flex gap-2">
            {
              career.keywords.map((keyword, index) => (
                  <div key={index} className="px-2 border-2 border-gray-200 rounded-sm">{keyword}</div>
              ))
            }
          </div>
        </div>
    );
  };

  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">경력사항</p>
          <span className="text-md text-gray-700">총 경력 {totalExperience.year}년 {totalExperience.month}개월</span>
        </div>

        <CustomAddButton onClick={() => { openModal(); }} />
      </div>
      <ol className="relative">
        {
          careers.map((career, index) => (
            <li key={career.id} className="pl-1 flex flex-col mb-6 leading-7">
              <div>
                <div className="flex items-center gap-4 mb-3 tracking-tight">
                  <div className="flex items-end gap-4">
                    <span className="text-2xl font-bold">
                      {career.companyName}, {career.jobRank} ({formatDate(career.startedAt)} ~ {formatDate(career.endedAt)})
                    </span>
                    <span className="text-xl text-gray-700">
                      {Math.floor((career.careerPeriodMonth + 1) / 12)}년 {(career.careerPeriodMonth + 1) % 12}개월
                    </span>
                  </div>
                  <div className="flex items-center gap-2">
                    <button onClick={() => {
                      if (confirm("정말 삭제하시겠습니까?")) {
                        deleteCareer(index);
                      }
                    }}>
                      <HiOutlineMinusCircle className="text-3xl text-red-500" />
                    </button>
                    <button onClick={() => {
                      openModal(career.id);
                    }}>
                      <HiOutlinePencil className="text-3xl" />
                    </button>
                  </div>
                </div>
                { formatCareerSummary(career) }
              </div>
            </li>
          ))
        }
      </ol>

      {isModalOpen && <CareerCreateModal profileId={props.profileId} selected={selectedCareer} onSubmit={onSubmit}
        onClose={closeModal} />}
    </div>
  );
};
