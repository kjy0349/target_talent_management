package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.profile.dto.response.ProfileExternalPreviewDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfilePreviewDto;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployStatus;
import com.ssafy.s10p31s102be.project.infra.entity.Project;
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
public class ProjectFullDto {
    private String title;
    private Integer targetMemberCount;
    private Integer targetYear;
    private Boolean isPrivate;
    private ProjectType projectType;
    private String targetCountry;
    private Integer responsibleMemberId;
    private List<ProjectMemberSummaryDto> projectMembers;
    private List<String> targetJobRanks;
    private List<Integer> targetJobRanksIds;
    private ProjectDepartmentSummaryDto projectDepartment;
    private List<ProfilePreviewDto> projectProfilesPreviewDtos;
    private Integer poolSize = 0;
    private int[] filteredArray;
    private String description;

    public static ProjectFullDto fromEntity(Project project, List<ProfilePreviewDto> projectProfilesPreviewDtos){
        ProjectFullDto projectFullDto = new ProjectFullDto();
        projectFullDto.title = project.getTitle();
        projectFullDto.targetMemberCount = project.getTargetMemberCount();
        projectFullDto.targetYear = project.getTargetYear();
        projectFullDto.isPrivate = project.getIsPrivate();
        projectFullDto.projectType = project.getProjectType();
        projectFullDto.targetCountry = project.getTargetCountry();
        projectFullDto.responsibleMemberId = project.getResponsibleMemberId();
        projectFullDto.targetJobRanksIds = project.getTargetJobRanks().stream().map( projectJobRank -> projectJobRank.getJobRank().getId() ).toList();
        projectFullDto.projectMembers = project.getProjectMembers().stream().map(ProjectMemberSummaryDto::fromEntity).toList();
        projectFullDto.targetJobRanks = project.getTargetJobRanks().stream().map( jr -> jr.getJobRank().getDescription()).toList();
        projectFullDto.projectDepartment = ProjectDepartmentSummaryDto.fromEntity( project.getTargetDepartment() );
        projectFullDto.description = project.getDescription();
        projectFullDto.filteredArray = new int[EmployStatus.values().length];
        projectFullDto.poolSize = project.getProjectProfiles().size();
        project.getProjectProfiles().stream().forEach( pp -> {
            if (pp.getProfile().getEmployStatus() != null) {
                projectFullDto.filteredArray[pp.getProfile().getEmployStatus().ordinal()]++;
            }
        });
        projectFullDto.projectProfilesPreviewDtos= projectProfilesPreviewDtos ;

        return projectFullDto;
    }
}

