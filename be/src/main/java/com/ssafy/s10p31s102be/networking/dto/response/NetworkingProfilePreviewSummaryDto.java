package com.ssafy.s10p31s102be.networking.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkingProfilePreviewSummaryDto {
    private Integer profileId;
    private String profileImage;
    private String name; //DC.
    private String column1; // DC.
    private String companyName; //Career.company.name
    private String jobRank; // Career.jobrank
    private LocalDateTime careerStartedAt; // Career.startedAt
    private LocalDateTime careerEndedAt; // Career.endedAt
    private String schoolName; // School.name
    private String schoolMajor; // School.major
    private String schoolDegree; // School.degree enum
    private String techDetailName; // TechDetail.techDetailName
    private LocalDateTime createAt;

    public static NetworkingProfilePreviewSummaryDto fromEntity(Profile profile) {
        profile.getEducations().forEach( education -> System.out.println( profile.getId()+"::"+ education.getSchool().getSchoolName()));
        Career recentCareer = profile.getCareers() == null ? null : profile.getCareers().size() == 0 ? null :profile.getCareers().get( profile.getCareers().size() - 1);
        Education recentEducation = profile.getEducations() == null ? null : profile.getEducations().size() == 0 ? null :profile.getEducations().get(profile.getEducations().size()-1);

        return NetworkingProfilePreviewSummaryDto.builder()
                .profileId(profile.getId())
                .profileImage(profile.getProfileImage())
                .name( profile.getProfileColumnDatas()
                        .stream().filter( pcd ->
                                pcd.getProfileColumn().getName().equals("name"))
                        .toList().stream().findAny().orElseGet( () -> new ProfileColumnData() ).getContent() )
                .column1( profile.getProfileColumnDatas()
                        .stream().filter( pcd ->
                                pcd.getProfileColumn().getName().equals("column1"))
                        .toList().stream().findAny().orElseGet(()-> new ProfileColumnData() ).getContent())
                .companyName( recentCareer == null ? null : recentCareer.getCompany().getName())
                .jobRank( profile.getTargetJobRank().getDescription() )
                .careerStartedAt(recentCareer == null ? null :recentCareer.getStartedAt())
                .careerEndedAt(recentCareer == null ? null :recentCareer.getEndedAt())
                .schoolName( recentEducation == null ? null : recentEducation.getSchool() == null ? null :recentEducation.getSchool().getSchoolName())
                .schoolMajor(recentEducation == null ? null :recentEducation.getMajor())
                .schoolDegree(recentEducation == null ? null: recentEducation.getDegree() == null ? null :recentEducation.getDegree().name())
                .techDetailName( profile.getKeyword() != null ? profile.getKeyword().getData(): null )
                .build();

    }
}