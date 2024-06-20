package com.ssafy.s10p31s102be.techmap.infra.entity;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
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
public class TechmapProjectMember {
    public TechmapProjectMember(TechmapProject techmapProject, Member member) {
        this.techmapProject = techmapProject;
        this.member = member;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techmap_project_id")
    private TechmapProject techmapProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
