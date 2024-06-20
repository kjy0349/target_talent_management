package com.ssafy.s10p31s102be.techmap.infra.entity;

import com.ssafy.s10p31s102be.profile.infra.entity.education.Lab;
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
public class TechmapProjectLab {
    public TechmapProjectLab(TechmapProject techmapProject, Lab lab) {
        this.techmapProject = techmapProject;
        this.lab = lab;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techmap_project_id")
    private TechmapProject techmapProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id")
    private Lab lab;
}
