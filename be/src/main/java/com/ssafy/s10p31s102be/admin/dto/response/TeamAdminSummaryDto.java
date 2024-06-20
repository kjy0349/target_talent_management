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
public class TeamAdminSummaryDto {
    private Integer teamId;

    private String name;

    private String description;

    public static TeamAdminSummaryDto fromEntity( Team team ){
        return TeamAdminSummaryDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .build();
    }
}
