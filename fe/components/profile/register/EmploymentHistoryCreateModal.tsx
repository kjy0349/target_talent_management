import {
  EmploymentHistory,
  EmploymentHistoryCreateRequest,
  EmploymentHistoryFullCreateRequest,
  InterviewCreateRequest,
} from "@/types/talent/ProfileEmploymentHistory";
import { useLocalAxios } from "@/app/api/axios";
import { useEffect, useState } from "react";
import { AutoCompleteResponse } from "@/types/common/AutoComplete";
import {
  Button,
  Datepicker,
  Modal,
  Select,
  Textarea,
  TextInput,
} from "flowbite-react";
import CustomAddButton from "@/components/common/CustomAddButton";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
import CustomTextarea from "@/components/common/CustomTextarea";
import moment from "moment";

interface EmploymentHistoryCreateModalProps {
  profileId: number;
  selected?: EmploymentHistory;
  onSubmit: (employmentHistory: EmploymentHistory) => void;
  onClose: () => void;
}

const EmploymentHistoryTypes = [
  { label: "활용도 검토", value: "USAGE_REVIEW" },
  { label: "면접", value: "INTERVIEW" },
  { label: "APPROVE_E", value: "APPROVE_E" },
  { label: "처우 협의", value: "NEGOTIATION" },
  { label: "처우 결렬", value: "NEGOTIATION_DENIED" },
  { label: "입사 포기", value: "EMPLOY_ABANDON" },
  { label: "입사 대기", value: "EMPLOY_WAITING" },
  { label: "입사", value: "EMPLOYED" },
];

const NegotiationSteps = ["협의中", "협의完"];
const UsageReviewTypes = ["현업", "HR"];
const UsageReviewResults = ["적합", "부적합"];
const ApproveTypes = ["완료", "미완료"];

const InterviewTypes = [
  { label: "대면", value: "FACE" },
  { label: "비대면", value: "NO_FACE" },
];

const InterviewResultTypes = [
  { label: "A+", value: "A_PLUS" },
  { label: "A", value: "A" },
  { label: "B+", value: "B_PLUS" },
  { label: "B", value: "B" },
  { label: "C", value: "C" },
  { label: "D", value: "D" },
];

