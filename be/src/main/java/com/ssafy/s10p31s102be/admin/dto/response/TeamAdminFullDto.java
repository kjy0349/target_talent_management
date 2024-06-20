package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAdminFullDto {
    private Integer teamId;

    private String name;

    private String description;

    private String departmentName;

    private Integer departmentId;

    public static TeamAdminFullDto fromEntity( Team team ){
        return TeamAdminFullDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
//                .departmentId( team.getDepartment() != null ? team.getDepartment().getId() : null )
//                .departmentName( team.getDepartment() != null ? team.getDepartment().getName() : null )
                .build();
    }
}