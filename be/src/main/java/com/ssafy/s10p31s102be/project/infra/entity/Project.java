package com.ssafy.s10p31s102be.project.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.ProjectAdminUpdateDto;
import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.project.dto.request.ProjectUpdateDto;
import jakarta.persistence.*;

import java.util.ArrayList;

import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Project extends BaseTimeEntity {

    @Builder
    public Project(Integer id, String title, Integer targetMemberCount, Integer targetYear, Boolean isPrivate, ProjectType projectType, String targetCountry, Integer poolSize, Integer responsibleMemberId, Department targetDepartment, String description) {
        this.id = id;
        this.title = title;
        this.targetMemberCount = targetMemberCount;
        this.targetYear = targetYear;
        this.isPrivate = isPrivate;
        this.projectType = projectType;
        this.targetCountry = targetCountry;
        this.responsibleMemberId = responsibleMemberId;
        this.targetDepartment = targetDepartment;
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private Integer targetMemberCount;

    private Integer targetYear;

    private Boolean isPrivate;

    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    private String targetCountry;

    private String description;

    //TODO 이거 ONETOONE할지 ID만 넣을지 상의할 것.
    private Integer responsibleMemberId;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectProfile> projectProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectJobRank> targetJobRanks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectBookmark> projectBookMarks = new ArrayList<>();

    @ManyToOne
    @JoinColumn( name = "target_department_id")
    private Department targetDepartment;

    public void update(ProjectUpdateDto dto) {
        this.title = dto.getTitle();
        this.targetMemberCount = dto.getTargetMemberCount();
        this.targetYear = dto.getTargetYear();
        this.isPrivate = dto.getIsPrivate();
        this.projectType = dto.getProjectType();
        this.targetCountry = dto.getTargetCountry();
        this.description = dto.getDescription();
        this.responsibleMemberId = dto.getResponsibleMemberId();
    }
    public void update(ProjectAdminUpdateDto dto ){
        this.title = dto.getTitle();
        this.targetMemberCount = dto.getTargetMemberCount();
        this.targetYear = dto.getTargetYear();
        this.isPrivate = dto.getIsPrivate();
        this.projectType = dto.getProjectType();
        this.targetCountry = dto.getTargetCountry();
        this.responsibleMemberId = dto.getResponsibleMemberId();
    }

    public void updateTitle( String title ){
        this.title = title;
    }

    public void updateProjectProfiles(List<ProjectProfile> projectProfiles) {
        this.projectProfiles = projectProfiles;
    }

    public void updateProjectMembers(List<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public void updateTargetJobRanks(List<ProjectJobRank> targetJobRanks) {
        this.targetJobRanks = targetJobRanks;
    }

    public void updateTargetDepartment(Department targetDepartment) {
        this.targetDepartment = targetDepartment;
    }

    public void updateIsPrivate( Boolean bool ){
        this.isPrivate = bool;
    }

    public Integer getPoolSize() { return this.getProjectProfiles().size(); }
}
