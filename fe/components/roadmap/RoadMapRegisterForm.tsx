"use client";
import {
  Label,
  TextInput,
  Textarea,
  Checkbox,
  Button,
  Modal,
  Select,
  Badge,
} from "flowbite-react";
import { useState, useEffect } from "react";
import TagInput from "@/components/profile/TagInput";
import { techmapList, techmapProjectForm } from "@/types/techmap/techmap";
import { Department } from "@/types/techmap/common";
import { useLocalAxios } from "@/app/api/axios";
// import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  mode: boolean;
  data?: techmapList;
  departments?: Department[];
}

export default function techmapRegisterForm(props: ModalProps) {
  const localAxios = useLocalAxios();
  const [departmentList, setDepartmentList] = useState<Department[]>([]);
  const [formData, setFormData] = useState<techmapProjectForm>({
    targetYear: props.data?.targetYear ?? 0,
    description: props.data?.description ?? "",
    dueDate: new Date(),
    keywords: props.data?.keywords ?? [],
    departments: [],
    isAlarmSend: false,
  });
  const [date, setDate] = useState("2024-05-05");
  const descriptionMaxLength = 100;

  useEffect(() => {
    setDepartmentList([]);
    if (props.mode) {
      // 등록
      setFormData({
        targetYear: 2024,
        description: "",
        dueDate: new Date(),
        keywords: [],
        departments: [],
        isAlarmSend: false,
      });
    } else {
      setDepartmentList(mappedDepartments ?? []);

      // 수정
      setFormData({
        targetYear: props.data?.targetYear ?? 0,
        description: props.data?.description ?? "",
        dueDate: new Date(),
        keywords: props.data?.keywords ?? [],
        departments: [],
        isAlarmSend: false,
      });
    }
  }, [props.isModalOpen]);

  const mappedDepartments = props.data?.departments.map((dept) => {
    const found = props.departments?.find((d) => d.data === dept);
    return {
      id: found?.id ?? 0, // 찾은 객체가 있으면 id 반환, 없으면 null
      data: dept,
    };
  });

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    // 로그인 해서 확인하는 로직 넣는 부분
    event.preventDefault();

    if (departmentList.length === 0) {
      alert("하나 이상의 부서를 반드시 선택해야합니다.")
      return;
    }

    try {
      let response = null;
      if (props.mode) {
        response = await localAxios.post(`/techmap`, formData);
      } else {
        response = await localAxios.put(
          `/techmap/${props.data?.techmapId}`,
          formData,
        );
      }

      if (response.status === 200) {
        props.onClose();
      }
    } catch (error) {
      console.error("등록 에러", error);
    }
  };

  const handleDepartment = (event: any) => {
    const { name, value } = event.target;
    const parts = value.split(":"); // ':'를 구분자로 사용하여 분리
    const id = Number(parts[0]);
    const data = parts[1];
    if (data === "null") return;
    function checkElement(element: Department) {
      return element.id === id;
    }

    if (!departmentList.some(checkElement)) {
      const newDepartmentList = [...departmentList, { id: id, data: data }];
      setDepartmentList(newDepartmentList);
      setFormData((prevState) => ({
        ...prevState,
        [name]: newDepartmentList.map((department) => department.id),
      }));
    }
  };

  const handleChange = (event: any) => {
    const { name, type, checked, value } = event.target;

    if (type === "date") setDate(value);

    setFormData((prevState) => ({
      ...prevState,
      [name]:
        type === "checkbox"
          ? checked
          : type === "date"
            ? new Date(value)
            : value,
    }));
  };
  const handleDeleteDepartment = (index: number) => {
    const newItems = departmentList.filter((_, idx) => idx !== index);
    setDepartmentList(newItems);
  };

  // const updateKeywords = (newKeywords: Array<string>) => {
  //   setFormData((prevState) => ({
  //     ...prevState,
  //     ["keywords"]: newKeywords,
  //   }));
  // };

  return (
    <>
      {props.isModalOpen && (
        <Modal show={props.isModalOpen} onClose={props.onClose}>
          <Modal.Header className="text-3xl font-bold">
            tech 생성
          </Modal.Header>
          <form onSubmit={handleSubmit}>
            <Modal.Body className="max-h-[500px] overflow-y-auto p-0">
              <div className="grid grid-cols-6 gap-2 border bg-white p-5">
                {/* 적용연도 */}
                <div className="col-span-6 grid grid-cols-1 sm:col-span-1">
                  <div className="flex-col space-y-2">
                    <Label htmlFor="targetYear">적용연도*</Label>
                    <TextInput
                      id="targetYear"
                      name="targetYear"
                      type="number"
                      min="1900"
                      max="2099"
                      step="1"
                      placeholder="2024"
                      value={formData.targetYear}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>
                <div className="col-span-6 grid grid-cols-1 sm:col-span-2">
                  <div className="flex-col space-y-2">
                    <Label htmlFor="departments">사업부 *(중복선택가능)</Label>
                    <Select
                      id="departments"
                      name="departments"
                      onChange={handleDepartment}
                    >
                      <option key={-1} value={`-1:null`}>
                        선택해주세요
                      </option>
                      {props.departments?.map((department, index) => (
                        <option
                          key={index}
                          value={`${department.id}:${department.data}`}
                        >
                          {department.data}
                        </option>
                      ))}
                    </Select>
                  </div>
                </div>

                <div className="col-span-6 grid grid-cols-1 space-x-2 sm:col-span-3">
                  <div className="grid grid-cols-3 gap-1">
                    {departmentList.map((department, index) => (
                      <Badge
                        key={index}
                        size={"sm"}
                        onClick={() => handleDeleteDepartment(index)}
                        className="justify-center hover:cursor-pointer"
                      >
                        {department.data}
                      </Badge>
                    ))}
                  </div>
                </div>

                {/* 설명 */}
                {/* <div className="relative col-span-6 sm:col-span-6">
                  <div className="mb-1 block">
                    <Label htmlFor="description">설명 *</Label>
                  </div>
                  <Textarea
                    id="description"
                    name="description"
                    placeholder="업무경험, 주요 성과 중심으로 작성"
                    required
                    rows={4}
                    className="resize-none"
                    value={formData.description}
                    onChange={handleChange}
                    maxLength={descriptionMaxLength}
                  />
                  <div className="absolute bottom-0 right-2">
                    {formData.description.length < descriptionMaxLength ? (
                      <p>
                        <span className="text-blue-500">
                          {formData.description.length}
                        </span>
                        <span>/{descriptionMaxLength}</span>
                      </p>
                    ) : (
                      <p className="text-red-500">
                        {formData.description.length}/{descriptionMaxLength}
                      </p>
                    )}
                  </div>
                </div> */}

                {/* 등록기한 */}
                <div className="col-span-6 grid grid-cols-1 sm:col-span-2">
                  <Label htmlFor="sendAlarm">등록기한 *</Label>
                  <TextInput
                    type="date"
                    id="dueDate"
                    name="dueDate"
                    value={date}
                    onChange={handleChange}
                    required
                  ></TextInput>
                </div>
                <div className="col-span-6 grid grid-cols-1 items-center sm:col-span-4">
                  <p className="text-xs leading-relaxed text-gray-400">
                    *등록기한 내 제출을 못할 경우, 각 사업부에 리마인더 알림이
                    발송됩니다.
                  </p>
                </div>
                {/* 키워드 */}
                {/* <div className="col-span-6 grid grid-cols-1 sm:col-span-5">
                  <Label htmlFor="keywords">키워드 *</Label>
                  <TagInput
                    tagProp={formData.keywords}
                    onUpdateKeywords={(newTags) => updateKeywords(newTags)}
                  />
                </div> */}

                {/* 담당자에게 알림 */}
                <div className="col-span-6 flex items-center gap-2 sm:col-span-3">
                  <Checkbox
                    id="isAlarmSend"
                    name="isAlarmSend"
                    checked={formData.isAlarmSend}
                    onChange={handleChange}
                  />
                  <Label htmlFor="isAlarmSend">
                    담당자에게 알림 보내지 않기
                  </Label>
                </div>
                <div className="sm:col-span-3" />
              </div>
            </Modal.Body>
            <Modal.Footer className="justify-end">
              <Button type="submit">등록</Button>
              <Button onClick={props.onClose}>취소</Button>
            </Modal.Footer>
          </form>
        </Modal>
      )}
    </>
  );
}
