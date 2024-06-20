package com.ssafy.s10p31s102be.profile.infra.entity.career;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.common.infra.entity.Keyword;
import com.ssafy.s10p31s102be.profile.dto.request.CareerCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import jakarta.persistence.*;
import java.util.ArrayList;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Career extends BaseTimeEntity {

    @Builder
    public Career(String jobRank, LocalDateTime startedAt, LocalDateTime endedAt, Long careerPeriodMonth, EmployType employType, String level, Boolean isManager, Boolean isCurrent, String dept, String role, String description, Profile profile, Company company, String country, String region) {
        this.jobRank = jobRank;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.careerPeriodMonth = careerPeriodMonth;
        this.employType = employType;
        this.level = level;
        this.isManager = isManager;
        this.isCurrent = isCurrent;
        this.dept = dept;
        this.role = role;
        this.description = description;
        this.profile = profile;
        this.company = company;
        this.country = country;
        this.region = region;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String jobRank;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Long careerPeriodMonth;

    private EmployType employType;

    private String level;

    private Boolean isManager;

    private Boolean isCurrent;

    private String dept;

    private String role;

    private String country;

    private String region;

    @Lob
    @Size(max = 1000)
    private String description;

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerKeyword> careerKeywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public void update(String jobRank, LocalDateTime startedAt, LocalDateTime endedAt, Long careerPeriodMonth, EmployType employType, String level, Boolean isManager, Boolean isCurrent, String dept, String role, String description, Company company, String country, String region) {
        this.jobRank = jobRank;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.careerPeriodMonth = careerPeriodMonth;
        this.employType = employType;
        this.level = level;
        this.isManager = isManager;
        this.isCurrent = isCurrent;
        this.dept = dept;
        this.role = role;
        this.description = description;
        this.company = company;
        this.country = country;
        this.region = region;
    }
}
