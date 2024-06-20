package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.member.dto.response.JobRankDetailDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberPreviewDto;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
//import com.ssafy.s10p31s102be.project.dto.response.ProjectPreviewDto;
import com.ssafy.s10p31s102be.techmap.dto.response.TechmapPreviewDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ssafy.s10p31s102be.project.dto.response.ProjectPreviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDetailDto {
    private Integer profileId;
    private String profileImage;
    private EmployStatus employStatus;
    private List<ProfileColumnDataDto> columnDatas;
    private List<EmploymentHistoryReadDto> employmentHistories;
    private List<ProfileMemoReadDto> memos;
    private List<ProjectPreviewDto> projectsPreview;
    private List<TechmapPreviewDto> techmapsPreview;
    private List<CareerDetailDto> careersDetail;
    private List<EducationDetailDto> educationsDetail;
    private List<KeywordPreviewDto> keywordsPreview;
    private JobRankDetailDto jobRankDetail;
    private MemberPreviewDto memberPreview;
    private LocalDateTime createdAt;
    private LocalDateTime statusModifiedAt;
    private Boolean isPrivate;
    private Boolean isAllCompany;

    public static ProfileDetailDto fromEntity(Integer memberId, Profile profile) {
        List<EmploymentHistoryReadDto> employmentHistories = new ArrayList<>();

        employmentHistories.addAll(profile.getMeetings().stream().filter(Objects::nonNull).map(EmploymentHistoryReadDto::fromEntity).toList());
        employmentHistories.addAll(profile.getInterviews().stream().filter(Objects::nonNull).map(EmploymentHistoryReadDto::fromEntity).toList());
        employmentHistories.addAll(profile.getEmploymentHistories().stream().filter(Objects::nonNull).map(EmploymentHistoryReadDto::fromEntity).toList());

        return ProfileDetailDto.builder()
                .profileId(profile.getId())
                .profileImage(profile.getProfileImage())
                .employStatus(profile.getEmployStatus())
                .columnDatas(profile.getProfileColumnDatas().stream().filter(Objects::nonNull).map(ProfileColumnDataDto::fromEntity).toList())
                .employmentHistories(employmentHistories)
                .memos(profile.getProfileMemos().stream().filter(Objects::nonNull).filter(x -> !x.getIsPrivate() || x.getMember().getId().equals(memberId)).map(ProfileMemoReadDto::fromEntity).toList())
                .projectsPreview(profile.getProjectProfiles().stream().filter(Objects::nonNull).map(projectProfile -> {
                    return ProjectPreviewDto.fromEntity(projectProfile.getProject());
                }).toList())
                .techmapsPreview(profile.getTechmapProjectProfiles().stream().filter(Objects::nonNull).map(techmapProjectProfile -> {
                    return TechmapPreviewDto.fromEntity(techmapProjectProfile.getTechmapProject());
                }).toList())
                .careersDetail(profile.getCareers().stream().filter(Objects::nonNull).map(CareerDetailDto::fromEntity).toList())
                .educationsDetail(profile.getEducations().stream().filter(Objects::nonNull).map(EducationDetailDto::fromEntity).toList())
                .keywordsPreview(profile.getProfileKeywords().stream().filter(Objects::nonNull).map(profileKeyword -> {
                    return KeywordPreviewDto.fromEntity(profileKeyword.getKeyword());
                }).toList())
                .memberPreview(MemberPreviewDto.fromEntity(profile.getManager()))
                .jobRankDetail(profile.getTargetJobRank() != null ? JobRankDetailDto.fromEntity(profile.getTargetJobRank()) : null)
                .createdAt(profile.getCreatedAt())
                .statusModifiedAt(profile.getStatusModifiedAt())
                .isPrivate(profile.getIsPrivate())
                .isAllCompany(profile.getIsAllCompany())
                .build();
    }
}
