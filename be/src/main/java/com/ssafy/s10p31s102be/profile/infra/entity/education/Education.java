package com.ssafy.s10p31s102be.profile.infra.entity.education;

import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.GraduateStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue
    private Long id;

    @Builder
    public Education(Degree degree, String major,
                     LocalDateTime enteredAt, LocalDateTime graduatedAt, GraduateStatus graduateStatus, School school,
                     Lab lab, Profile profile, String labResearchType, String labResearchResult, String labResearchDescription) {
        this.degree = degree;
        this.major = major;
        this.enteredAt = enteredAt;
        this.graduatedAt = graduatedAt;
        this.graduateStatus = graduateStatus;
        this.school = school;
        this.lab = lab;
        this.profile = profile;

        this.labResearchType = labResearchType;
        this.labResearchResult = labResearchResult;
        this.labResearchDescription = labResearchDescription;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Degree degree;

    @Enumerated(EnumType.STRING)
    private GraduateStatus graduateStatus;

    private String major;

    private LocalDateTime enteredAt;

    private LocalDateTime graduatedAt;

    private String labResearchType;

    private String labResearchResult;

    private String labResearchDescription;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeywordEducation> keywordEducation = new ArrayList<>();

    public void update(Degree degree, String major, LocalDateTime enteredAt, LocalDateTime graduatedAt, GraduateStatus graduateStatus, School school, Lab lab, String labResearchType, String labResearchResult, String labResearchDescription) {
        this.degree = degree;
        this.major = major;
        this.enteredAt = enteredAt;
        this.graduatedAt = graduatedAt;
        this.graduateStatus = graduateStatus;
        this.school = school;
        this.lab = lab;

        this.labResearchType = labResearchType;
        this.labResearchResult = labResearchResult;
        this.labResearchDescription = labResearchDescription;
    }
}
