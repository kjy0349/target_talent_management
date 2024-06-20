import {Career, CareerCreateRequest} from "@/types/talent/ProfileDetailResponse";
import {Button, Checkbox, Datepicker, Modal, Select, Textarea, TextInput} from "flowbite-react";
import {useEffect, useState} from "react";
import {AutoCompleteInput} from "@/components/common/AutoCompleteInput";
import TagInput from "@/components/profile/TagInput";
import {AutoCompleteResponse} from "@/types/common/AutoComplete";
import {useLocalAxios} from "@/app/api/axios";
import CustomTextarea from "@/components/common/CustomTextarea";
import moment from "moment";

interface CareerCreateModalProps {
    profileId: number;
    selected?: Career;
    onSubmit: (career: Career) => void;
    onClose: () => void;
}

const EmployType = [
    { label: "정규직", value: "FULL_TIME" },
    { label: "계약직", value: "CONTRACT" }
];

const CareerCreateModal = (props: CareerCreateModalProps) => {
    const localAxios = useLocalAxios();
    const [career, setCareer] = useState<CareerCreateRequest>({
        companyName: "",
        companyCountryName: "",
        companyRegion: "",
        jobRank: "",
        level: "",
        isManager: false,
        employType: "FULL_TIME",
        startedAt: new Date(),
        isCurrent: false,
        dept: "",
        role: "",
        description: "",
        keywords: []
    });

    useEffect(() => {
            setCareer({
                companyName: props.selected?.companyName || "",
                companyCountryName: props.selected?.country || "",
                companyRegion: props.selected?.region || "",
                jobRank: props.selected?.jobRank || "",
                level: props.selected?.level || "",
                isManager: props.selected?.isManager || false,
                employType: props.selected?.employType || "FULL_TIME",
                startedAt: props.selected?.startedAt ? moment(props.selected.startedAt).add(9, "hour").toDate() : new Date(),
                endedAt: props.selected?.endedAt ? moment(props.selected.endedAt).add(9, "hour").toDate() : undefined,
                isCurrent: props.selected?.isCurrent || false,
                dept: props.selected?.dept || "",
                role: props.selected?.role || "",
                description: props.selected?.description || "",
                keywords: props.selected?.keywords || []
            });
    }, [props.selected]);

    const submitModal = async (e: React.FormEvent) => {
        e.preventDefault();

        if (props.selected) {
            const response = await localAxios.put<Career>(`/profile/${props.profileId}/career/${props.selected.id}`, career);
            props.onSubmit(response.data);
        } else {
            const response = await localAxios.post<Career>(`/profile/${props.profileId}/career`, career);
            props.onSubmit(response.data);
        }

        props.onClose();
    }

    const handleAutoCompleteInputChange = (key: string, e: AutoCompleteResponse) => {
        setCareer(prev => ({ ...prev, [key]: e.data }));
    };

    const handleInputChange = (key: string, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        setCareer(prev => ({ ...prev, [key]: e.target.value }));
    };

    const handleDateChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
        setCareer(prev => ({ ...prev, [key]: new Date(e.target.value) }));
    }
    const handleCheckboxChange = (key: string, e: React.ChangeEvent<HTMLInputElement>) => {
        setCareer(prev => ({
            ...prev,
            [key]: e.target.checked,
            endedAt: (e.target.checked ? new Date() : undefined)
        }));
    };

    const handleKeywordChange = (keywords: string[]) => {
        if (career) {
            setCareer({ ...career, keywords });
        }
    };

    const formatDateForInput = (date?: Date) => {
        if (date) {
            return date.getFullYear() + "-" + String(date.getMonth() + 1).padStart(2, "0");
        }
    }

    return (
        <Modal onClose={props.onClose} show={true} size="5xl">
            <form onSubmit={submitModal}>
                <Modal.Header>경력사항 { props.selected ? "수정" : "추가" }</Modal.Header>
                <Modal.Body className="overflow-visible flex flex-col gap-4">
                    <div className="flex items-center gap-4">
                        <span className="font-bold">회사명*</span>
                        <AutoCompleteInput
                            className="basis-3/12"
                            identifier="COMPANY"
                            required
                            value={ { id: 0, data: career.companyName }}
                            onChange={ (e) => { if (e) handleAutoCompleteInputChange("companyName", e); } }
                        />
                        <span className="font-bold">직급*</span>
                        <TextInput
                            className="basis-2/12"
                            type="text"
                            placeholder="직접 입력"
                            required
                            value={career.jobRank}
                            onChange={ (e) => { handleInputChange("jobRank", e); } }
                        />
                        <span className="font-bold">레벨</span>
                        <TextInput
                            className="basis-[11%]"
                            type="text"
                            placeholder="직접 입력"
                            value={career.level}
                            onChange={ (e) => { handleInputChange("level", e); } }
                        />
                        <span className="font-bold">매니저 여부*</span>
                        <Checkbox
                            className="size-6"
                            checked={career.isManager}
                            onChange={ (e) => { handleCheckboxChange("isManager", e); } }
                        />
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">고용형태*</span>
                        <Select
                            required
                            value={career.employType}
                            onChange={ (e) => { handleInputChange("employType", e); } }
                        >
                            {
                                EmployType.map((type, index) => (
                                    <option key={index} value={type.value}>{type.label}</option>
                                ))
                            }
                        </Select>
                        <span className="font-bold">국가*</span>
                        <AutoCompleteInput
                            className="basis-2/12"
                            identifier="COUNTRY"
                            required
                            value={ { id: 0, data: career.companyCountryName} }
                            onChange={ (e) => { if (e) handleAutoCompleteInputChange("companyCountryName", e); } }
                        />
                        <span className="font-bold">지역</span>
                        <TextInput
                            className="basis-2/12"
                            placeholder="직접 입력"
                            value={career.companyRegion}
                            onChange={ (e) => { handleInputChange("companyRegion", e); } }
                        />
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">시작년월*</span>
                        <TextInput
                            className="basis-2/12"
                            type="month"
                            max="2999-12"
                            required
                            value={formatDateForInput(career.startedAt)}
                            onChange={ (e) => { handleDateChange("startedAt", e); } }
                        />
                        {
                            !career.isCurrent &&
                            <>
                                <span className="font-bold">종료년월*</span>
                                <TextInput
                                    className="basis-2/12"
                                    type="month"
                                    max="2999-12"
                                    required
                                    value={formatDateForInput(career.endedAt)}
                                    onChange={ (e) => { handleDateChange("endedAt", e); } }
                                />
                            </>
                        }
                        <label className="flex gap-2 items-center font-bold">
                            <Checkbox
                                className="size-6"
                                checked={career.isCurrent}
                                onChange={ (e) => { handleCheckboxChange("isCurrent", e); } }
                            />
                            현재 근무 중
                        </label>
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">근무부서*</span>
                        <TextInput
                            className="basis-3/12"
                            placeholder="직접 입력"
                            required
                            value={career.dept}
                            onChange={ (e) => { handleInputChange("dept", e); } }
                        />
                        <span className="font-bold">담당업무*</span>
                        <TextInput
                            className="flex-1"
                            placeholder="직접 입력"
                            required
                            value={career.role}
                            onChange={ (e) => { handleInputChange("role", e); } }
                        />
                    </div>
                    <div className="flex items-start gap-4">
                        <span className="font-bold">설&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명&nbsp;</span>
                        <CustomTextarea
                            rows={4}
                            className="flex-1"
                            placeholder="업무경험, 주요 성과 중심으로 작성해주시기 바랍니다."
                            value={career.description}
                            onChange={(e) => {
                                handleInputChange("description", e);
                            }}
                            maxLength={1000}
                        />
                    </div>
                    <div className="flex items-center gap-4">
                        <span className="font-bold">보유기술&nbsp;</span>
                        <TagInput tagProp={career.keywords} onUpdateKeywords={handleKeywordChange}/>
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
    );
}

export default CareerCreateModal;