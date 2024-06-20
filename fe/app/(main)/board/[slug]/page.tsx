'use client'

import { useEffect, useState } from 'react';
import { useLocalAxios } from "@/app/api/axios";
import { Textarea } from 'flowbite-react';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'next/navigation';
import { HiChevronDoubleLeft, HiDownload } from 'react-icons/hi';
import FileUpload from '@/components/board/FileUpload';

interface BoardDetail {
  title: string,
  writer: string,
  viewCount: number,
  content: string,
  createdAt: string,
  fileSource?: string,
}

const BoardDetailPage = ({ params }: { params: { slug: number } }) => {
  const localAxios = useLocalAxios();
  const [boardDetail, setBoardDetail] = useState<BoardDetail | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [fileUrl, setFileUrl] = useState("");
  const authStore = useAuthStore();
  const name = authStore.name;
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

  const fetchBoardDetail = async () => {
    try {
      const response = await localAxios.get(`/article/${params.slug}/in/${1}`);
      setBoardDetail(response.data);
      setTitle(response.data.title);
      setContent(response.data.content);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchBoardDetail();
  }, []);

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  }

  const saveChanges = async () => {
    try {
      const response = await localAxios.put(`/article/${params.slug}`, {
        title: title,
        content: content,
        fileSource: fileUrl.length > 0 ? fileUrl : boardDetail?.fileSource
      });
      if (response.status === 200) {
        alert("공지사항을 수정했습니다.")
        setIsEditing(false);
        fetchBoardDetail();
      }
      else {
        alert("파일이 올바르지 않습니다.")
      }
    } catch (error) {
      alert("네트워크 오류");
      console.error(error);
    }
  }

  const deleteBoard = async () => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      try {
        const response = await localAxios.delete(`/article`, {
          data: [params.slug]
        });
        if (response.status === 200) {
          (router.push("/board"));
        }
        else {
          alert("네트워크 오류");
        }
      } catch (error) {
        console.error(error);
      }
    }
  }

  if (!boardDetail) {
    return <div className='min-h-[calc(100vh-128px)] bg-white p-10'>게시글을 가져오고 있습니다.</div>;
  }

  const renderButton = () => {
    if (name === boardDetail.writer) {
      return (
        <div className='flex w-full flex-row justify-center gap-4'>
          {isEditing ? (
            <button
              onClick={saveChanges}
              className='w-fit rounded-md bg-blue-500 p-2 px-4 text-white'>
              저장
            </button>
          ) : (
            <button
              onClick={toggleEdit}
              className='w-fit rounded-md bg-primary p-2 px-4 text-white'>
              수정
            </button>
          )}
          <button
            onClick={deleteBoard}
            className='w-fit rounded-md bg-red-500 p-2 px-4 text-white'>
            삭제
          </button>
        </div>
      )
    }
  }

  return (
    <div className='min-h-[calc(100vh-128px)] bg-white p-10'>
      <p className='flex h-10 cursor-pointer flex-row items-center' onClick={() => router.push("/board")}><HiChevronDoubleLeft className='inline' />목록으로 이동</p>
      <p className='text-center text-3xl font-bold'>공지사항 상세</p>
      <div className='mx-auto my-10 flex w-2/3 flex-col gap-4'>
        <p className='text-right font-light'>등록일 : {new Date(boardDetail.createdAt).toLocaleDateString()}</p>
        <div className="flex items-center">
          <label htmlFor="title" className="mb-2 block w-10 font-bold text-gray-700">
            제목
          </label>
          <input
            type="text"
            id="title"
            className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            name="title"
            value={title}
            placeholder="Enter title here."
            disabled={!isEditing}
            onChange={handleTitleChange}
            required
          />
        </div>
        <div className="flex w-full">
          <label htmlFor="content" className="mb-2 block w-10 font-bold text-gray-700">
            내용
          </label>
          <Textarea
            id="content"
            className="rounded border border-slate-300 bg-slate-50 px-3 py-2 leading-tight text-gray-700 shadow focus:outline-none"
            name="content"
            value={content}
            placeholder="Enter content here."
            onChange={handleContentChange}
            rows={13}
            required
            disabled={!isEditing}
            theme={{
              "base": "block w-full rounded-lg border text-sm"
            }}
          />
        </div>
        {boardDetail.fileSource && !isEditing && <a className='flex items-center gap-1' href={`${process.env.NEXT_PUBLIC_BASE_URL}/${boardDetail.fileSource}`} target="_blank" download><HiDownload className='inline-block' />첨부파일 다운로드</a>}
        {isEditing && (
          <FileUpload
            onImageUpload={handleImageUpload}
          />
        )}
        {renderButton()}
      </div>
    </div >
  );
};

export default BoardDetailPage;
