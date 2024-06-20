package com.ssafy.s10p31s102be.profile.infra.entity.education;

import com.ssafy.s10p31s102be.admin.dto.request.SchoolUpdateDto;
import jakarta.persistence.*;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private String country;

    @Builder
    public School(String schoolName, String country) {
        this.schoolName = schoolName;
        this.country = country;
    }

    @OneToMany(mappedBy = "school")
    private List<Lab> labs = new ArrayList<>();

    @OneToMany(mappedBy = "school")
    private List<Education> educations = new ArrayList<>();

    private boolean isDeleted = false;

    public void update(SchoolUpdateDto dto) {
        this.schoolName = dto.getSchoolName();
        this.country = dto.getCountry();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
