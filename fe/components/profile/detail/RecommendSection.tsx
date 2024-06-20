import { useEffect, useMemo, useState } from "react";
import { useLocalAxios } from "@/app/api/axios";
import { Card } from "flowbite-react";
import { ProfileDetail } from "@/types/talent/ProfileDetailResponse";
import Image from "next/image";
import ProfileCardProp from "@/types/talent/ProfileCardProp";
import Link from "next/link";

interface RecommendSectionProps {
  profileId: number;
}

const RecommendSection = (props: RecommendSectionProps) => {
  const localAxios = useLocalAxios();
  const [recommendedProfiles, setRecommendedProfiles] = useState<ProfileCardProp[]>([]);

  useEffect(() => {
    if (props.profileId) {
      fetchData();
    }
  }, [props.profileId]);

  const fetchData = async () => {
    const response = await localAxios.get(`/profile/${props.profileId}/recommend`);
    setRecommendedProfiles(response.data);
  };

  return (
    <Card className="bg-gray-50">
      <div className="flex flex-col justify-between">
        <div className="w-full flex justify-between items-center border-b-2 pb-2 mb-2">
          <p className="text-lg font-bold whitespace-nowrap">비슷한 후보자 추천</p>
        </div>
        <div className="flex flex-col gap-2 max-h-[400px] overflow-y-auto">
          {
            recommendedProfiles.length <= 0 &&
            <div className="font-bold mx-auto pt-4 pb-2">비슷한 후보자가 존재하지 않습니다.</div>
          }
          {
            recommendedProfiles.map(profile => (
              <Link href={"/talent/" + profile.profileId} key={profile.profileId} className="flex p-2 border-2 border-gray-200 bg-white hover:bg-gray-100">
                <img src={profile.profileImage ? process.env.NEXT_PUBLIC_BASE_URL + "/" + profile.profileImage : "/assets/picture/profile.png"} className="w-20 h-24" />
                <div className="flex-1 p-2 flex flex-col justify-between">
                  <div className="font-bold text-xl">{profile.columnDatas["name"]}</div>
                  {
                    profile.careersPreview.length > 0 &&
                    <div className="font-bold text-gray-700 tracking-tighter">
                      <span>{profile.careersPreview[0].companyName}, </span>
                      <span>{profile.careersPreview[0].role}</span>
                    </div>
                  }
                  {
                    profile.educationsPreview.length > 0 &&
                    <div className="font-bold text-gray-700 tracking-tighter">
                      <span>{profile.educationsPreview[0].schoolName}, </span>
                      <span>{profile.educationsPreview[0].major}</span>
                    </div>
                  }
                </div>
              </Link>
            ))
          }
        </div>
      </div>
    </Card>
  );
}

export default RecommendSection;