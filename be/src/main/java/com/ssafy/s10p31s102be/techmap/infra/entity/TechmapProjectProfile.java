package com.ssafy.s10p31s102be.techmap.infra.entity;


import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TechmapProjectProfile {
    public TechmapProjectProfile(TechmapProject techmapProject, Profile profile) {
        this.techmapProject = techmapProject;
        this.profile = profile;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "techmap_project_id")
    private TechmapProject techmapProject;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
