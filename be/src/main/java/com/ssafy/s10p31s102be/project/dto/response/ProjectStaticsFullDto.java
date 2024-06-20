package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.project.infra.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStaticsFullDto {
    private Map<Integer,Integer> projectMemberStatics;
    private Map<Boolean, Integer> isPrivateStatics;
    private Map<Integer,Integer> targetYearStatics;
    private Map<Integer,Integer> targetDepartmentStatics;
    private Map<Integer,Integer> targetJobRanksStatics;
    private Map<ProjectType, Integer> projectTypeStatics;
    private Map<String,Integer> targetCountryStatics;
}
