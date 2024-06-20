import {useLocalAxios} from "@/app/api/axios";
import {Specialization, SpecializationCreateRequest} from "@/types/talent/ProfileSpecialization";
import {useEffect, useState} from "react";
import {Button, Modal, Textarea, TextInput} from "flowbite-react";
import CustomTextarea from "@/components/common/CustomTextarea";

interface SpecializationCreateModalProps {
    profileId: number;
    selected?: Specialization;
    onSubmit: (specialization: Specialization) => void;
    onClose: () => void;
}

const SpecializationCreateModal = (props: SpecializationCreateModalProps) => {
    const localAxios = useLocalAxios();
    const [specialization, setSpecialization] = useState<SpecializationCreateRequest>({
        specialPoint: "",
        description: ""
    });

    useEffect(() => {
        setSpecialization({
            specialPoint: props.selected?.specialPoint || "",
            description: props.selected?.description || ""
        });
    }, [props.selected]);

    const submitModal = async (e: React.FormEvent) => {
        e.preventDefault();

        if (props.selected) {
            const response = await localAxios.put<Specialization>(`/profile/${props.profileId}/specialization/${props.selected.id}`, specialization);
            props.onSubmit(response.data);
        } else {
            const response = await localAxios.post<Specialization>(`/profile/${props.profileId}/specialization`, specialization);
            props.onSubmit(response.data);
        }

        props.onClose();
    }

    const handleInputChange = (key: string, e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setSpecialization(prev => ({ ...prev, [key]: e.target.value }));
    }

    return (
        <Modal onClose={props.onClose} show={true} size="5xl">
            <form onSubmit={submitModal}>
                <Modal.Header>special { props.selected ? "수정" : "추가" }</Modal.Header>
                <Modal.Body className="overflow-visible flex flex-col gap-4">
                    <div className="flex items-center gap-4">
                        <span className="font-bold">전문역량*</span>
                        <TextInput
                            className="flex-1"
                            type="text"
                            placeholder="예 : 네트워크 최적화 및 AI 알고리즘 개발 전문가"
                            required
                            value={specialization.specialPoint}
                            onChange={ (e) => { handleInputChange("specialPoint", e); } }
                        />
                    </div>
                    <div className="flex items-start gap-4">
                        <span className="font-bold">설&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명&nbsp;</span>
                        <CustomTextarea
                            rows={5}
                            className="flex-1"
                            placeholder="예 : AI 알고리즘을 기반으로 Big Data 분석 및 IoT 보안 최적화 솔루션 개발"
                            value={specialization.description}
                            onChange={ (e) => { handleInputChange("description", e); } }
                            maxLength={1000}
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
}

export default SpecializationCreateModal;