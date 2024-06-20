import {
  Avatar,
  Checkbox,
  Label,
  Select,
  TextInput,
  Button,
  Modal,
} from "flowbite-react";
import ProfileImageUpload from "@/components/member/ProfileImageUploaded";
import { useEffect, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { useRouter, useSearchParams } from "next/navigation";
import Image from "next/image";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";
import { AutoCompleteResponse } from "@/types/common/AutoComplete";
import {
  ProfileDetail,
  ProfileColumn,
} from "@/types/talent/ProfileDetailResponse";

export default function PersonalInformationForm() {
  const [originalImageSrc, setOriginalImageSrc] = useState<string | null>();
  const [newImageSrc, setNewImageSrc] = useState<string | null>();
  const [formData, setFormData] = useState<columns>({
    name: "",
    nameEng: "",
    column1: "",
    foundRegion: "",
    column1: "",
    visa: "비대상",
    korean: "상",
    relocation: "확인필요",
    foundPath: "",
    skillMainCategory: "",
    skillSubCategory: "",
    skillDetail: "",
    isS: "N",
    phone: "",
    email: "",
    homepageUrl: "",
    foundCountry: "",
    recommenderInfo: "",
    recommenderRelation: "",
    specialDescription: "",
    isF: "N",
  });
  const [columnAPI, setColumnAPI] = useState<formColumnApi>();
  const router = useRouter();
  const localAxios = useLocalAxios();
  const [foundCountry, setFoundCountry] = useState("");
  const disabledList = {
    미국: ["column1", "column1"],
    일본: "originalImageSrc",
  };
  const [koreanName, setKoreanName] = useState<string[]>(["", ""]);
  const [engName, setEngName] = useState<string[]>(["", "", ""]);
  const [options, setOptions] = useState<Options>();
  const [isAllCompany, setIsAllCompany] = useState(false);
  const [isPrivate, setIsPrivate] = useState(false);
  const [targetJobRank, setTargetJobRank] = useState<JobRank>({
    id: -1,
    description: "",
  });
  const [jobRanks, setJobRanks] = useState<
    { id: number; description: string }[]
  >([]);
  const [isEdit, setIsEdit] = useState(false);
  const [profileId, setProfileId] = useState(0);
  const [openProfileModal, setOpenProfileModal] = useState<boolean>(false);
  const [disabledImage, setDisabledImage] = useState(false);
  const [disabledcolumn1, setDisabledcolumn1] = useState(false);
  const [disabledcolumn1, setDisabledcolumn1] = useState(false);

  useEffect(() => {
    const profileData = sessionStorage.getItem("profileData");
    if (profileData) {
      setIsEdit(true);
      const parsedData: ProfileDetail = JSON.parse(profileData);
      setOriginalImageSrc(parsedData.profileImage);
      setIsPrivate(parsedData.isPrivate);
      setIsAllCompany(parsedData.isAllCompany ?? false);
      setTargetJobRank(parsedData.targetJobRank ?? { id: -1, description: "" });
      parsedData.columns.map((data) => {
        if (data.name === "foundCountry") {
          checkCountry(data.value);
        }
        setFormData((prev) => ({ ...prev, [data.name]: data.value }));
      });
      setProfileId(parsedData.profileId);
      fetchColumnData(false);
    } else {
      fetchColumnData(true);
    }

    // 수정 데이터를 모두 받았으므로 제거
    sessionStorage.removeItem("profileData");
  }, []);

  interface Option {
    [key: string]: string;
  }
  interface column {
    name: string;
    label: string;
    required: boolean;
    type: string; // 데이터 타입 ("STRING", "NUMBER", "BOOLEAN")
    options: string[];
  }
  interface formColumnApi {
    columns: column[];
    disabledColumns: {
      [key: string]: Array<string>;
    };
    jobRanks: {
      id: number;
      description: string;
    }[];
  }
  interface response {
    columns: column[];
  }

  interface JobRank {
    id: number;
    description: string;
  }

  interface ProfileDetail {
    profileId: number;
    profileImage: string;
    isPrivate: true;
    isAllCompany: true;
    targetJobRankId: number;
    targetJobRank?: JobRank;
    columns: ProfileColumn[];
  }

  interface columns {
    name: string;
    nameEng: string;
    column1: string;
    foundRegion: string;
    foundCountry: string;
    column1: string;
    visa: string;
    korean: string;
    relocation: string;
    foundPath: string;
    recommenderInfo: string;
    recommenderRelation: string;
    skillMainCategory: string;
    skillSubCategory: string;
    skillDetail: string;
    isS: string;
    specialDescription: string;
    phone: string;
    email: string;
    homepageUrl: string;
    isF: string;
  }

  interface Options {
    [key: string]: string[];
  }

  const handleImageUpload = (imageUrl: string) => {
    setNewImageSrc(imageUrl);
  };
  const handleImageChange = () => {
    setOriginalImageSrc(newImageSrc);
    setNewImageSrc(null);
    setOpenProfileModal(false);
  };

  const handleChange = (event: any) => {
    const { name, type, checked, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const checkCountry = (country: string) => {
    // 미국이나 일본이기 때문에 해당 위치 값들을 disabled해준다.
    // 그리고 제출 전 안에 데이터를 전송하지 않는다.
    setFoundCountry(country);
    setDisabledcolumn1(false);
    setDisabledcolumn1(false);
    setDisabledImage(false);
    if (country === "미국") {
      setDisabledcolumn1(true);
      setDisabledcolumn1(true);
    } else if (country === "일본") {
      setDisabledImage(true);
    }
  };
  const handleAutoCompleteInputChange = (
    key: string,
    e: AutoCompleteResponse,
  ) => {
    if (key === "targetJobRankId") {
      setTargetJobRank({ id: e.id, description: e.data });
    } else {
      if (key === "foundCountry") {
        // 미국이나 일본이기 때문에 해당 위치 값들을 disabled해준다.
        // 그리고 제출 전 안에 데이터를 전송하지 않는다.
        checkCountry(e.data);
      }
      setFormData((prev) => ({ ...prev, [key]: e.data }));
    }
  };

  const fetchColumnData = async (regist: boolean) => {
    try {
      const response = await localAxios.get<formColumnApi>("/profile/column");
      const temp: Options = {};

      response.data.columns.map((data) => {
        if (data.options.length > 0) {
          temp[data.name] = data.options;
          // 등록일 경우
          // if (regist) {
          //   console.log("??????");
          //   if (data.name === "isS" || "isF") {
          //     setFormData((prev) => ({ ...prev, [data.name]: "N" }));
          //   } else if (data.name === "visa") {
          //     setFormData((prev) => ({
          //       ...prev,
          //       [data.name]: "비대상",
          //     }));
          //   } else if (data.name === "relocation") {
          //     setFormData((prev) => ({
          //       ...prev,
          //       [data.name]: "확인필요",
          //     }));
          //   } else {
          //     setFormData((prev) => ({
          //       ...prev,
          //       [data.name]: data.options[0],
          //     }));
          //   }
          // }
        }
      });
      setOptions(temp);
      setJobRanks(response.data.jobRanks);
    } catch (error) {
      console.error(error);
    }
  };

  const handleName = (event: any) => {
    const { name, value } = event.target;
    let updatedEngName = [...engName];
    let updatedKoreanName = [...koreanName];

    if (name === "eng-first-name") {
      updatedEngName = [value, engName[1], engName[2]];
    } else if (name === "eng-middle-name") {
      updatedEngName = [engName[0], value, engName[2]];
    } else if (name === "eng-last-name") {
      updatedEngName = [engName[0], engName[1], value];
    } else if (name === "last-name") {
      updatedKoreanName = [value, koreanName[1]];
    } else if (name === "first-name") {
      updatedKoreanName = [koreanName[0], value];
    }

    const updatedKoreanNameStr = updatedKoreanName.join("");
    const updatedEngNameStr = updatedEngName.filter(Boolean).join(" ");

    // 상태 업데이트
    setEngName(updatedEngName);
    setKoreanName(updatedKoreanName);

    // formData 업데이트
    setFormData((prevState) => ({
      ...prevState,
      name: updatedKoreanNameStr,
      nameEng: updatedEngNameStr,
    }));
  };

  const handelFullName = (event: any) => {
    const { name, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const submitFormData = async (event: React.FormEvent<HTMLFormElement>) => {
    // 로그인 해서 확인하는 로직 넣는 부분
    event.preventDefault();

    try {
      // 데이터를 넣기 전 국가를 확인한다.
      const data = {
        profileImage: originalImageSrc,
        columns: formData,
        isPrivate: isPrivate,
        isAllCompany: isAllCompany,
        targetJobRankId: targetJobRank.id === -1 ? null : targetJobRank.id,
      };
      if (foundCountry === "미국") {
        data.columns.column1 = "";
        data.columns.column1 = "";
      } else if (foundCountry === "일본") {
        data.profileImage = "";
      }

      if (isEdit) {
        const response = await localAxios.put(`/profile/${profileId}`, data);
        if (response.status === 200) {
          alert("수정되었습니다.");
          router.back();
        }
      } else {
        const response = await localAxios.post("/profile", data);
        if (response.status === 200) {
          alert("등록되었습니다.");
          router.push(`/talent/${response.data.profileId}`);
        }
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <form className="" onSubmit={submitFormData}>
        <div className="grid grid-cols-6 gap-6 rounded border bg-white p-10">
          <h3 className="col-span-6 pb-5 text-3xl font-bold">
            {isEdit ? "프로필 수정" : "신규 프로필 등록"}
          </h3>
          {/*  프로필 사진 */}
          <div className="relative col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label key="image" className="mb-5 font-bold">
              프로필 사진*
            </Label>
            {disabledImage ? (
              <Image
                alt="Profile"
                width="200"
                height="200"
                src="/assets/picture/profile.png"
              />
            ) : (
              <Image
                alt="Profile"
                width="200"
                height="200"
                src={
                  originalImageSrc
                    ? process.env.NEXT_PUBLIC_BASE_URL + "/" + originalImageSrc
                    : "/assets/picture/profile.png"
                }
                onClick={() => setOpenProfileModal(true)}
                className="hover:cursor-pointer"
              />
            )}
            <Modal
              show={openProfileModal}
              onClose={() => setOpenProfileModal(false)}
            >
              <Modal.Header>프로필 이미지 수정</Modal.Header>
              <Modal.Body className="p-4">
                <p className="pb-3 text-end font-extrabold">
                  *3:4비율로 올려주세요.
                </p>
                <div className="grid grid-cols-2 gap-5">
                  <div className="col-span-1 flex flex-col items-center">
                    <p className="mb-2 text-lg font-semibold">새로운 이미지</p>
                    <div className="w-[200px]">
                      <ProfileImageUpload
                        className="aspect-[3/4] hover:cursor-pointer"
                        // defaultAvatar="/assets/picture/upload-image.png"
                        onImageUpload={handleImageUpload}
                      />
                      <p className="text-xs">새로운 이미지를 클릭해서 사진을</p>
                      <p className="text-xs">등록 또는 수정할 수 있습니다.</p>
                    </div>
                  </div>
                  <div className="col-span-1 flex flex-col items-center ">
                    <p className="mb-2 text-lg font-semibold">기존 이미지</p>
                    <div className="relative aspect-[3/4] w-[200px]">
                      <Image
                        alt="Profile"
                        fill
                        // width="200"
                        // height="200"
                        src={
                          originalImageSrc
                            ? process.env.NEXT_PUBLIC_BASE_URL +
                              "/" +
                              originalImageSrc
                            : "/assets/picture/profile.png"
                        }
                        className="object-cover"
                      />
                    </div>
                  </div>
                </div>
              </Modal.Body>
              <Modal.Footer className="justify-end">
                <Button onClick={handleImageChange}>등록하기</Button>
                <Button onClick={() => setOpenProfileModal(false)}>
                  취소하기
                </Button>
              </Modal.Footer>
            </Modal>

            <p className="text-sm font-light">
              클릭해서 사진을 등록 또는 수정할 수 있습니다.
            </p>
            {/* <p>파일 용량 제한 20KB</p> */}
          </div>
          <div className="sm:col-span-5"></div>

          {isEdit ? (
            <>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1 ">
                <Label htmlFor="last-name" className="font-bold">
                  한글이름*
                </Label>
                <TextInput
                  id="name"
                  name="name"
                  placeholder={"예: 김상성"}
                  value={formData.name}
                  onChange={handelFullName}
                  autoComplete="off"
                  required
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
                <Label htmlFor="last-name" className="font-bold">
                  영어이름
                </Label>
                <TextInput
                  id="nameEng"
                  name="nameEng"
                  placeholder={"예: Kim Samsung"}
                  value={formData.nameEng}
                  onChange={handelFullName}
                  autoComplete="off"
                />
              </div>
              <div className="sm:col-span-3" />
            </>
          ) : (
            <>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1 ">
                <Label htmlFor="last-name" className="font-bold">
                  한글이름*
                </Label>
                <TextInput
                  id="last-name"
                  name="last-name"
                  placeholder={"예: 김"}
                  onChange={handleName}
                  autoComplete="off"
                  value={koreanName[0]}
                  required
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label className="invisible font-bold" htmlFor="first-name">
                  이름
                </Label>
                <TextInput
                  id="first-name"
                  name="first-name"
                  placeholder={"예: 삼성"}
                  onChange={handleName}
                  value={koreanName[1]}
                  autoComplete="off"
                  required
                />
              </div>
              {/* 영어이름 */}
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label htmlFor="eng-first-name" className="font-bold">
                  영어 이름
                </Label>
                <TextInput
                  id="eng-first-name"
                  name="eng-first-name"
                  placeholder={"first name"}
                  autoComplete="off"
                  onChange={handleName}
                  value={engName[0]}
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label
                  className="invisible font-bold"
                  htmlFor="eng-middle-name"
                >
                  중간이름
                </Label>
                <TextInput
                  id="eng-middle-name"
                  name="eng-middle-name"
                  placeholder={"middle name"}
                  autoComplete="off"
                  onChange={handleName}
                  value={engName[1]}
                />
              </div>
              <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
                <Label className="invisible font-bold" htmlFor="eng-last-name">
                  성
                </Label>
                <TextInput
                  id="eng-last-name"
                  name="eng-last-name"
                  placeholder={"last name"}
                  autoComplete="off"
                  onChange={handleName}
                  value={engName[2]}
                />
              </div>
              <div className="sm:col-span-1" />
            </>
          )}
          {/* 국가 / 지역 */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="foundCountry" className="font-bold">
              발굴국가*
            </Label>
            <AutoCompleteInput
              required
              identifier="COUNTRY"
              value={{ id: 0, data: formData.foundCountry }}
              onChange={(e) => {
                if (e) handleAutoCompleteInputChange("foundCountry", e);
              }}
            />
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label className="font-bold" htmlFor="foundRegion">
              발굴지역
            </Label>
            <TextInput
              id="foundRegion"
              name="foundRegion"
              placeholder={"예: 서울"}
              onChange={handleChange}
              autoComplete="off"
              value={formData.foundRegion}
            />
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="column1" className="font-bold">
              column1
            </Label>
            <TextInput
              id="column1"
              name="column1"
              min={1900}
              max={2099}
              type="number"
              placeholder="예: 1990"
              autoComplete="off"
              value={formData.column1}
              onChange={handleChange}
              disabled={disabledcolumn1}
            />
          </div>
          <div className="md:col-span-2"></div>
          {/* column1 */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="column1" className="font-bold">
              column1
            </Label>
            <AutoCompleteInput
              identifier="COUNTRY"
              value={{ id: 0, data: formData.column1 }}
              onChange={(e) => {
                if (e) handleAutoCompleteInputChange("column1", e);
              }}
              disabled={disabledcolumn1}
            />
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="visa" className="font-bold">
              비자유형
            </Label>
            <Select
              id="visa"
              name="visa"
              onChange={handleChange}
              value={formData.visa}
            >
              {options?.visa?.map((visa) => <option key={visa}>{visa}</option>)}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="korean" className="font-bold">
              한국어
            </Label>
            <Select
              id="korean"
              name="korean"
              onChange={handleChange}
              value={formData.korean}
            >
              {options?.korean?.map((language) => (
                <option key={language}>{language}</option>
              ))}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="relocation" className="font-bold">
              Relocation
            </Label>
            <Select
              id="relocation"
              name="relocation"
              value={formData.relocation}
              onChange={handleChange}
            >
              {options?.relocation?.map((relocation) => (
                <option key={relocation}>{relocation}</option>
              ))}
            </Select>
          </div>
          <div className="sm:col-span-2"></div>
          {/* 발굴 경로 */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="foundPath" className="font-bold">
              발굴경로*
            </Label>
            <Select
              id="foundPath"
              name="foundPath"
              value={formData.foundPath}
              onChange={handleChange}
              required
            >
              <option key={-1} value="">
                선택
              </option>
              {options?.foundPath?.map((path, index) => (
                <option key={index} value={path}>
                  {path}
                </option>
              ))}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="recommenderInfo" className="font-bold">
              추천인 정보
            </Label>
            <TextInput
              id="recommenderInfo"
              name="recommenderInfo"
              onChange={handleChange}
              autoComplete="off"
              value={formData.recommenderInfo}
              placeholder={"예 : MX 사업부 개발팀 김삼성 프로"}
            />
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="recommenderRelation" className="font-bold">
              추천인 관계
            </Label>
            <TextInput
              id="recommenderRelation"
              name="recommenderRelation"
              onChange={handleChange}
              autoComplete="off"
              value={formData.recommenderRelation}
              placeholder={"예 : 대학원 선후배"}
            />
          </div>
          <div className="sm:col-span-1"></div>
          {/* 타겟 직급 */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="targetJobRankId" className="font-bold">
              타겟직급
            </Label>
            <Select
              id="jobRank"
              name="jobRank"
              value={targetJobRank.id}
              onChange={(e) => {
                setTargetJobRank({
                  id: parseInt(e.target.value),
                  description: "",
                });
              }}
              required
            >
              <option value="">선택</option>
              {jobRanks.map((jobRank) => (
                <option key={jobRank.id} value={jobRank.id}>
                  {jobRank.description}
                </option>
              ))}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="skillMainCategory" className="font-bold">
              tech
            </Label>
            <Select
              id="skillMainCategory"
              name="skillMainCategory"
              autoComplete="off"
              value={formData.skillMainCategory}
              onChange={handleChange}
            >
              <option key={-1} value="">
                선택
              </option>
              {options?.skillMainCategory?.map((mainCategory, index) => (
                <option key={index} value={mainCategory}>
                  {mainCategory}
                </option>
              ))}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="skillSubCategory" className="font-bold">
              special
            </Label>
            <Select
              id="skillSubCategory"
              name="skillSubCategory"
              onChange={handleChange}
              value={formData.skillSubCategory}
            >
              <option key={-1} value="">
                선택
              </option>
              {options?.skillSubCategory?.map((subCategory, index) => (
                <option key={index} value={subCategory}>
                  {subCategory}
                </option>
              ))}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="skillDetail" className="font-bold">
              상세분야
            </Label>
            <TextInput
              id="skillDetail"
              name="skillDetail"
              placeholder={"예 : 통신 IP 설계"}
              autoComplete="off"
              value={formData.skillDetail}
              onChange={handleChange}
            />
          </div>
          <div className="sm:col-span-1" />
          {/* pS */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="isF" className="font-bold">
              pS
            </Label>
            <Select
              id="isF"
              name="isF"
              value={formData.isF}
              onChange={handleChange}
            >
              {options?.isF?.map((data) => <option key={data}>{data}</option>)}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="isS" className="font-bold">
              isS
            </Label>
            <Select
              id="isS"
              name="isS"
              value={formData.isS}
              onChange={handleChange}
            >
              {options?.isS?.map((data) => <option key={data}>{data}</option>)}
            </Select>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="specialDescription" className="font-bold">
              isS 사유
            </Label>
            <TextInput
              id="specialDescription"
              name="specialDescription"
              onChange={handleChange}
              autoComplete="off"
              value={formData.specialDescription}
              placeholder={"직접 입력"}
            />
          </div>
          <div className="sm:col-span-2"></div>
          {/* 전화번호 */}
          {/* <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <Label htmlFor="phone" className="font-bold">
              전화번호
            </Label>
            <TextInput id="phone" name="phone" placeholder={"국가번호"} />
          </div> */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label className=" font-bold" htmlFor="phone">
              전화번호
            </Label>
            <TextInput
              id="phone"
              name="phone"
              placeholder={"000-000-0000"}
              autoComplete="off"
              value={formData.phone}
              onChange={handleChange}
            />
          </div>
          {/* 이메일 */}
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="email" className="font-bold">
              이메일
            </Label>
            <TextInput
              id="email"
              name="email"
              placeholder={"park.samsung@gamil.com"}
              type="email"
              autoComplete="off"
              value={formData.email}
              onChange={handleChange}
            />
          </div>
          <div className="sm:col-span-2" />
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2"></div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
            <Label htmlFor="homepageUrl" className="font-bold">
              개인홈페이지 URL
            </Label>
            <TextInput
              id="homepageUrl"
              name="homepageUrl"
              onChange={handleChange}
              autoComplete="off"
              value={formData.homepageUrl}
              placeholder={"개인홈페이지 URL"}
            />
          </div>
          <div className="sm:col-span-2" />
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <div className="flex space-x-2">
              <Label htmlFor="isPrivate" className="font-bold">
                나만보기
              </Label>
              <Checkbox
                id="isPrivate"
                name="isPrivate"
                checked={isPrivate}
                onChange={() => setIsPrivate(!isPrivate)}
              />
            </div>
          </div>
          <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
            <div className="flex space-x-2">
              <Label htmlFor="isAllCompany" className="font-bold">
                전사 프로필 등록
              </Label>
              <Checkbox
                id="isAllCompany"
                name="isAllCompany"
                checked={isAllCompany}
                onChange={() => setIsAllCompany(!isAllCompany)}
              />
            </div>
          </div>
          <div className="sm:col-span-4" />
        </div>
        <Button
          type="submit"
          // onClick={submitFormData}
          className="mx-auto bg-primary"
        >
          등록
        </Button>
      </form>
    </>
  );
}
