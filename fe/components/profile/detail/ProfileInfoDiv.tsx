import ProfileCareerSection from "./ProfileCareerSection";
import ProfileEducationSection from "./ProfileEducationSection";
import ProfileDetailSection from "./ProfileDetailSection";
import {Career, Education, ProfileColumn, ProfileDetail} from "@/types/talent/ProfileDetailResponse";
import { useLocalAxios } from "@/app/api/axios";

export default function ProfileInfoDiv({
  profile, params, reload
}: {
  profile: ProfileDetail,
  params: { slug: string },
    reload: () => void
}) {
  const localAxios = useLocalAxios();

  const handleCareerUpdates = (updatedCareer: Career) => {
    const { id, ...CareerData } = updatedCareer;
    localAxios.put(`/profile/${params.slug}/career/${id}`, CareerData);
  };

  const handleEducationUpdates = (updatedEducation: Education) => {
    const { id, ...EducationData } = updatedEducation;
    localAxios.put(`/profile/${params.slug}/education/${id}`, EducationData);
  }

  const handleReload = () => {
      reload();
  }

  return (
    <div className="bg-white">
      <ProfileCareerSection
        profileId={profile.profileId}
        careers={profile.careersDetail}
        onUpdate={handleCareerUpdates}
        reload={handleReload}
      />
      <ProfileEducationSection
        profileId={profile.profileId}
        educations={profile.educationsDetail}
        onUpdate={handleEducationUpdates}
        reload={handleReload}
      />
      <ProfileDetailSection
        profile={profile}
        params={params}
      />
    </div>
  )
}
