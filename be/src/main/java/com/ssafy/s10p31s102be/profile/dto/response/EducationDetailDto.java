package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EducationDetailDto {
    private Long id;

    private Degree degree;

    private String major;

    private LocalDateTime enteredAt;

    private LocalDateTime graduatedAt;

    private SchoolDetailDto school;

    private LabDetailDto lab;

    private String labResearchType;

    private String labResearchResult;

    private String labResearchDescription;

    private List<String> keywords;

    public static EducationDetailDto fromEntity(Education education) {
        if (education == null) return null;
        return EducationDetailDto.builder()
                .id(education.getId())
                .degree(education.getDegree())
                .major(education.getMajor())
                .enteredAt(education.getEnteredAt())
                .graduatedAt(education.getGraduatedAt())
                .school(SchoolDetailDto.fromEntity(education.getSchool()))
                .lab(LabDetailDto.fromEntity(education.getLab()))
                .labResearchType(education.getLabResearchType())
                .labResearchResult(education.getLabResearchResult())
                .labResearchDescription(education.getLabResearchDescription())
                .keywords(education.getKeywordEducation().stream().map(keywordEducation -> keywordEducation.getKeyword().getData()).toList())
                .build();
    }
}
