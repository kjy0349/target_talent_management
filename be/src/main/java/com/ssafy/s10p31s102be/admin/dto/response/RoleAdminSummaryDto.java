package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleAdminSummaryDto {
    public Integer roleId;
    public String description;
    public static RoleAdminSummaryDto fromEntity(Role role ){
        return RoleAdminSummaryDto.builder()
                .roleId((role.getId()))
                .description(role.getDescription())
                .build();
    }
}
