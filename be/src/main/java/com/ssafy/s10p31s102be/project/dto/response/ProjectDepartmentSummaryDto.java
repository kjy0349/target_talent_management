package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Department;
import lombok.Data;

@Data
public class ProjectDepartmentSummaryDto {
    private Integer departmentId;
    private String name;

    public static ProjectDepartmentSummaryDto fromEntity(Department department){
        if( department == null ){
            ProjectDepartmentSummaryDto projectDepartmentSummaryDto = new ProjectDepartmentSummaryDto();
            projectDepartmentSummaryDto.departmentId = null;
            projectDepartmentSummaryDto.name = "없음";
            return  projectDepartmentSummaryDto;

        }
        ProjectDepartmentSummaryDto projectDepartmentSummaryDto = new ProjectDepartmentSummaryDto();
        projectDepartmentSummaryDto.departmentId = department.getId();
        projectDepartmentSummaryDto.name = department.getName();
        return  projectDepartmentSummaryDto;
    }
}
