package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.member.dto.response.MemberPreviewDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingFullDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import com.ssafy.s10p31s102be.project.dto.response.ProjectPreviewDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapPreviewDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePreviewDto {
    private Integer profileId;
    private String profileImage;
    private Map<String, String> columnDatas;
    private List<ProjectPreviewDto> projectsPreview;
    private List<TechmapPreviewDto> techmapsPreview;
    private List<CareerPreviewDto> careersPreview;
    private List<EducationPreviewDto> educationsPreview;
    private List<KeywordPreviewDto> keywordsPreview;
    private MemberPreviewDto memberPreview;
    private LocalDateTime createdAt;
    private EmployStatus employStatus;
    private LocalDateTime statusModifiedAt;
    private List<NetworkingFullDto> networking;
    private List<SpecializationDetailDto> specializationDetails;
    private List<UsagePlanReadDto> usagePlans;

    public static ProfilePreviewDto fromEntity(Profile profile) {
        return ProfilePreviewDto.builder()
                .profileId(profile.getId())
                .profileImage(profile.getProfileImage())
                .columnDatas(profile.getProfileColumnDatas().stream()
                        .filter(pcd -> pcd.getProfileColumn().getIsPreview())
                        .collect(Collectors.toMap(
                                pcd -> pcd.getProfileColumn().getName(), ProfileColumnData::getContent
                        )))
                .projectsPreview(profile.getProjectProfiles().stream().map(projectProfile -> {
                    return ProjectPreviewDto.fromEntity(projectProfile.getProject());
                }).toList())
                .careersPreview(profile.getCareers().stream().map(CareerPreviewDto::fromEntity).toList())
                .educationsPreview(profile.getEducations().stream().map(EducationPreviewDto::fromEntity).toList())
                .keywordsPreview(profile.getProfileKeywords().stream().map(profileKeyword -> {
                    return KeywordPreviewDto.fromEntity(profileKeyword.getKeyword());
                }).toList())
                .memberPreview(MemberPreviewDto.fromEntity(profile.getManager()))
                .createdAt(profile.getCreatedAt())
                .employStatus(profile.getEmployStatus())
                .statusModifiedAt(profile.getStatusModifiedAt())
                .usagePlans(profile.getUsagePlans().stream().map(UsagePlanReadDto::fromEntity).toList())
                .networking(profile.getNetworkings().stream().map((networkProfile) -> {
                    return NetworkingFullDto.fromEntity(networkProfile.getNetworking());
                }).toList())
                .techmapsPreview(profile.getTechmapProjectProfiles().stream().map(techmapProjectProfile -> { return TechmapPreviewDto.fromEntity(techmapProjectProfile.getTechmapProject());}).toList())
                .specializationDetails(profile.getSpecializations().stream().map(SpecializationDetailDto::fromEntity).toList())
                .build();
    }
}
