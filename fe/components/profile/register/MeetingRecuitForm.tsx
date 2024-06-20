'use client'

import { useLocalAxios } from "@/app/api/axios"
import { useState } from "react";
import { Select, Checkbox, Textarea } from "flowbite-react";
import { HiUpload } from "react-icons/hi";
import { AutoCompleteInput } from "@/components/common/AutoCompleteInput";

const translateTag: { [key: string]: string }[] = [
  { "meetingType": "면담 유형" },
  { "meetAt": "면담일" },
  { "isFace": "대면면담" },
  { "place": "면담장소" },
  { "country": "면담국가" },
  { "description": "상세사항" },
  { "isMemberDirected": "본인 담당" },
  { "currentTask": "현재업무" },
  { "leadershipDescription": "리더쉽" },
  { "interestType": "당사관심도" },
  { "interestTech": "관심분야" },
  { "question": "질문사항" },
  { "etc": "기타사항" },
  { "inChargeMemberName": "담당자" },
  { "targetDepartment": "관심사업부" },
  { "targetJobRank": "기대직급" }
];


export default function MeetingRecuitForm({ params }: { params: { slug: string } }) {
  const localAxios = useLocalAxios();
  const [meetingRecruit, setMeetingRecruit] = useState({
    meetingType: "채용",
    meetAt: new Date(),
    isFace: false,
    country: "",
    place: "",
    isMemberDirected: true,
    inChargeMemberName: 0,
    currentTask: "",
    description: "",
    leadershipDescription: "",
    interestType: "",
    targetDepartment: "",
    interestTech: "",
    targetJobRank: "",
    question: "",
    etc: "",
  });

  const [inChargeMemberName, setInChargeMemberName] = useState();

  function translateKey(key: string) {
    const translation = translateTag.find(pair => pair[key]);
    return translation ? translation[key] : key;
  }

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setMeetingRecruit(prev => ({
      ...prev,
      [name]: name === 'isFace' || name === "isMemberDirected" ? e.target.checked : value
    }));
  };


  const postMeeting = async () => {
    try {
      await localAxios.post(`/profile/${params.slug}/meeting`, meetingRecruit);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="mb-4 p-4 border rounded">
      {Object.entries(meetingRecruit).map(([key]) => (
        <div key={key} className="flex items-center my-1">
          <label className="font-bold w-1/4">{translateKey(key)}</label>
          {key === "isFace" ? (
            <Checkbox id="isFace" name="isFace" checked={meetingRecruit.isFace} onChange={handleChange} />
          ) : key === "isMemberDirected" ? (
            <Checkbox id="isMemberDirected" name="isMemberDirected" checked={meetingRecruit.isMemberDirected} onChange={handleChange} />
          ) : key.includes("description") || key.includes("etc") ? (
            <Textarea
              className="w-96 border border-gray-300 rounded-md"
              rows={4}
              id={key}
              name={key}
              placeholder={translateKey(key) + "을 입력하세요"}
            />
          ) : key.includes("country") || key.includes("place") ? (
            <input
              id={key}
              name={key}
              type="text"
              className="border border-gray-300 rounded-md disabled:bg-gray-200"
              disabled={!meetingRecruit.isFace}
              onChange={handleChange}
              placeholder={translateKey(key)}
            />
          ) : key === "inChargeMemberName" ? (
            <AutoCompleteInput
              identifier="EXECUTIVE"
              className=""
              onChange={handleChange}
              required={meetingRecruit.isFace}

            />
          ) : key === "meetingType" ? (
            <Select id="meetingType">
              <option value="채용">채용</option>
              <option value="면담">면담</option>
              <option value="협의">협의</option>
            </Select>
          ) : (
            <input
              id={key}
              name={key}
              type={key.includes("meetAt") ? "date" : "text"}
              className="border border-gray-300 rounded-md"
              onChange={handleChange}
              placeholder={translateKey(key)}
            />
          )}
        </div>
      ))}
      <button onClick={postMeeting} className="mt-4 bg-green-500 p-2 rounded-md flex items-center justify-center text-white cursor-pointer">
        <HiUpload className="mr-2 inline-block" />등록
      </button>
    </div>
  )
}
