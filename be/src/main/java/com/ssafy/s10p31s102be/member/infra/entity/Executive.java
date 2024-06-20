package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Executive {

    @Builder
    public Executive(String name, String department, String jobRank, String email) {
        this.name = name;
        this.department = department;
        this.jobRank = jobRank;
        this.email = email;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String department;

    private String jobRank;

    private String email;

    @OneToMany(mappedBy = "executive", cascade = CascadeType.ALL)
    private List<Networking> networkings = new ArrayList<>();
}
