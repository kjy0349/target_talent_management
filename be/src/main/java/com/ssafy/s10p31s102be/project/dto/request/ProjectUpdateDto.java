package com.ssafy.s10p31s102be.project.dto.request;

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
public class ProjectUpdateDto {
    public String title;
    public Integer targetMemberCount;
    public Integer targetYear;

    public Boolean isPrivate;
    public ProjectType projectType;
    public String targetCountry;
    public Integer responsibleMemberId;

    public List<Integer> projectMembers;
    public List<Integer> projectProfiles;
    public List<Integer> targetJobRanks;
    public Integer targetDepartmentId;

    public String description;
}
