"use client";

import { Label, Select, TextInput, Checkbox, Textarea } from "flowbite-react";
import { useState } from "react";
const countryList = ["대한민국", "미국", "일본"];
const degreeList = [{ 학사: "BACHELOR" }, { 석사: "pPollSize" }, { 박사: "Doctors" }];
import { useLocalAxios } from "@/app/api/axios";

// import TagInput from "@/components/profile/TagInput";
// const researchType = ["분류"];

export default function EducationalInformationForm({
  params,
}: {
  params: { slug: string };
}) {
  const localAxios = useLocalAxios();

  const [knowDates, setKnowDates] = useState(false);

  const [form, setForm] = useState({
    degree: "",
    educationCountry: "",
    schoolName: "",
    major: "",
    enteredAt: null,
    graduatedAt: null,
    labName: null,
    labResearchType: null,
    labResearchDescription: null,
    labResearchResult: null,
    labProfessor: null,
  });

  const handleChange = (e: any) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    try {
      const response = await localAxios.post(
        `/profile/${params.slug}/education`,
        form,
      );
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <form
      className="mx-auto max-w-4xl rounded-lg border bg-white p-8"
      onSubmit={handleSubmit}
    >
      <div className="grid grid-cols-6 gap-6 rounded border bg-white p-10">
        <h3 className="col-span-6 text-3xl font-bold">학력사항</h3>
        {/* 학위 */}
        <div className="col-span-1 grid grid-cols-1 gap-y-2">
          <Label htmlFor="degree">학위*</Label>
          <Select id="degree" name="degree" onChange={handleChange}>
            {degreeList.map((degree) => {
              const [label, value] = Object.entries(degree)[0];
              return (
                <option key={value} value={value}>
                  {label}
                </option>
              );
            })}
          </Select>
        </div>
        <div className="col-span-5"></div>

        {/* 학교국가 */}
        <div className="grid grid-cols-1 gap-y-2">
          <Label htmlFor="educationCountry">학교국가*</Label>
          <Select
            id="educationCountry"
            name="educationCountry"
            onChange={handleChange}
          >
            {countryList.map((country) => (
              <option key={country} value={country}>
                {country}
              </option>
            ))}
          </Select>
        </div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
          <Label htmlFor="schoolName">학교명*</Label>
          <TextInput
            id="schoolName"
            name="schoolName"
            placeholder={"학교명"}
            onChange={handleChange}
            required
          />
        </div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
          <Label htmlFor="major">전공명*</Label>
          <TextInput
            id="major"
            name="major"
            placeholder={"전공명"}
            onChange={handleChange}
            required
          />
        </div>
        <div className="col-span-2 row-span-2 grid grid-cols-1 gap-y-2">
          <Label htmlFor="enterdAt">입학년월*</Label>
          <TextInput
            defaultValue={"1"}
            id="enterdAt"
            name="enterdAt"
            required
            type="month"
            onChange={handleChange}
            disabled={knowDates}
          />
        </div>
        <div className="col-span-2 row-span-2 grid grid-cols-1 gap-y-2">
          <Label htmlFor="graduatedAt">졸업년월*</Label>
          <TextInput
            defaultValue={"1"}
            id="graduatedAt"
            name="graduatedAt"
            required
            type="month"
            disabled={knowDates}
          />
        </div>
        <div className="row-span-2" />
        <div className="col-span-2 flex items-center gap-2">
          <Checkbox
            id="knowDates"
            name="knowDates"
            onClick={() => setKnowDates(!knowDates)}
            checked={knowDates}
          />
          <Label htmlFor="knowDates">입학/졸업년월을 모릅니다.</Label>
        </div>
        <div className="col-span-4"></div>

        {/* 연구분야 */}
        {/* <div className="col-span-1 grid grid-cols-1 gap-y-2">
          <Label htmlFor="settings-language">연구분야*</Label>
          <Select id="settings-language" name="settings-language">
            {researchType.map((research) => (
              <option key={research}>{research}</option>
            ))}
          </Select>
        </div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-2">
          <Label htmlFor="last-name">지도교수</Label>
          <TextInput
            id="last-name"
            name="last-name"
            placeholder={"상세 연구내용 작성"}
            required
          />
        </div>
        <div className="col-span-3"></div> */}

        {/* 연구실명 */}
        <div className="col-span-2 row-span-2 grid grid-cols-1 gap-y-2">
          <Label htmlFor="labName">연구실명</Label>
          <TextInput
            id="labName"
            name="labName"
            placeholder={"연구실명"}
            onChange={handleChange}
          />
        </div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
          <Label htmlFor="labProfessor">지도교수</Label>
          <TextInput
            id="labProfessor"
            name="labProfessor"
            placeholder={"교수명"}
            required={form.labName !== null}
            onChange={handleChange}
          />
        </div>
        <div className="col-span-4"></div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
          <Label htmlFor="labResearchType">연구분류</Label>
          <TextInput
            id="labResearchType"
            name="labResearchType"
            placeholder={"분류명"}
            required={form.labName !== null}
            onChange={handleChange}
          />
        </div>
        <div className="col-span-4"></div>
        <div className="col-span-6 grid grid-cols-1 gap-y-2 sm:col-span-1">
          <Label htmlFor="labResearchDescription">연구상세</Label>
          <Textarea
            id="labResearchDescription"
            name="labResearchDescription"
            placeholder={"상세내역 입력"}
            required={form.labName !== null}
            onChange={handleChange}
          />
        </div>
        <div className="col-span-4"></div>

        {/* 연구실적 */}

        <div className="col-span-2">
          <div className="mb-2 block">
            <Label htmlFor="labResearchResult">연구실적</Label>
          </div>
          <Textarea
            id="labResearchResult"
            name="labResearchResult"
            placeholder="논문, 특허, 수상내용"
            required={form.labName !== null}
            rows={4}
            onChange={handleChange}
          />
        </div>

        {/* 주요기술 */}
        {/* <div className="col-span-3">
          <Label htmlFor="comment">주요기술</Label>
          {/* <TagInput /> */}
        {/* </div> */}
      </div>
      <button
        type="submit"
        className="mt-4 rounded bg-primary px-4 py-2 font-bold text-white"
      >
        제출
      </button>
    </form>
  );
}
