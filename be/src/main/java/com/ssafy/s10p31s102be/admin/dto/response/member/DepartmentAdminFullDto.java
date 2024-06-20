package com.ssafy.s10p31s102be.admin.dto.response.member;

import com.ssafy.s10p31s102be.member.infra.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAdminFullDto {
    private Integer departmentId;
    private String name;
    private String description;
    private String managerName;
    private Integer managerId;
    public static DepartmentAdminFullDto fromEntity( Department department ){
        DepartmentAdminFullDto dto = DepartmentAdminFullDto.builder()
                .departmentId(department.getId())
                .managerName(department.getManager() == null ? "관리자 없음":department.getManager().getName())
                .name( department.getName() )
                .description(department.getDescription())
                .managerId(department.getManager().getId())
                .build();
        return dto;
    }
}
