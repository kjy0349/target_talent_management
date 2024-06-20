import { useState } from 'react';
import { HiSave, HiUpload } from 'react-icons/hi';
import TagInputDetail from '../TagInputDetail';
import { Select } from 'flowbite-react';
import { useLocalAxios } from '@/app/api/axios';

interface InterviewResultCreateDto {
  resultId: number;
  comment: string;
}

const translateTag: { [key: string]: string }[] = [
  { "interviewDegree": "면접 단계" },
  { "meetDate": "면접 일시" },
  { "interviewType": "면접 유형" },
  { "place": "면접 장소" },
  { "description": "면접 내용" },
  { "interviewResults": "면접 결과" },
]

// interface InterviewDetails {
//   interviewDegree: number;
//   meetDate: Date;
//   interviewType: string,
//   place: string;
//   description: string;
//   interviewResults: InterviewResultCreateDto[];
// }

export default function InterviewForm({ params }: { params: { slug: string } }) {
  const localAxios = useLocalAxios();

  const [newInterview, setNewInterview] = useState({
    "interviewDegree": 0,
    "meetDate": new Date(),
    "interviewType": "",
    "place": "",
    "description": "",
    "interviewResults": [],
  });

  function translateKey(key: string) {
    const translation = translateTag.find(pair => pair[key]);
    return translation ? translation[key] : key;
  }

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setNewInterview(prev => ({
      ...prev,
      [name]: name === '면접 결과' ? e.target.checked : value
    }));
  };

  const postInterview = async () => {
    try {
      await localAxios.post(`/profile/${params.slug}/interview`, newInterview);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <form className="mb-4 p-4 border rounded" onSubmit={postInterview}>
      {Object.entries(newInterview).map(([key, value]) => (
        <div key={key} className="flex items-center my-1">
          <label
            id={key}
            htmlFor={key}
            className="font-bold w-1/4">{translateKey(key)}</label>
          {key === 'interviewResults' ? (
            <TagInputDetail
              onUpdatePairs={handleChange}
            />
          ) : key.includes('유형') ? (
            <Select
              id={key}
              name={key}
              onChange={handleChange}
            >
              <option value="대면">대면</option>
              <option value="비대면">비대면</option>
            </Select>
          ) : key.includes('내용') ? (
            <textarea
              id={key}
              name={key}
              rows={3}
              className="flex-grow border border-gray-300 p-2 rounded-md"
              onChange={handleChange}
            />
          ) : (
            <input
              id={key}
              name={key}
              type={key.includes('일시') ? 'date' : 'text'}
              className="flex-grow border border-gray-300 p-2 rounded-md"
              onChange={handleChange}
            />
          )}
        </div>
      ))}

      <button type='submit' className="mt-4 bg-green-500 p-2 rounded-md flex items-center justify-center text-white">
        <HiUpload className="mr-2 inline-block" />등록
      </button>
    </form>
  );
}
