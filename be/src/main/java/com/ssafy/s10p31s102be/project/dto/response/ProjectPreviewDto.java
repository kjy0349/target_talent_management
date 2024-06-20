package com.ssafy.s10p31s102be.project.dto.response;

import com.ssafy.s10p31s102be.project.infra.entity.Project;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectPreviewDto {
    private Integer projectId;
    private String title;
    private Integer targetYear;

    public static ProjectPreviewDto fromEntity(Project project) {
        if (project == null) return null;
        return ProjectPreviewDto.builder()
                .projectId(project.getId())
                .title(project.getTitle())
                .targetYear(project.getTargetYear())
                .build();
    }
}
