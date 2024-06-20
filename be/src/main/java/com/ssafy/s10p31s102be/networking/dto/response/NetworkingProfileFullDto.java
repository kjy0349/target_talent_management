package com.ssafy.s10p31s102be.networking.dto.response;


import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingProfileFullDto {
    public Integer profile_id;
    public String name;
    public String companyName;
    public String schoolName;
    public String major;
    public Degree degree;
    public String specialization;

    public static NetworkingProfileFullDto fromEntity(Profile profile) {
        Education lastEducation = null;
        if (!profile.getEducations().isEmpty()) {
            lastEducation = profile.getEducations().get(profile.getEducations().size() - 1);
        }
        Career lastCareer = null;
        if (!profile.getCareers().isEmpty()) {
            lastCareer = profile.getCareers().get(profile.getCareers().size() - 1);
        }
        return NetworkingProfileFullDto.builder()
                .major(lastEducation != null ? lastEducation.getMajor() : null)
                .degree(lastEducation != null ? lastEducation.getDegree() : null)
                .companyName(lastCareer != null ? lastCareer.getCompany().getName() : null)
                .schoolName(lastEducation != null ? lastEducation.getSchool().getSchoolName() : null)
                .profile_id(profile.getId())
                .name(
                        profile.getProfileColumnDatas()
                                .stream().filter(pd -> "name" .equals(pd.getProfileColumn().getName()))
                                .findFirst()
                                .map(ProfileColumnData::getContent)
                                .orElse(null)
                )
                .build();
    }
}
