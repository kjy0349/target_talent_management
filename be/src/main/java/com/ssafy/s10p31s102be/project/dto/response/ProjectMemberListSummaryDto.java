package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberListSummaryDto {
    private Integer memberId;
    private String name;
    public static ProjectMemberListSummaryDto fromEntity(Member member){
        ProjectMemberListSummaryDto projectMemberSummaryDto =
        ProjectMemberListSummaryDto.builder()
                .memberId(member.getId())
                .name( member.getName() )
                .build();
        return projectMemberSummaryDto;
    }
}
