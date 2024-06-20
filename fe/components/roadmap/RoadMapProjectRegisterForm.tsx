import {
  Label,
  Select,
  TextInput,
  Textarea,
  Checkbox,
  Modal,
  Button,
} from "flowbite-react";
import { useEffect, useState } from "react";
import {
  RequestCategories,
  techmapProjects,
} from "@/types/techmap/techmap-project";
import { useLocalAxios } from "@/app/api/axios";

const techTypeList = [
  { id: "NEW", name: "신규" },
  { id: "EXISTING", name: "기존" },
];

const relativeLevel = [
  { id: "OUTNUMBERED", name: "열세" },
  { id: "NORMAL", name: "동등" },
  { id: "SUPERIOR", name: "우세" },
];

interface ModalProps {
  isModalOpen: boolean;
  onClose: () => void;
  mode: boolean;
  data?: techmapProjects;
  techmapId: number;
}

export default function techmapNewTechRegister(props: ModalProps) {
  const [formData, setFormData] = useState({
    mainCategoryId: 0,
    subCategory: "",
    keyword: "",
    techStatus: "",
    description: "",
    relativeLevel: "",
    relativeLevelReason: "",
    targetStatus: false,
    targetMemberCount: 0,
    techmapId: props.techmapId,
  });
  const localAxios = useLocalAxios();
  const [techMainCategories, setTechMainCategories] = useState<
    RequestCategories[]
  >([]);
  const descriptionMaxLength = 100;
  const reasonMaxLength = 100;
  const handleChange = (event: any) => {
    const { name, type, checked, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const submitFormData = async (event: any) => {
    event.preventDefault();

    try {
      let response;
      if (props.mode) {
        response = await localAxios.post("/techmap-project", formData);
      } else {
        response = await localAxios.put(
          `/techmap-project/${props.data!.techmapProjectId}`,
          formData,
        );
      }

      if (response.status === 200) {
        // 성공
        alert("등록 완료.");
        props.onClose();
      } else {
        alert("등록 실패");
      }
      props.onClose();
    } catch (error: any) {
    }
  };

  // 들어오는 부분이 edit인지 register인지 확인
  useEffect(() => {
    if (props.isModalOpen) {
      if (props.mode) {
        // 등록에 맞는 api 호출
        setFormData({
          mainCategoryId: techMainCategories[0]?.mainCategoryId ?? 0,
          subCategory: "",
          keyword: "",
          techStatus: "NEW",
          description: "",
          relativeLevel: "OUTNUMBERED",
          relativeLevelReason: "",
          targetStatus: false,
          targetMemberCount: 0,
          techmapId: props.techmapId,
        });
      } else {
        setFormData({
          mainCategoryId:
            techMainCategories.find((element) => {
              if (element.mainCategory === props.data?.mainCategory)
                return element;
            })?.mainCategoryId ?? techMainCategories[0]?.mainCategoryId,
          subCategory: props.data?.subCategory ?? "",
          keyword: props.data?.techDetail ?? "",
          techStatus: props.data?.techStatus ?? "",
          description: props.data?.description ?? "",
          relativeLevel: props.data?.techCompanyRelativeLevel ?? "",
          relativeLevelReason: props.data?.relativeLevelReason ?? "",
          targetStatus: props.data?.targetStatus ?? false,
          targetMemberCount: props.data?.targetMemberCount ?? 0,
          techmapId: props.techmapId,
        });
        // 수정에 맞는 api 호출
      }
    }
  }, [props.isModalOpen]);

  const fetchTechMainCategory = async () => {
    const response = await localAxios.get("/tech-main-category");
    setTechMainCategories(response.data);
  };

  useEffect(() => {
    fetchTechMainCategory();
  }, []);
  return (
    <>
      {props.isModalOpen && (
        <Modal show={props.isModalOpen} onClose={props.onClose}>
          <Modal.Header className="text-3xl font-bold">
            tech 신규기술 등록
          </Modal.Header>

          <form onSubmit={submitFormData}>
            <div className="grid grid-cols-6 gap-3 border bg-white p-6">
              {/* 대분류 */}
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label htmlFor="mainCategory">대분류 *</Label>
                <Select
                  id="mainCategoryId"
                  name="mainCategoryId"
                  sizing="sm"
                  onChange={handleChange}
                  value={formData.mainCategoryId}
                  defaultChecked={false}
                  required
                >
                  <option value="">선택해주세요.</option>
                  {techMainCategories?.map((data, index) => (
                    <option key={index} value={data.mainCategoryId}>
                      {data.mainCategory}
                    </option>
                  ))}
                </Select>
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
                <Label htmlFor="subCategory">기술분야 *</Label>
                <TextInput
                  id="subCategory"
                  name="subCategory"
                  sizing="sm"
                  value={formData.subCategory}
                  placeholder={"예: Vision"}
                  onChange={handleChange}
                  autoComplete="off"
                  required
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
                <Label htmlFor="keyword">세부기술 *</Label>
                <TextInput
                  id="keyword"
                  name="keyword"
                  sizing="sm"
                  value={formData.keyword}
                  placeholder={"예: On Device AI"}
                  onChange={handleChange}
                  autoComplete="off"
                  required
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label htmlFor="techStatus">기술구분 *</Label>
                <Select
                  id="techStatus"
                  name="techStatus"
                  value={formData.techStatus}
                  onChange={handleChange}
                  sizing="sm"
                >
                  {techTypeList.map((data, index) => (
                    <option key={index} value={data.id}>
                      {data.name}
                    </option>
                  ))}
                </Select>
              </div>

              {/* <div className="sm:col-span-2"></div> */}

              {/* 기술개요 */}
              <div className="relative col-span-6 sm:col-span-6">
                <div className="mb-2 block">
                  <Label htmlFor="description">기술개요 *</Label>
                </div>
                <Textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  placeholder="기술설명 및 필요한 역량에 대해 작성해주시기 바랍니다."
                  required
                  rows={4}
                  onChange={handleChange}
                  maxLength={descriptionMaxLength}
                  autoComplete="off"
                  className="resize-none"
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
              </div>

              {/* 사업부 */}
              <div className="col-span-6 row-start-3 grid sm:col-span-1 ">
                <div className="flex-col space-y-2">
                  <Label htmlFor="relativeLevel">당사 수준 *</Label>
                  <Select
                    id="relativeLevel"
                    name="relativeLevel"
                    value={formData.relativeLevel}
                    onChange={handleChange}
                    sizing="sm"
                  >
                    {relativeLevel.map((type, index) => (
                      <option key={index} value={type.id}>
                        {type.name}
                      </option>
                    ))}
                  </Select>
                </div>
              </div>

              <div className="relative col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-5">
                <Label htmlFor="relativeLevelReason">판단근거 *</Label>
                <Textarea
                  id="relativeLevelReason"
                  name="relativeLevelReason"
                  value={formData.relativeLevelReason}
                  placeholder="당사수준 우세 동등 열세에 대한 판단 근거를 작성해주시기 바랍니다"
                  required
                  rows={2}
                  onChange={handleChange}
                  autoComplete="off"
                  className="resize-none"
                  maxLength={reasonMaxLength}
                />
                <div className="absolute bottom-0 right-2">
                  {formData.relativeLevelReason.length < reasonMaxLength ? (
                    <p>
                      <span className="text-blue-500">
                        {formData.relativeLevelReason.length}
                      </span>
                      <span>/{reasonMaxLength}</span>
                    </p>
                  ) : (
                    <p className="text-red-500">
                      {formData.relativeLevelReason.length}/{reasonMaxLength}
                    </p>
                  )}
                </div>
              </div>

              {/* 타겟여부 */}
              <div className="col-span-6 flex items-center gap-2 sm:col-span-2">
                <Checkbox
                  id="targetStatus"
                  name="targetStatus"
                  checked={formData.targetStatus}
                  onChange={handleChange}
                />
                <Label htmlFor="targetStatus">당해년도 타겟여부</Label>
              </div>

              <div className="col-span-6 grid gap-y-1  sm:col-span-1">
                <Label htmlFor="targetMemberCount">확보목표 *</Label>
                <div className="flex items-center gap-x-2">
                  <TextInput
                    id="targetMemberCount"
                    name="targetMemberCount"
                    value={formData.targetMemberCount}
                    onChange={handleChange}
                    placeholder={"7"}
                    sizing="sm"
                    type="number"
                    inputMode="numeric"
                    autoComplete="off"
                    min={0}
                    required
                  />
                  명
                </div>
              </div>
              <div className="sm:col-span-3" />
            </div>
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
