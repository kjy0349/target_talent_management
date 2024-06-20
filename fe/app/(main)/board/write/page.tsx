'use client'

import { useState } from 'react';
import { useLocalAxios } from "@/app/api/axios";
import { Textarea } from 'flowbite-react';
import { useRouter } from 'next/navigation';
import FileUpload from '@/components/board/FileUpload';

const BoardWritePage = () => {
  const localAxios = useLocalAxios();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [fileUrl, setFileUrl] = useState("");
  const router = useRouter();
  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  }
  const handleContentChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContent(event.target.value);
  }
  const handleImageUpload = (fileUrl: string) => {
    setFileUrl(fileUrl);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      const response = await localAxios.post('/article', {
        boardId: 1,
        title: title,
        content: content,
        fileSource: fileUrl,
      })
      if (response.status === 200) {
        alert("공지사항을 등록했습니다.");
        router.push("/board");
      }
      else {
        alert("파일이 올바르지 않습니다.")
      }
    } catch (error) {
      alert("네트워크 오류");
      console.error(error);
    }
  };

  return (
    <div className='min-h-[calc(100vh-128px)] bg-white p-10'>
      <h2 className='mt-10 text-center text-3xl font-bold'>공지사항 등록</h2>
      <div className='mx-auto my-10 flex w-2/3 flex-col gap-4'>
        <div className="flex items-center">
          <label
            htmlFor="title"
            className="mb-2 block w-10 font-bold text-gray-700"
          >
            제목
          </label>
          <input
            type="text"
            id="title"
            className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            name="title"
            value={title}
            placeholder="제목을 입력하세요."
            onChange={handleTitleChange}
            required
          />
        </div>
        <div className="flex w-full">
          <label
            htmlFor="title"
            className="mb-2 block w-10 font-bold text-gray-700"
          >
            내용
          </label>
          <Textarea
            id="description"
            className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            name="description"
            value={content}
            placeholder="내용을 입력하세요."
            onChange={handleContentChange}
            rows={13}
            required
          />
        </div>
        <FileUpload
          onImageUpload={handleImageUpload}
        />
        <button onClick={handleSubmit} className='mx-auto w-fit rounded-md bg-primary p-2 px-4 text-white'>
          작성
        </button>
      </div>
    </div >
  );
};

export default BoardWritePage;
