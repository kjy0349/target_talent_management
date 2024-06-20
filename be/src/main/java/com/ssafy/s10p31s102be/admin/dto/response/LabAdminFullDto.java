package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
import com.ssafy.s10p31s102be.profile.infra.entity.education.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabAdminFullDto {
    private Integer id;

    private String labName;

    private String labProfessor;

    private String researchDescription;

    private String researchType;

    private String researchResult;

    private String major;

    private Integer schoolId;
    private String schoolName;

    public static LabAdminFullDto fromEntity(School s, Lab lab ){
        return LabAdminFullDto.builder()
                .id( lab.getLabId() )
                .labName(lab.getLabName())
                .labProfessor(lab.getLabProfessor())
                .major(lab.getMajor())
                .researchDescription(lab.getResearchDescription())
                .researchResult(lab.getResearchResult())
                .researchType(lab.getResearchType())
                .schoolId(s.getId())
                .schoolName(s.getSchoolName())
                .build();
    }
}
