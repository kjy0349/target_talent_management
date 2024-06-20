package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.project.infra.entity.Project;
import com.ssafy.s10p31s102be.project.infra.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSummaryDto {
    private Integer id;
    private String title;
    private List<String> targetJobRanks;
    private String responsibleMemberName;
    private Integer targetYear;
    private Integer targetMemberCount;
    private ProjectType projectType;
    private Boolean isBookMarked = false;
    private String description;
    private String departmentName;
    private Boolean isPrivate;
    private LocalDateTime createAt;
    private Integer poolSize;

    public static ProjectSummaryDto fromEntity(Project project) {
        return ProjectSummaryDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .targetMemberCount( project.getTargetMemberCount() )
                .targetJobRanks( project.getTargetJobRanks().stream().map( j -> j.getJobRank().getDescription()).toList())
                .responsibleMemberName(project.getProjectMembers().stream().filter( pm -> pm.getMember().getId().equals(project.getResponsibleMemberId())).findFirst().isPresent()?
                        project.getProjectMembers().stream().filter( pm -> pm.getMember().getId().equals(project.getResponsibleMemberId())).findFirst().get().getMember().getName()
                        :
                        "해당 없음")
                .poolSize( project.getProjectProfiles().size() )
                .targetYear(project.getTargetYear())
                .projectType(project.getProjectType())
                .description(project.getDescription())
                .departmentName(project.getTargetDepartment().getName())
                .createAt(project.getCreatedAt())
                .isPrivate( project.getIsPrivate())
                .build();
    }
}
