package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;
import com.ssafy.s10p31s102be.admin.dto.response.MemberAdminSummaryDto;
import com.ssafy.s10p31s102be.admin.dto.response.member.DepartmentAdminSummaryDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberSummaryDto;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectListFullDto {
    private List<ProjectSummaryDto> projectSummaryDtos;
    private Integer totalPages = 0;
    private Long totalCount = 0L;
//    private ProjectStaticsFullDto projectStaticsFullDto;

    public static ProjectListFullDto fromEntity(List<ProjectSummaryDto> projectSummaryDtos){
        return ProjectListFullDto.builder()
                .projectSummaryDtos(projectSummaryDtos)
//                .projectStaticsFullDto(new ProjectStaticsFullDto())
                .build();
    }
}