const EmploymentHistoryCreateModal = (
  props: EmploymentHistoryCreateModalProps,
) => {
  const localAxios = useLocalAxios();
  const [employmentHistory, setEmploymentHistory] =
    useState<EmploymentHistoryFullCreateRequest>({
      type: "INTERVIEW",
      step: "1차",
      description: "",
      meetDate: new Date(),
      interviewType: "FACE",
      place: "",
      interviewResults: [],
      result: "",
      targetDepartmentName: "",
      targetJobRankName: "",
      executiveName: "",
      targetEmployDate: new Date(),
    });

  useEffect(() => {
    setEmploymentHistory({
      type: props.selected?.type ? props.selected.type : "INTERVIEW",
      step: props.selected?.step ? props.selected.step : "1차",
      description: props.selected?.description
        ? props.selected.description
        : "",
      meetDate: props.selected?.meetDate
        ? moment(props.selected.meetDate).add(9, "hour").toDate()
        : new Date(),
      interviewType: props.selected?.interviewType
        ? props.selected.interviewType
        : "FACE",
      place: props.selected?.place ? props.selected.place : "",
      interviewResults: props.selected?.interviewResults
        ? props.selected.interviewResults.map((result) => ({
            result: result.interviewResultType,
            executiveName: result.executiveName,
          }))
        : [],
      result: props.selected?.result ? props.selected.result : undefined,
      targetDepartmentName: props.selected?.targetDepartmentName
        ? props.selected.targetDepartmentName
        : undefined,
      targetJobRankName: props.selected?.targetJobRankName
        ? props.selected.targetJobRankName
        : undefined,
      executiveName: props.selected?.executiveName
        ? props.selected.executiveName
        : undefined,
      targetEmployDate: props.selected?.targetEmployDate
        ? new Date(props.selected.targetEmployDate)
        : undefined,
    });
  }, [props.selected]);

  const submitModal = async (e: React.FormEvent) => {
    e.preventDefault();

    if (props.selected) {
      if (props.selected.type == "INTERVIEW") {
        const interviewRequest: InterviewCreateRequest = {
          interviewDegree: employmentHistory.step,
          description: employmentHistory.description,
          meetDate: employmentHistory.meetDate,
          interviewType: employmentHistory.interviewType,
          place: employmentHistory.place,
          interviewResults: employmentHistory.interviewResults,
        };

        const response = await localAxios.put<EmploymentHistory>(
          `/profile/${props.profileId}/interview/${props.selected.id}`,
          interviewRequest,
        );
        props.onSubmit(response.data);
      } else {
        const employmentHistoryRequest: EmploymentHistoryCreateRequest = {
          type: employmentHistory.type,
          step: employmentHistory.step,
          description: employmentHistory.description,
          result: employmentHistory.result,
          targetDepartmentName: employmentHistory.targetDepartmentName,
          targetJobRankName: employmentHistory.targetJobRankName,
          executiveName: employmentHistory.executiveName,
          targetEmployDate: employmentHistory.targetEmployDate,
        };

        const response = await localAxios.put<EmploymentHistory>(
          `/profile/${props.profileId}/employment-history/${props.selected.id}`,
          employmentHistoryRequest,
        );
        props.onSubmit(response.data);
      }
    } else {
      if (employmentHistory.type == "INTERVIEW") {
        const interviewRequest: InterviewCreateRequest = {
          interviewDegree: employmentHistory.step,
          description: employmentHistory.description,
          meetDate: employmentHistory.meetDate,
          interviewType: employmentHistory.interviewType,
          place: employmentHistory.place,
          interviewResults: employmentHistory.interviewResults,
        };

        const response = await localAxios.post<EmploymentHistory>(
          `/profile/${props.profileId}/interview`,
          interviewRequest,
        );
        props.onSubmit(response.data);
      } else {
        const employmentHistoryRequest: EmploymentHistoryCreateRequest = {
          type: employmentHistory.type,
          step: employmentHistory.step,
          description: employmentHistory.description,
          result: employmentHistory.result,
          targetDepartmentName: employmentHistory.targetDepartmentName,
          targetJobRankName: employmentHistory.targetJobRankName,
          executiveName: employmentHistory.executiveName,
          targetEmployDate: employmentHistory.targetEmployDate,
        };

        const response = await localAxios.post<EmploymentHistory>(
          `/profile/${props.profileId}/employment-history`,
          employmentHistoryRequest,
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
    setEmploymentHistory((prev) => ({ ...prev, [key]: e.data }));
  };

  const handleInputChange = (
    key: string,
    e: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >,
  ) => {
    if (key == "type") {
      setEmploymentHistory({
        type: e.target.value,
        step: e.target.value == "NEGOTIATION" ? "협의中" : "1차",
        description: "",
        meetDate: new Date(),
        interviewType: "FACE",
        place: "",
        interviewResults: [],
        result: e.target.value == "USAGE_REVIEW" ? "적합" : "완료",
        targetDepartmentName: "",
        targetJobRankName: "",
        executiveName: "",
        targetEmployDate: new Date(),
      });
    }

    setEmploymentHistory((prev) => ({ ...prev, [key]: e.target.value }));
  };

  const handleCheckboxChange = (
    key: string,
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setEmploymentHistory((prev) => ({ ...prev, [key]: e.target.checked }));
  };

  const handleDateChange = (
    key: string,
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setEmploymentHistory((prev) => ({
      ...prev,
      [key]: new Date(e.target.value),
    }));
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
        <Modal.Header>
          채용전형 기록 {props.selected ? "수정" : "추가"}
        </Modal.Header>
        <Modal.Body className="flex max-h-[600px] flex-col gap-4 overflow-visible">
          <div className="flex items-center gap-4">
            <span className="font-bold">타입</span>
            <Select
              className="basis-2/12"
              required
              value={employmentHistory.type}
              onChange={(e) => {
                handleInputChange("type", e);
              }}
            >
              {EmploymentHistoryTypes.map((type, index) => (
                <option key={index} value={type.value}>
                  {type.label}
                </option>
              ))}
            </Select>
          </div>
          {employmentHistory.type === "INTERVIEW" && (
            <>
              <div className="flex items-center gap-4">
                <span className="font-bold">면접단계*</span>
                <TextInput
                  className="basis-1/12"
                  required
                  placeholder="1차"
                  value={employmentHistory.step}
                  onChange={(e) => {
                    handleInputChange("step", e);
                  }}
                />
                <span className="font-bold">면접일시</span>
                <TextInput
                  className="basis-2/12"
                  type="date"
                  max={formatDateForInput(new Date())}
                  value={formatDateForInput(employmentHistory.meetDate)}
                  onChange={(e) => {
                    handleDateChange("meetDate", e);
                  }}
                />
                <span className="font-bold">면접유형*</span>
                <Select
                  className="basis-[11%]"
                  required
                  value={employmentHistory.interviewType}
                  onChange={(e) => {
                    handleInputChange("interviewType", e);
                  }}
                >
                  {InterviewTypes.map((type, index) => (
                    <option key={index} value={type.value}>
                      {type.label}
                    </option>
                  ))}
                </Select>
                <span className="font-bold">면접장소*</span>
                <TextInput
                  className="basis-[18%]"
                  required
                  placeholder="예 : R4 38층 1회의실"
                  value={employmentHistory.place}
                  onChange={(e) => {
                    handleInputChange("place", e);
                  }}
                />
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">면접 내용</span>
                <CustomAddButton
                  size="sm"
                  onClick={(e) => {
                    e.preventDefault();
                    setEmploymentHistory((prev) => ({
                      ...prev,
                      interviewResults: prev.interviewResults
                        ? [
                            ...prev.interviewResults,
                            { result: "A_PLUS", executiveName: "" },
                          ]
                        : [{ result: "A_PLUS", executiveName: "" }],
                    }));
                  }}
                />
              </div>
              <ul className="flex flex-col gap-2 px-4">
                {employmentHistory.interviewResults?.map(
                  (interviewResult, index) => (
                    <li key={index} className="flex items-center gap-4">
                      <span className="font-bold">면접결과*</span>
                      <Select
                        className="basis-1/12"
                        required
                        value={interviewResult.result}
                        onChange={(e) => {
                          if (employmentHistory.interviewResults) {
                            const newInterviewResults = [
                              ...employmentHistory.interviewResults,
                            ];
                            newInterviewResults[index].result = e.target.value;
                            setEmploymentHistory((prev) => ({
                              ...prev,
                              interviewResults: newInterviewResults,
                            }));
                          }
                        }}
                      >
                        {InterviewResultTypes.map((type) => (
                          <option key={type.value} value={type.value}>
                            {type.label}
                          </option>
                        ))}
                      </Select>
                      <span className="font-bold">면접위원*</span>
                      <TextInput
                        className="flex-1"
                        required
                        placeholder="예 : MX사업부 개발실 김삼성 부직급1"
                        value={interviewResult.executiveName}
                        onChange={(e) => {
                          if (employmentHistory.interviewResults) {
                            const newInterviewResults = [
                              ...employmentHistory.interviewResults,
                            ];
                            newInterviewResults[index].executiveName =
                              e.target.value;
                            setEmploymentHistory((prev) => ({
                              ...prev,
                              interviewResults: newInterviewResults,
                            }));
                          }
                        }}
                      />
                    </li>
                  ),
                )}
              </ul>
              <div className="flex items-start gap-4">
                <span className="font-bold">면접내용&nbsp;</span>
                <CustomTextarea
                  rows={5}
                  className="flex-1"
                  placeholder="면접평가 주요 의견을 입력해주세요."
                  value={employmentHistory.description}
                  onChange={(e) => {
                    handleInputChange("description", e);
                  }}
                  maxLength={1000}
                />
              </div>
            </>
          )}
          {employmentHistory.type == "USAGE_REVIEW" && (
            <>
              <div className="flex items-center gap-4">
                <span className="font-bold">구분</span>
                <Select
                  className="basis-[11%]"
                  value={employmentHistory.targetDepartmentName}
                  onChange={(e) => {
                    handleInputChange("targetDepartmentName", e);
                  }}
                >
                  {UsageReviewTypes.map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </Select>
                <span className="font-bold">일시*</span>
                <TextInput
                  className="basis-2/12"
                  type="date"
                  max={formatDateForInput(new Date())}
                  value={formatDateForInput(employmentHistory.targetEmployDate)}
                  onChange={(e) => {
                    handleDateChange("targetEmployDate", e);
                  }}
                />
                <span className="font-bold">검토결과*</span>
                <Select
                  className="basis-[11%]"
                  value={employmentHistory.result}
                  onChange={(e) => {
                    handleInputChange("result", e);
                  }}
                >
                  {UsageReviewResults.map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </Select>
              </div>
              <div className="flex items-center gap-4">
                <span className="font-bold">검토자*</span>
                <TextInput
                  className="basis-4/12"
                  placeholder="예 : MX사업부 개발팀 홍삼성 직급4"
                  value={employmentHistory.executiveName}
                  onChange={(e) => {
                    handleInputChange("executiveName", e);
                  }}
                />
              </div>
              <div className="flex items-start gap-4">
                <span className="font-bold">활용도 검토 의견*</span>
                <CustomTextarea
                  rows={3}
                  className="flex-1"
                  placeholder="검토의견 받은 내용을 입력해주세요."
                  value={employmentHistory.description}
                  onChange={(e) => {
                    handleInputChange("description", e);
                  }}
                  maxLength={1000}
                />
              </div>
            </>
          )}
          {employmentHistory.type == "APPROVE_E" && (
            <>
              <div className="flex items-center gap-4">
                <span className="font-bold">승인단계*</span>
                <TextInput
                  className="basis-[11%]"
                  placeholder="1차"
                  value={employmentHistory.step}
                  onChange={(e) => {
                    handleInputChange("step", e);
                  }}
                />
                <span className="font-bold">승인일시*</span>
                <TextInput
                  className="basis-2/12"
                  type="date"
                  max={formatDateForInput(new Date())}
                  value={formatDateForInput(employmentHistory.targetEmployDate)}
                  onChange={(e) => {
                    handleDateChange("targetEmployDate", e);
                  }}
                />
                <span className="font-bold">결과*</span>
                <Select
                  className="basis-[11%]"
                  value={employmentHistory.result}
                  onChange={(e) => {
                    handleInputChange("result", e);
                  }}
                >
                  {ApproveTypes.map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </Select>
              </div>
              <div className="flex items-start gap-4">
                <span className="font-bold">참고사항</span>
                <CustomTextarea
                  rows={5}
                  className="flex-1"
                  placeholder="직접 입력해주세요."
                  value={employmentHistory.description}
                  onChange={(e) => {
                    handleInputChange("description", e);
                  }}
                  maxLength={1000}
                />
              </div>
            </>
          )}
          {[
            "NEGOTIATION",
            "NEGOTIATION_DENIED",
            "EMPLOY_ABANDON",
            "EMPLOY_WAITING",
            "EMPLOYED",
          ].includes(employmentHistory.type) && (
            <>
              <div className="flex items-center gap-4">
                {employmentHistory.type == "NEGOTIATION" && (
                  <>
                    <span className="font-bold">진행단계*</span>
                    <Select
                      className="basis-[11%]"
                      required
                      value={employmentHistory.step}
                      onChange={(e) => {
                        handleInputChange("step", e);
                      }}
                    >
                      {NegotiationSteps.map((step) => (
                        <option key={step} value={step}>
                          {step}
                        </option>
                      ))}
                    </Select>
                  </>
                )}
                <span className="font-bold">직급</span>
                <AutoCompleteInput
                  className="basis-2/12"
                  identifier="JOBRANK"
                  required
                  value={{
                    id: 0,
                    data: employmentHistory.targetJobRankName
                      ? employmentHistory.targetJobRankName
                      : "",
                  }}
                  onChange={(e) => {
                    if (e)
                      handleAutoCompleteInputChange("targetJobRankName", e);
                  }}
                />
                <span className="font-bold">
                  {employmentHistory.type == "EMPLOY_ABANDON"
                    ? "입사포기일"
                    : "입사예정일"}
                  *
                </span>
                <TextInput
                  className="basis-2/12"
                  type="date"
                  max={formatDateForInput(new Date())}
                  value={formatDateForInput(employmentHistory.targetEmployDate)}
                  onChange={(e) => {
                    handleDateChange("targetEmployDate", e);
                  }}
                />
              </div>
              <div className="flex items-start gap-4">
                <span className="font-bold">참고사항</span>
                <CustomTextarea
                  rows={5}
                  className="flex-1"
                  placeholder="직접 입력해주세요."
                  value={employmentHistory.description}
                  onChange={(e) => {
                    handleInputChange("description", e);
                  }}
                  maxLength={1000}
                />
              </div>
            </>
          )}
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

export default EmploymentHistoryCreateModal;
