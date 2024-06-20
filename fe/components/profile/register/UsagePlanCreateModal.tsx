import {UsagePlan, UsagePlanCreateRequest} from "@/types/talent/ProfileUsagePlan";
import {useLocalAxios} from "@/app/api/axios";
import {useEffect, useState} from "react";
import {AutoCompleteResponse} from "@/types/common/AutoComplete";
import {Button, Datepicker, Modal, Textarea, TextInput} from "flowbite-react";
import {AutoCompleteInput} from "@/components/common/AutoCompleteInput";
import CustomTextarea from "@/components/common/CustomTextarea";
import moment from "moment";

interface UsagePlanCreateModalProps {
    profileId: number;
    selected?: UsagePlan;
    onSubmit: (usagePlan: UsagePlan) => void;
    onClose: () => void;
}

const UsagePlanCreateModal = (props: UsagePlanCreateModalProps) => {
    const localAxios = useLocalAxios();
    const [usagePlan, setUsagePlan] = useState<UsagePlanCreateRequest>({
        mainDescription: "",
        detailDescription: "",
        targetEmployDate: new Date(),
        jobRank: "",
        targetDepartmentName: ""
    });

    useEffect(() => {
        setUsagePlan({
            mainDescription: props.selected?.mainDescription || "",
            detailDescription: props.selected?.detailDescription || "",
            targetEmployDate: props.selected?.targetEmployDate ? moment(props.selected.targetEmployDate).add(9, "hour").toDate() : new Date(),
            jobRank: props.selected?.jobRank || "",
            targetDepartmentName: props.selected?.targetDepartmentName || ""
        });
    }, [props.selected]);

    const submitModal = async (e: React.FormEvent) => {
        e.preventDefault();

        if (props.selected) {
            const response = await localAxios.put<UsagePlan>(`/profile/${props.profileId}/usage-plan/${props.selected.id}`, usagePlan);
            props.onSubmit(response.data);
        } else {
            const response = await localAxios.post<UsagePlan>(`/profile/${props.profileId}/usage-plan`, usagePlan);
            props.onSubmit(response.data);
        }

        props.onClose();
    }

    const handleAutoCompleteInputChange = (key: string, e: AutoCompleteResponse) => {
        setUsagePlan(prev => ({ ...prev, [key]: e.data }));
    }

    const handleInputChange = (key: string, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setUsagePlan(prev => ({ ...prev, [key]: e.target.value }));
    }

    const handleDateChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
        setUsagePlan(prev => ({ ...prev, [key]: new Date(e.target.value) }));
    }

    const formatDateForInput = (date?: Date) => {
        if (date) {
            return date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0") + "-" + String(date.getDate()).padStart(2, "0");
        }
    }

    return (
        <Modal onClose={props.onClose} show={true} size="5xl">
            <form onSubmit={submitModal}>
                <Modal.Header>활용계획 { props.selected ? "수정" : "추가" }</Modal.Header>
                <Modal.Body className="overflow-visible flex flex-col gap-4">
                    <div className="flex items-center gap-4">
                        <span className="font-bold">활용부서</span>
                        <TextInput
                            className="flex-1"
                            type="text"
                            placeholder="예 : OO사업부 OOOO팀 OOOO그룹"
                            value={usagePlan.targetDepartmentName}
                            onChange={ (e) => { handleInputChange("targetDepartmentName", e); } }
                        />
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">주요설명</span>
                        <TextInput
                            className="flex-1"
                            type="text"
                            placeholder="예 : AI기반 실시간 네트워크 최적화 솔루션 개발"
                            value={usagePlan.mainDescription}
                            onChange={ (e) => { handleInputChange("mainDescription", e); } }
                        />
                    </div>
                    <div className="flex items-start gap-4">
                        <span className="font-bold">상세설명</span>
                        <CustomTextarea
                            rows={5}
                            className="flex-1"
                            placeholder="상세 활용계획 내용에 대해 직접 입력해주세요."
                            value={usagePlan.detailDescription}
                            onChange={ (e) => { handleInputChange("detailDescription", e); } }
                            maxLength={1000}
                        />
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">활용직급</span>
                        <AutoCompleteInput
                            className="basis-2/12"
                            identifier="JOBRANK"
                            value={ { id: 0, data: usagePlan.jobRank } }
                            onChange={ (e) => { if (e) handleAutoCompleteInputChange("jobRank", e); } }
                        />
                        <span className="font-bold">입사목표일</span>
                        <TextInput
                            className="basis-2/12"
                            type="date"
                            max="2999-12-31"
                            value={formatDateForInput(usagePlan.targetEmployDate)}
                            onChange={ (e) => { handleDateChange("targetEmployDate", e); } }
                        />
                    </div>
                </Modal.Body>
                <Modal.Footer className="justify-end">
                    <Button color="failure" onClick={ (e: React.MouseEvent) => { e.preventDefault(); props.onClose(); } }>취소</Button>

                    <button>
                        <Button>{ props.selected ? "수정" : "추가" }</Button>
                    </button>
                </Modal.Footer>
            </form>
        </Modal>
    )
};

export default UsagePlanCreateModal;