import { Education, EducationCreateRequest } from "@/types/talent/ProfileDetailResponse";
import { useEffect, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { Button, Checkbox, Modal, Select, TextInput } from "flowbite-react";
import { AutoCompleteResponse } from "@/types/common/AutoComplete";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
import TagInput from "@/components/profile/TagInput";
import CustomTextarea from "@/components/common/CustomTextarea";
import moment from "moment";

interface EducationCreateModalProps {
  profileId: number;
  selected?: Education;
  onSubmit: (education: Education) => void;
  onClose: () => void;
}

const Degree = [
  { label: "박사", value: "pPollSize" },
  { label: "석사", value: "MASTER" },
  { label: "학사", value: "BACHELOR" }
];

const GraduateStatus = [
  { label: "졸업", value: "GRADUATED" },
  { label: "졸업예정", value: "EXPECTED" },
  { label: "수료", value: "COMPLETED" }
];

const EducationCreateModal = (props: EducationCreateModalProps) => {
  const localAxios = useLocalAxios();
  const [education, setEducation] = useState<EducationCreateRequest>({
    degree: "BACHELOR",
    schoolCountry: "",
    schoolName: "",
    major: "",
    enteredAt: new Date(),
    graduateStatus: "GRADUATED",
    labResearchType: "",
    labResearchDescription: "",
    labResearchResult: "",
    labProfessor: "",
    keywords: []
  });
  const [periodUnknown, setPeriodUnknown] = useState<boolean>(false);

  useEffect(() => {
    setEducation({
      degree: props.selected?.degree || "BACHELOR",
      schoolCountry: props.selected?.school.country || "",
      schoolName: props.selected?.school.schoolName || "",
      major: props.selected?.major || "",
      enteredAt: props.selected?.enteredAt ? moment(props.selected.enteredAt).add(9, "hour").toDate() : undefined,
      graduatedAt: props.selected?.graduatedAt ? moment(props.selected.graduatedAt).add(9, "hour").toDate() : undefined,
      labName: props.selected?.lab?.labName || undefined,
      graduateStatus: props.selected?.graduateStatus || "GRADUATED",
      labResearchType: props.selected?.labResearchType || "",
      labResearchDescription: props.selected?.labResearchDescription || "",
      labResearchResult: props.selected?.labResearchResult || "",
      labProfessor: props.selected?.lab?.labProfessor || "",
      keywords: props.selected?.keywords || []
    });
  }, [props.selected]);

  const submitModal = async (e: React.FormEvent) => {
    e.preventDefault();

    if (props.selected) {
      const response = await localAxios.put<Education>(`/profile/${props.profileId}/education/${props.selected.id}`, education);
      props.onSubmit(response.data);
    } else {
      const response = await localAxios.post<Education>(`/profile/${props.profileId}/education`, education);
      props.onSubmit(response.data);
    }

    props.onClose();
  };

  const handleAutoCompleteInputChange = (key: string, e: AutoCompleteResponse) => {
    setEducation(prev => ({ ...prev, [key]: e.data }));
  };

  const handleInputChange = (key: string, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setEducation(prev => ({ ...prev, [key]: e.target.value }));
  };

  const handleDateChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
    setEducation(prev => ({ ...prev, [key]: new Date(e.target.value) }));
  }

  const handleCheckboxChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
    setEducation(prev => ({
      ...prev,
      [key]: e.target.checked,
      enteredAt: (e.target.checked ? new Date() : undefined),
      graduatedAt: (e.target.checked ? new Date() : undefined)
    }));
  }

  const handleKeywordChange = (keywords: string[]) => {
    if (education) {
      setEducation({ ...education, keywords });
    }
  }

  const formatDateForInput = (date?: Date) => {
    if (date) {
      return date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0");
    }
  }

  return (
    <Modal onClose={props.onClose} show={true} size="5xl">
      <form onSubmit={submitModal}>
        <Modal.Header>학력사항 {props.selected ? "수정" : "추가"}</Modal.Header>
        <Modal.Body className="overflow-visible flex flex-col gap-4">
          <div className="flex items-center gap-4">
            <span className="font-bold">학위*</span>
            <Select
              className="basis-1/12"
              required
              value={education.degree}
              onChange={(e) => { handleInputChange("degree", e); }}
            >
              {
                Degree.map((degree, index) => (
                  <option key={index} value={degree.value}>{degree.label}</option>
                ))
              }
            </Select>
            <span className="font-bold">학교국가*</span>
            <AutoCompleteInput
              className="basis-[11%]"
              identifier="COUNTRY"
              required
              value={{ id: 0, data: education.schoolCountry }}
              onChange={(e) => { if (e) handleAutoCompleteInputChange("schoolCountry", e); }}
            />
            <span className="font-bold">학교명*</span>
            <AutoCompleteInput
              className="basis-2/12"
              identifier="SCHOOL"
              required
              value={{ id: 0, data: education.schoolName }}
              onChange={(e) => { if (e) handleAutoCompleteInputChange("schoolName", e); }}
            />
            <span className="font-bold">전공명*</span>
            <TextInput
              className="basis-2/12"
              placeholder="직접 입력"
              required
              value={education.major}
              onChange={(e) => { handleInputChange("major", e); }}
            />
          </div>
          <div className="flex items-center gap-4">
            {
              !periodUnknown &&
              <>
                <span className="font-bold">입학년월*</span>
                <TextInput
                  className="basis-2/12"
                  type="month"
                  required
                  max="2999-12"
                  value={formatDateForInput(education.enteredAt)}
                  onChange={(e) => { handleDateChange("enteredAt", e); }}
                />
                <span className="font-bold">졸업년월*</span>
                <TextInput
                  className="basis-2/12"
                  type="month"
                  required
                  max="2999-12"
                  value={formatDateForInput(education.graduatedAt)}
                  onChange={(e) => { handleDateChange("graduatedAt", e); }}
                />
              </>
            }
            <label className="flex gap-2 items-center font-bold">
              <Checkbox
                className="size-6"
                checked={periodUnknown}
                onChange={(e) => { setPeriodUnknown(e.target.checked); }}
              />
              입학/졸업년월을 모릅니다.
            </label>
          </div>
          <div className="flex items-center gap-4">
            <span className="font-bold">연구실명</span>
            <AutoCompleteInput
              className="basis-[21%]"
              identifier="LAB"
              value={{ id: 0, data: (education.labName ? education.labName : "") }}
              onChange={(e) => { if (e) handleAutoCompleteInputChange("labName", e); }}
            />
            <span className="font-bold">지도교수</span>
            <TextInput
              className="basis-1/12"
              placeholder="직접 입력"
              value={education.labProfessor}
              disabled
              onChange={(e) => { handleInputChange("labProfessor", e); }}
            />
            <span className="font-bold">연구분야</span>
            <TextInput
              className="basis-[11%]"
              placeholder="직접 입력"
              value={education.labResearchType}
              onChange={(e) => { handleInputChange("labResearchType", e); }}
            />
            <TextInput
              className="basis-2/12"
              placeholder="상세 연구내용 설명"
              value={education.labResearchDescription}
              onChange={(e) => { handleInputChange("labResearchDescription", e); }}
            />
          </div>
          <div className="flex items-start gap-4">
            <span className="font-bold">연구실적</span>
            <CustomTextarea
              rows={3}
              className="flex-1"
              placeholder="논문, 특허, 수상내역 중심으로 작성해주시기 바랍니다."
              value={education.labResearchResult}
              onChange={(e) => { handleInputChange("labResearchResult", e); }}
              maxLength={1000}
            />
          </div>
          <div className="flex items-center gap-4">
            <span className="font-bold">주요기술</span>
            <TagInput tagProp={education.keywords} onUpdateKeywords={handleKeywordChange} />
          </div>
        </Modal.Body>
        <Modal.Footer className="justify-end">
          <Button color="failure" onClick={(e: React.MouseEvent) => { e.preventDefault(); props.onClose(); }}>취소</Button>

          <button>
            <Button>{props.selected ? "수정" : "추가"}</Button>
          </button>
        </Modal.Footer>
      </form>
    </Modal>
  );
}

export default EducationCreateModal;