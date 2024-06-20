"use client";
import { useLocalAxios } from "@/app/api/axios";
import { FilesSummary } from "@/types/admin/File";
import axios, { AxiosError } from "axios";
import { useRef, useState } from "react";
import { FileInput, Label } from "flowbite-react";

interface ProfileImageUploadProps {
  onImageUpload?: (imageUrl: string) => void;
  className?: string;
  setImageUrl?: () => void;
}

const FileUpload = ({ onImageUpload, className, setImageUrl }: ProfileImageUploadProps) => {
  const [selectedFile, setSelectedFile] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const localAxios = useLocalAxios();
  const [isUploaded, setIsUploaded] = useState(false);

  const handleFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedFile(URL.createObjectURL(file));
      const formData = new FormData();
      formData.append("file", file);
      formData.append("type", "all")
      try {
        const result = await localAxios.post<FilesSummary>(
          `/common/files`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          },
        );
        if (result.status === 200) {
          setIsUploaded(true);
          alert("등록에 성공했습니다!");
        }
        if (onImageUpload) {
          onImageUpload(result.data.imgUrl);
        }
      }
      catch (error) {
        const axiosError = error as AxiosError;
        if (axiosError.response?.status === 404) {
          alert("올바른 파일 형식이 아닙니다.")
        }
        console.error(error);
      }
    }
  };

  return (
    <div className="mx-auto flex items-center justify-center">
      <Label
        htmlFor="dropzone-file"
        className="flex w-full cursor-pointer flex-col items-center justify-center rounded-lg border-2 border-dashed border-gray-300 bg-gray-50 hover:bg-gray-100 dark:border-gray-600 dark:bg-gray-700 dark:hover:border-gray-500 dark:hover:bg-gray-600"
      >
        {
          !isUploaded && (


            <div className="flex flex-col items-center justify-center p-10">
              <svg
                className="mb-4 size-8 text-gray-500 dark:text-gray-400"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 20 16"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"
                />
              </svg>
              <p className="text-xs text-gray-500 dark:text-gray-400">파일을 첨부해주세요.(용량 제한 10MB)</p>
            </div>
          )
        }
        <FileInput id="dropzone-file"
          className={!isUploaded ? "hidden" : ""}
          ref={fileInputRef}
          onChange={handleFileChange}
          disabled={isUploaded ? true : false}
        />
      </Label>
    </div>
  );
};

export default FileUpload;