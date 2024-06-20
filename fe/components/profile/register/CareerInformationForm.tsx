import { useState } from 'react';
import { Label, Select, TextInput, Checkbox, Textarea } from 'flowbite-react';
import TagInput from '@/components/profile/TagInput';
import { useLocalAxios } from '@/app/api/axios';

const countryList = ["대한민국", "미국", "일본"];
// const employmentType = ["정규직", "계약직"];

export default function CareerInformationForm({ params }: { params: { slug: string } }) {
  const localAxios = useLocalAxios();

  const [form, setForm] = useState({
    companyName: '',
    jobRank: '',
    dept: '',
    isManager: false,
    role: '',
    startedAt: '',
    endedAt: '',
    isCurrent: false,
    companyCountryName: '',
    employType: '',
    description: '',
    companyCountryRegion: null,
    level: null,
    keywords: []
  });

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: name === 'isCurrent' ? e.target.checked : value
    }));
  };

  const handlKeywordsChange = (keywords: any) => {
    setForm(prev => ({
      ...prev,
      keywords
    }));
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    const formData = {
      ...form,
      startedAt: new Date(form.startedAt),
      endedAt: new Date(form.endedAt)
    };

    try {
      const response = await localAxios.post(`/profile/${params.slug}/career`, formData);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <form className="bg-white p-8 border rounded-lg max-w-4xl mx-auto max-h-screen" onSubmit={handleSubmit}>
      <h3 className="text-3xl font-bold mb-6">경력사항 추가</h3>
      <div className="grid grid-cols- md:grid-cols-2 gap-4 gap-y-6">
        <div>
          <Label htmlFor="companyName">회사명*</Label>
          <TextInput id="companyName" name="companyName" placeholder="회사명" required onChange={handleChange} />

          <Label htmlFor="jobRank">직급*</Label>
          <TextInput id="jobRank" name="jobRank" placeholder="직급" required onChange={handleChange} />

          <Label htmlFor="level">레벨*</Label>
          <TextInput id="level" name="level" placeholder="레벨" onChange={handleChange} />


          <Label htmlFor="dept">근무부서*</Label>
          <TextInput id="dept" name="dept" placeholder="부서명" required onChange={handleChange} />

          <Label htmlFor="role">담당업무*</Label>
          <TextInput id="role" name="role" placeholder="업무명" required onChange={handleChange} />

        </div>
        <div>
          <Label htmlFor="isManager">매니징 여부</Label>
          <Checkbox id="isManager" name="isManager" checked={form.isCurrent} onChange={handleChange} />
        </div>
        <div>
          <Label htmlFor="startedAt">입사일</Label>
          <TextInput id="startedAt" name="startedAt" type="date" required onChange={handleChange} />

          <Label htmlFor="endedAt">퇴사일</Label>
          <TextInput id="endedAt" name="endedAt" type="date" disabled={form.isCurrent} required onChange={handleChange} />
          <div>
            <Label htmlFor="isCurrent">현재 근무 중</Label>
            <Checkbox id="isCurrent" name="isCurrent" checked={form.isCurrent} onChange={handleChange} />
          </div>

          <Label htmlFor="companyCountryName">국가*</Label>
          <Select id="companyCountryName" name="companyCountryName" onChange={handleChange}>
            {countryList.map(country => <option key={country} value={country}>{country}</option>)}
          </Select>

          <Label htmlFor="companyCountryRegion">지역</Label>
          <TextInput id="companyCountryRegion" name="companyCountryRegion" type="input" onChange={handleChange} />

          <Label htmlFor="employType">고용형태*</Label>
          <Select id="employType" name="employType" onChange={handleChange}>
            <option value={"FULL_TIME"}>정규직</option>
            <option value={"CONTRACT"}>계약직</option>
            {/* {employmentType.map(type => <option key={type} value={type}>{type}</option>)} */}
          </Select>

        </div>
      </div>
      <Label htmlFor="description">설명</Label>
      <Textarea id="description" name="description" placeholder="업무경험, 주요 성과 중심으로 작성" rows={4} required onChange={handleChange} />

      <div className="mt-6">
        <Label htmlFor="keywords">보유 기술</Label>
        <TagInput tagProp={form.keywords} onUpdateKeywords={handlKeywordsChange} />
      </div>

      <button type="submit" className="mt-4 bg-primary text-white font-bold py-2 px-4 rounded">
        제출
      </button>
    </form>
  );
}
