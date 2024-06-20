import { Label, TextInput, Textarea } from 'flowbite-react';

export default function ProfessionForm() {
  // todo : 제출 시 string 하나로 묶어서 api 제출
  return (
    <div className="bg-white p-8 border rounded-lg max-w-4xl mx-auto min-w-[40lvw]">
      <h3 className="text-3xl font-bold mb-6">special 추가</h3>
      <div className="grid grid-cols-1">
        <div>
          <Label htmlFor="job-field">직무분야*</Label>
          <TextInput id="job-field" name="job-field" placeholder="직무분야" required />

          <Label htmlFor="tech-field">기술분야*</Label>
          <TextInput id="tech-field" name="tech-field" placeholder="기술분야" required />

          <Label htmlFor="description">상세분야*</Label>
          <Textarea rows={2} id="description" name="description" placeholder="상세분야" required />

        </div>
      </div>
    </div>
  );
}
