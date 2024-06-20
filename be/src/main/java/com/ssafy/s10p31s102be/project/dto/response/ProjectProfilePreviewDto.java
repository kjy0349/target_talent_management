package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.member.dto.response.MemberPreviewDto;
import com.ssafy.s10p31s102be.networking.dto.response.NetworkingExecutivePreview;
import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
import com.ssafy.s10p31s102be.profile.dto.response.CareerPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.EducationPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.KeywordPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileColumnData;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectProfilePreviewDto {
    private Integer profileId;
    private String profileImage;
    private Map<String, String> columnDatas;

    private List<CareerPreviewDto> careersPreview;
    private List<EducationPreviewDto> educationsPreview;
    private List<KeywordPreviewDto> keywordsPreview;
    private MemberPreviewDto memberPreview;
    private EmployStatus employstatus;
    private LocalDateTime createdAt;
    private List<NetworkingExecutivePreview> networkingExecutivePreviews;


    public static ProjectProfilePreviewDto fromEntity(Profile profile) {
        return ProjectProfilePreviewDto.builder()
                .profileId(profile.getId())
                .profileImage(profile.getProfileImage())
                .employstatus( profile.getEmployStatus() )
                .columnDatas(profile.getProfileColumnDatas().stream()
                        .filter(pcd -> pcd.getProfileColumn().getIsPreview())
                        .collect(Collectors.toMap(
                                pcd -> pcd.getProfileColumn().getName(), ProfileColumnData::getContent
                        )))
                .careersPreview(profile.getCareers().stream().map(CareerPreviewDto::fromEntity).toList())
                .educationsPreview(profile.getEducations().stream().map(EducationPreviewDto::fromEntity).toList())
                .keywordsPreview(profile.getProfileKeywords().stream().map(profileKeyword -> {
                    return KeywordPreviewDto.fromEntity(profileKeyword.getKeyword());
                }).toList())
                .memberPreview(MemberPreviewDto.fromEntity(profile.getManager()))
                .createdAt(profile.getCreatedAt())
                .networkingExecutivePreviews( profile.getNetworkings().stream().map( NetworkingProfile::getNetworking).map(NetworkingExecutivePreview::fromEntity).toList())
                .build();
    }
}
