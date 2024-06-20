package com.ssafy.s10p31s102be.admin.dto.request;

import com.ssafy.s10p31s102be.project.infra.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAdminUpdateDto {
    private String title;
    private Integer targetMemberCount;
    private Integer targetYear;

    private Boolean isPrivate;
    private ProjectType projectType;
    private String targetCountry;
    private Integer responsibleMemberId;

    private List<Integer> projectMembers;
    private List<Integer> projectProfiles;
    private List<Integer> targetJobRanks;
    private Integer targetDepartmentId;
}
