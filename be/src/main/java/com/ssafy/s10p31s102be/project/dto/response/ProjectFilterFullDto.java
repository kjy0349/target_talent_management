package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;
import com.ssafy.s10p31s102be.admin.dto.response.member.DepartmentAdminSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFilterFullDto {
    private List<ProjectMemberListSummaryDto> memberList;
    private List<DepartmentAdminSummaryDto> departmentAdminSummaryDtos;
    private List<JobRankAdminSummaryDto> jobRankAdminSummaryDtos;
    private List<String> targetCountries;
    private List<Integer> targetYears;

    private ProjectStaticsFullDto projectStaticsFullDto;
}
