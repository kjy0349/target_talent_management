package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabDetailDto {
    private Integer labId;

    private String labName;

    private String labProfessor;

    private String researchDescription;

    private String researchType;

    private String researchResult;

    public static LabDetailDto fromEntity(Lab lab) {
        if (lab == null) return null;
        return LabDetailDto.builder()
                .labId(lab.getLabId())
                .labName(lab.getLabName())
                .labProfessor(lab.getLabProfessor())
                .researchDescription(lab.getResearchDescription())
                .researchType(lab.getResearchType())
                .researchResult(lab.getResearchResult())
                .build();
    }
}
