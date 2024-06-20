package com.ssafy.s10p31s102be.techmap.infra.entity;

import com.ssafy.s10p31s102be.profile.infra.entity.career.Company;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TechmapProjectCompany {
    public TechmapProjectCompany(TechmapProject techmapProject, Company company) {
        this.techmapProject = techmapProject;
        this.company = company;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techmap_project_id")
    private TechmapProject techmapProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
