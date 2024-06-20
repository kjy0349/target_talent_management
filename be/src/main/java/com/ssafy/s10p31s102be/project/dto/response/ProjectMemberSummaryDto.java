package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.project.infra.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberSummaryDto {
    private Integer memberId;
    private String name;
    private String profileImage;
    public static ProjectMemberSummaryDto fromEntity(ProjectMember projectMember){
        ProjectMemberSummaryDto projectMemberSummaryDto = new ProjectMemberSummaryDto();
        projectMemberSummaryDto.memberId=projectMember.getMember().getId();
        projectMemberSummaryDto.name = projectMember.getMember().getName();
        projectMemberSummaryDto.profileImage=projectMember.getMember().getProfileImage();
        return projectMemberSummaryDto;
    }
}
