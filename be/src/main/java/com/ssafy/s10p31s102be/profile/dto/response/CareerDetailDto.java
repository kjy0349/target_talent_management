package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CareerDetailDto {
    private Long id;

    private String companyName;

    private String jobRank;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private EmployType employType;

    private String level;

    private Boolean isManager;

    private String region;

    private Boolean isCurrent;

    private String dept;

    private String role;

    private String description;

    private String country;

    private Long careerPeriodMonth;

    private List<String> keywords;

    @Builder
    public static CareerDetailDto fromEntity(Career career) {
        if (career == null) return null;
        return CareerDetailDto.builder()
                .id(career.getId())
                .companyName(career.getCompany().getName())
                .jobRank(career.getJobRank())
                .startedAt(career.getStartedAt())
                .endedAt(career.getEndedAt())
                .employType(career.getEmployType())
                .level(career.getLevel())
                .isManager(career.getIsManager())
                .region(career.getRegion())
                .isCurrent(career.getIsCurrent())
                .dept(career.getDept())
                .role(career.getRole())
                .description(career.getDescription())
                .country(career.getCountry())
                .careerPeriodMonth(career.getCareerPeriodMonth())
                .keywords(career.getCareerKeywords().stream().map(careerKeyword -> careerKeyword.getKeyword().getData()).toList())
                .build();
    }
}
