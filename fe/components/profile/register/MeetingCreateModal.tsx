import {
  Meeting,
  MeetingHeadCreateRequest,
  MeetingRecruitCreateRequest,
} from "@/types/talent/ProfileMeeting";
import {
  Button,
  Checkbox,
  Datepicker,
  Modal,
  Select,
  Textarea,
  TextInput,
} from "flowbite-react";
import { useLocalAxios } from "@/app/api/axios";
import { useEffect, useState } from "react";
import { AutoCompleteResponse } from "@/types/common/AutoComplete";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
import CustomTextarea from "@/components/common/CustomTextarea";
import moment from "moment";

interface MeetingCreateModalProps {
  profileId: number;
  selected?: Meeting;
  onSubmit: (meeting: Meeting) => void;
  onClose: () => void;
}

const MeetingTypes = [
  { label: "현업", value: "CURRENT" },
  { label: "채용부서장", value: "HEAD_RECRUITER" },
  { label: "채용담당자", value: "RECRUITER" },
];

const InterestTypes = [
  { label: "상", value: "UPPER" },
  { label: "중", value: "MIDDLE" },
  { label: "하", value: "LOWER" },
];

const MeetingCreateModal = (props: MeetingCreateModalProps) => {
  const localAxios = useLocalAxios();
  const [meetingType, setMeetingType] = useState<string>("CURRENT");
  const [meeting, setMeeting] = useState<MeetingRecruitCreateRequest>({
    meetAt: new Date(),
    isFace: true,
    country: "",
    place: "",
    isMemberDirected: true,
    participants: "",
    currentTask: "",
    leadershipDescription: "",
    interestType: "",
    interestTech: "",
    question: "",
    etc: "",
    targetDepartment: "",
    targetJobRank: "",
    description: "",
    isNetworking: false,
  });

  useEffect(() => {
    setMeeting({
      meetAt: props.selected?.meetAt
        ? moment(props.selected.meetAt).add(9, "hour").toDate()
        : new Date(),
      isFace: props.selected?.isFace ? props.selected.isFace : true,
      country: props.selected?.country || "",
      place: props.selected?.place || "",
      isMemberDirected: props.selected?.isMemberDirected
        ? props.selected.isMemberDirected
        : true,
      participants: props.selected?.participants || "",
      currentTask: props.selected?.currentTask || "",
      leadershipDescription: props.selected?.leadershipDescription || "",
      interestType: props.selected?.interestType || "",
      interestTech: props.selected?.interestTech || "",
      question: props.selected?.question || "",
      etc: props.selected?.etc || "",
      targetDepartment: props.selected?.targetDepartment || "",
      targetJobRank: props.selected?.targetJobRank || "",
      description: props.selected?.description || "",
      isNetworking: props.selected?.isNetworking
        ? props.selected.isNetworking
        : false,
    });
    setMeetingType(
      props.selected?.meetingType ? props.selected.meetingType : "CURRENT",
    );
  }, [props.selected]);

  const submitModal = async (e: React.FormEvent) => {
    e.preventDefault();

    if (props.selected) {
      if (props.selected.meetingType == "RECRUITER") {
        const response = await localAxios.put<Meeting>(
          `/profile/${props.profileId}/meeting/${props.selected.id}`,
          {
            ...meeting,
            meetingType: "RECRUITER",
          },
        );
        props.onSubmit(response.data);
      } else {
        const headMeeting: MeetingHeadCreateRequest = {
          meetAt: meeting.meetAt,
          isFace: meeting.isFace,
          country: meeting.country,
          place: meeting.place,
          participants: meeting.participants,
          description: meeting.description,
          isNetworking: meeting.isNetworking,
        };

        const response = await localAxios.put<Meeting>(
          `/profile/${props.profileId}/meeting/${props.selected.id}`,
          {
            ...headMeeting,
            meetingType: props.selected.meetingType,
          },
        );
        props.onSubmit(response.data);
      }
    } else {
      if (meetingType == "RECRUITER") {
        const response = await localAxios.post<Meeting>(
          `/profile/${props.profileId}/meeting`,
          {
            ...meeting,
            meetingType: "RECRUITER",
          },
        );
        props.onSubmit(response.data);
      } else {
        const headMeeting: MeetingHeadCreateRequest = {
          meetAt: meeting.meetAt,
          isFace: meeting.isFace,
          country: meeting.country,
          place: meeting.place,
          participants: meeting.participants,
          description: meeting.description,
          isNetworking: meeting.isNetworking,
        };

        const response = await localAxios.post<Meeting>(
          `/profile/${props.profileId}/meeting`,
          {
            ...headMeeting,
            meetingType: meetingType,
          },
        );

        props.onSubmit(response.data);
      }
    }

    props.onClose();
  };

  const handleAutoCompleteInputChange = (
    key: string,
    e: AutoCompleteResponse,
  ) => {
    setMeeting((prev) => ({ ...prev, [key]: e.data }));
  };

  const handleInputChange = (
    key: string,
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >,
  ) => {
    setMeeting((prev) => ({ ...prev, [key]: e.target.value }));
  };

  const handleCheckboxChange = (
    key: string,
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setMeeting((prev) => ({ ...prev, [key]: e.target.checked }));
  };

  const handleMeetingTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setMeetingType(e.target.value);
  };

  const handleDateChange = (
    key: string,
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setMeeting((prev) => ({ ...prev, [key]: new Date(e.target.value) }));
  };

  const formatDateForInput = (date?: Date) => {
    if (date) {
      return (
        date.getFullYear() +
        "-" +
        String(date.getMonth() + 1).padStart(2, "0") +
        "-" +
        String(date.getDate()).padStart(2, "0")
      );
    }
  };

  return (
    <Modal onClose={props.onClose} show={true} size="5xl">
      <form onSubmit={submitModal}>
        <Modal.Header>면담 {props.selected ? "수정" : "추가"}</Modal.Header>
        <Modal.Body className="flex max-h-[600px] flex-col gap-4 overflow-y-scroll">
          <div className="flex items-center gap-4">
            <span className="font-bold">면담유형*</span>
            <Select
              className="basis-2/12"
              required
              value={meetingType}
              onChange={(e) => {
                handleMeetingTypeChange(e);
              }}
            >
              {MeetingTypes.map((type, index) => (
                <option key={index} value={type.value}>
                  {type.label}
                </option>
              ))}
            </Select>
            <span className="font-bold">일시*</span>
            <TextInput
              className="basis-2/12"
              type="date"
              max={formatDateForInput(new Date())}
              required
              value={formatDateForInput(meeting.meetAt)}
              onChange={(e) => {
                handleDateChange("meetAt", e);
              }}
            />
          </div>
          <div className="flex items-center gap-4">
            <span className="font-bold">대면 여부*</span>
            <Checkbox
              className="size-6"
              checked={meeting.isFace}
              onChange={(e) => {
                handleCheckboxChange("isFace", e);
              }}
            />
            <span className="font-bold">국가/장소*</span>
            <AutoCompleteInput
              className="basis-2/12"
              identifier="COUNTRY"
              required
              value={{ id: 0, data: meeting.country }}
              onChange={(e) => {
                if (e) handleAutoCompleteInputChange("country", e);
              }}
            />
            <TextInput
              className="basis-5/12"
              required
              placeholder="예 : SRA OO 회의실"
              value={meeting.place}
              onChange={(e) => {
                handleInputChange("place", e);
              }}
            />
          </div>
          <div className="flex items-start gap-4">
            <span className="font-bold">참 석 자*</span>
            <CustomTextarea
              rows={2}
              required
              className="basis-6/12"
              placeholder="예 : MX사업부 개발팀 김삼성 부직급1"
              value={meeting.participants}
              onChange={(e) => {
                handleInputChange("participants", e);
              }}
              maxLength={400}
            />
            <span className="font-bold">네트워킹 활동*</span>
            <Checkbox
              className="size-6"
              checked={meeting.isNetworking}
              onChange={(e) => {
                handleCheckboxChange("isNetworking", e);
              }}
            />
          </div>
          {meetingType == "RECRUITER" && (
            <>
              <div className="font-bold">[ 면담내용 요약 ]</div>
              <div className="flex items-center gap-4">
                <span className="font-bold">현재업무*</span>
                <TextInput
                  className="flex-1"
                  required
                  placeholder="예 : Retail팀에서 Shopping 고객 니즈 프레임워크 설계 담당"
                  value={meeting.currentTask}
                  onChange={(e) => {
                    handleInputChange("currentTask", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">리 더 십*</span>
                <TextInput
                  className="flex-1"
                  required
                  placeholder="예 : OOO 팀 소속으로 20명 규모의 조직을 총괄 및 매니징 中"
                  value={meeting.leadershipDescription}
                  onChange={(e) => {
                    handleInputChange("leadershipDescription", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">당사관심도*</span>
                <Select
                  className="basis-2/12"
                  required
                  value={meeting.interestType}
                  onChange={(e) => {
                    handleInputChange("interestType", e);
                  }}
                >
                  {InterestTypes.map((type, index) => (
                    <option key={type.value} value={type.value}>
                      {type.label}
                    </option>
                  ))}
                </Select>
                <span className="font-bold">관심사업부*</span>
                <AutoCompleteInput
                  className="basis-3/12"
                  identifier="DEPARTMENT"
                  required
                  value={{ id: 0, data: meeting.targetDepartment }}
                  onChange={(e) => {
                    if (e) handleAutoCompleteInputChange("targetDepartment", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">관심 분야*</span>
                <TextInput
                  className="basis-3/12"
                  required
                  placeholder="직접 입력"
                  value={meeting.interestTech}
                  onChange={(e) => {
                    handleInputChange("interestTech", e);
                  }}
                />
                <span className="font-bold">기대 직급*</span>
                <AutoCompleteInput
                  className="basis-3/12"
                  identifier="JOBRANK"
                  required
                  value={{ id: 0, data: meeting.targetJobRank }}
                  onChange={(e) => {
                    if (e) handleAutoCompleteInputChange("targetJobRank", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">질 문 사 항*</span>
                <TextInput
                  className="flex-1"
                  required
                  placeholder="예 : MX사업부 OOO 서비스 관련 조직현황 및 업무에 대한 설명 문의"
                  value={meeting.question}
                  onChange={(e) => {
                    handleInputChange("question", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">기 타 사 항*</span>
                <TextInput
                  className="flex-1"
                  required
                  placeholder="(당사 지인여부 확인, 과거 당사 입사지원 여부, 대학원 세부전공 등 영입과 관련된 참고사항 입력"
                  value={meeting.etc}
                  onChange={(e) => {
                    handleInputChange("etc", e);
                  }}
                />
              </div>
            </>
          )}
          <div className="flex items-center gap-4">
            <span className="font-bold">면담내용*</span>
            <CustomTextarea
              rows={5}
              className="flex-1"
              placeholder="직접 입력 해주세요."
              value={meeting.description}
              onChange={(e) => {
                handleInputChange("description", e);
              }}
              maxLength={1000}
            />
          </div>
          <div className="flex items-center gap-1 text-red-700">
            ※ 영입 목적과 무관한 개인정보{" "}
            <span className="text-sm">(혼인여부, 가족사항, 병역, 건강 등)</span>{" "}
            입력은 하지 말아주시기 바랍니다.
          </div>
        </Modal.Body>
        <Modal.Footer className="justify-end">
          <Button
            color="failure"
            onClick={(e: React.MouseEvent) => {
              e.preventDefault();
              props.onClose();
            }}
          >
            취소
          </Button>

          <button>
            <Button>{props.selected ? "수정" : "추가"}</Button>
          </button>
        </Modal.Footer>
      </form>
    </Modal>
  );
};

export default MeetingCreateModal;
