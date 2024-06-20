package com.ssafy.s10p31s102be.profile.infra.entity.community;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UsagePlan extends BaseTimeEntity {

    @Builder
    public UsagePlan(String mainDescription, String detailDescription, LocalDateTime targetEmployDate, Profile profile, String jobRank, String targetDepartmentName, Member member) {
        this.mainDescription = mainDescription;
        this.detailDescription = detailDescription;
        this.targetEmployDate = targetEmployDate;
        this.profile = profile;
        this.jobRank = jobRank;
        this.targetDepartmentName = targetDepartmentName;
        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();

        this.isFavorite = Boolean.FALSE;
        this.modifiedAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String mainDescription;

    @Lob
    @Size(max = 1000)
    private String detailDescription;

    private String jobRank;

    private String targetDepartmentName;

    private LocalDateTime targetEmployDate;

    private Boolean isFavorite;

    private String memberDepartment;

    private String memberName;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public void update(String mainDescription, String detailDescription, LocalDateTime targetEmploymentDate, String jobRank, String targetDepartmentName) {
        this.mainDescription = mainDescription;
        this.detailDescription = detailDescription;
        this.targetEmployDate = targetEmploymentDate;
        this.jobRank = jobRank;
        this.targetDepartmentName = targetDepartmentName;

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
