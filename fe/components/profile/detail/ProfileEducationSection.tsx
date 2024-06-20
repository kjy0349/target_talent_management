import { HiOutlineMinusCircle, HiOutlinePencil, HiPencil, HiPlus, HiX } from "react-icons/hi";
import { useEffect, useState } from "react";
import { Career, Education } from "@/types/talent/ProfileDetailResponse";
import EducationalInformationForm from "../register/EducationalInformationForm";
import dateString from "@/hooks/dateToString";
import { Button } from "flowbite-react";
import CustomAddButton from "@/components/common/CustomAddButton";
import EducationCreateModal from "@/components/profile/register/EducationCreateModal";
import { useLocalAxios } from "@/app/api/axios";

interface EducationProps {
  profileId: number;
  educations: Education[];
  onUpdate: (education: Education) => void;
  reload: () => void;
}

export default function ProfileEducationSection(props: EducationProps) {
  const localAxios = useLocalAxios();
  const [educations, setEducations] = useState<Education[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedEducation, setSelectedEducation] = useState<Education>();

  useEffect(() => {
    setEducations(props.educations);
  }, [props.educations]);

  const formatDate = (dateString?: string) => {
    if (dateString) {
      const date = new Date(dateString);
      return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}`;
    } else {
      return "현재";
    }
  };

  const formatDegree = (degreeString: string) => {
    switch (degreeString) {
      case "pPollSize":
        return "박사";
      case "MASTER":
        return "석사";
      case "BACHELOR":
        return "학사";
    }
  };

  const educationPeriodMonth = (education: Education) => {
    const startDate = education.enteredAt ? new Date(education.enteredAt) : new Date();
    const endDate = education.graduatedAt ? new Date(education.graduatedAt) : new Date();

    const startYear = startDate.getFullYear();
    const startMonth = startDate.getMonth();
    const endYear = endDate.getFullYear();
    const endMonth = endDate.getMonth();

    let yearDiff = endYear - startYear;
    let monthDiff = endMonth - startMonth;

    if (monthDiff < 0) {
      monthDiff += 12;
      yearDiff -= 1;
    }

    return yearDiff * 12 + monthDiff + 1;
  };

  const openModal = (educationId?: number) => {
    if (educationId) {
      setSelectedEducation(educations.find(x => x.id === educationId));
    } else {
      setSelectedEducation(undefined);
    }

    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedEducation(undefined);
    setIsModalOpen(false);
  }

  const onSubmit = (education: Education) => {
    if (selectedEducation) {
      const newEducations = [...educations];
      newEducations[educations.findIndex(x => x.id === education.id)] = education;
      setEducations(newEducations);
    } else {
      setEducations(prev => [education, ...prev]);
    }
  };

  const deleteEducation = async (index: number) => {
    await localAxios.delete(`/profile/${props.profileId}/education/${educations[index].id}`);
    props.reload();
  }

  const formatEducationSummary = (education: Education) => {
    const firstLineElements = [];

    if (education.lab && education.lab.labName && education.lab.labName.trim().length > 0) firstLineElements.push(education.lab.labName);
    if (education.lab && education.lab.labProfessor && education.lab.labProfessor.trim().length > 0) firstLineElements.push(education.lab.labProfessor);

    const secondLineElements = [];

    if (education.labResearchType && education.labResearchType.trim().length > 0) secondLineElements.push(education.labResearchType);
    if (education.labResearchResult && education.labResearchResult.trim().length > 0) secondLineElements.push(education.labResearchResult);

    return (
        <div className="mt-1 flex flex-col gap-1">
          <div className="flex items-center gap-2 text-lg">{ firstLineElements.join(', ') }</div>
          <div className="text-lg">{ secondLineElements.join(', ') }</div>
          <div className="flex gap-2">
            {
              education.keywords?.map((keyword, index) => (
                  <div key={index} className="px-2 border-2 border-gray-200 rounded-sm">{keyword}</div>
              ))
            }
          </div>
        </div>
    )
  };

  return (
    <div className="shadow-md p-10">
      <div className="flex flex-row justify-between items-center border-b-2 mb-6 pb-2">
        <div className="flex items-end gap-3">
          <p className="text-2xl font-bold whitespace-nowrap">학력사항</p>
        </div>

        <CustomAddButton onClick={() => { openModal(); }} />
      </div>
      <ol className="relative">
        {
          educations.map((education, index) => (
            <li key={education.id} className="pl-1 flex flex-col mb-6 leading-7">
              <div>
                <div className="flex items-center gap-4 mb-3 tracking-tight">
                  <div className="flex items-end gap-4">
                    <span className="text-2xl font-bold">
                      ({formatDegree(education.degree)}) {education.school.schoolName}
                      , {education.major} ({formatDate(education.enteredAt)} ~ {formatDate(education.graduatedAt)})
                    </span>
                    <span className="text-xl text-gray-700">
                      {Math.floor(educationPeriodMonth(education) / 12)}년 {educationPeriodMonth(education) % 12}개월
                    </span>
                  </div>
                  <div className="flex items-center gap-2">
                    <button onClick={() => {
                      if (confirm("정말 삭제하시겠습니까?")) {
                        deleteEducation(index);
                      }
                    }}>
                      <HiOutlineMinusCircle className="text-3xl text-red-500" />
                    </button>
                    <button onClick={() => {
                      openModal(education.id);
                    }}>
                      <HiOutlinePencil className="text-3xl" />
                    </button>
                  </div>
                </div>
                { formatEducationSummary(education) }
              </div>
            </li>
          ))
        }
      </ol>

      {isModalOpen &&
        <EducationCreateModal profileId={props.profileId} selected={selectedEducation} onSubmit={onSubmit}
          onClose={closeModal} />}
    </div>
  );
}
