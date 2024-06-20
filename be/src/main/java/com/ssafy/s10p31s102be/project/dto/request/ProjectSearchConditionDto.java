package com.ssafy.s10p31s102be.project.dto.request;

import com.ssafy.s10p31s102be.project.infra.entity.ProjectType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ProjectSearchConditionDto {
    private String countryName;
    private Integer departmentId; // 프로젝트 필터링
    private Integer targetYear;
    private String orderBy;
    private String keyword;
    //--추가 항목
    private Integer memberId;
    private Boolean isPrivate;
    private ProjectType projectType;
    private String targetCountry;
    private Integer targetJobRankId;

}
