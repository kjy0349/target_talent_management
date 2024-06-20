package com.ssafy.s10p31s102be.profile.infra.entity.education;

import com.ssafy.s10p31s102be.admin.dto.request.LabAdminUpdateDto;
import com.ssafy.s10p31s102be.techmap.infra.entity.TechmapProjectLab;
import jakarta.persistence.*;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Lab {
    @Builder
    public Lab(String labName, String labProfessor, String researchDescription, String researchType, String researchResult, String major, School school) {
        this.labName = labName;
        this.labProfessor = labProfessor;
        this.researchDescription = researchDescription;
        this.researchType = researchType;
        this.researchResult = researchResult;
        this.major = major;
        this.school = school;
    }

    @Id
    @GeneratedValue
    private Integer labId;

    @Column(nullable = false)
    private String labName;

    private String labProfessor;

    private String researchDescription;

    private String researchType;

    private String researchResult;

    private String major;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToMany(mappedBy = "lab")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "lab")
    private final List<TechmapProjectLab> TechmapProjectLabs = new ArrayList<>();

    private boolean isDeleted = false;

    public void delete() {
        this.isDeleted = true;
    }

    public void update(LabAdminUpdateDto dto, School school) {
        this.labName = dto.getLabName();
        this.labProfessor = dto.getLabProfessor();
        this.researchDescription = dto.getResearchDescription();
        this.researchType = dto.getResearchType();
        this.researchResult = dto.getResearchResult();
        this.major = dto.getMajor();
        this.school = school;
    }

    public void updateSchool(School school) {
        this.school = school;
    }
}
