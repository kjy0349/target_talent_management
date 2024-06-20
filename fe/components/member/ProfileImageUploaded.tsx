"use client";
import { useLocalAxios } from "@/app/api/axios";
import { FilesSummary } from "@/types/admin/File";
import { Avatar } from "flowbite-react";
import { headers } from "next/headers";
import React, { useRef, useState, useEffect } from "react";
import Image from "next/image";

interface ProfileImageUploadProps {
  defaultAvatar?: string;
  onImageUpload?: (imageUrl: string) => void;
  className?: string;
  setImageUrl?: () => void;
}
const ProfileImageUpload: React.FC<ProfileImageUploadProps> = ({
  defaultAvatar,
  onImageUpload,
  className,
}) => {
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const localAxios = useLocalAxios();
  const handleImageClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  useEffect(() => {
    if (defaultAvatar) setSelectedImage(defaultAvatar);
  }, [defaultAvatar]);

  const handleImageChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("type", "image")

      try{
        const result = await localAxios.post<FilesSummary>(
          `/common/files`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          },
        );

        //   console.log(result);
        if (result.status === 200){
          if (onImageUpload) {
            setSelectedImage(URL.createObjectURL(file));
            onImageUpload(result.data.imgUrl);
          }
        }
      } catch{
      }
    }
  };

  return (
    <div className={`relative w-full ${className} justify-center`}>
      {/* <Avatar
        // img={selectedImage || defaultAvatar}
        img={(props) => (
          <Image
            alt="Profile"
            width="150"
            height="150"
            referrerPolicy="no-referrer"
            src={selectedImage ?? "/assets/picture/profile.png"}
            {...props}
          />
        )}
        alt="Profile"
        className="h-full w-full cursor-pointer rounded-md object-cover"
        size={"xl"}
        onClick={handleImageClick}
      /> */}
      <Image
        alt="Profile"
        // width="150"
        // height={"auto"}
        fill
        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
        referrerPolicy="no-referrer"
        src={selectedImage ?? "/assets/picture/profile.png"}
        onClick={handleImageClick}
        className="object-cover"
      />
      {/* <img
        src={selectedImage || defaultAvatar}
        alt="Profile"
        className="h-full w-full cursor-pointer rounded-md object-cover"
        onClick={handleImageClick}
      /> */}
      <input
        type="file"
        accept="image/*"
        ref={fileInputRef}
        style={{ display: "none" }}
        onChange={handleImageChange}
      />
    </div>
  );
};

export default ProfileImageUpload;
