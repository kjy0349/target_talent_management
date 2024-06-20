package com.ssafy.s10p31s102be.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabAdminCreateDto {
    private String labName;

    private String labProfessor;

    private String researchDescription;

    private String researchType;

    private String researchResult;

    private String major;

    private Integer schoolId;

}
