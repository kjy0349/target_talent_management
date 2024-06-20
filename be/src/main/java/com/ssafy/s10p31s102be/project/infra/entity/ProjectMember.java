package com.ssafy.s10p31s102be.project.infra.entity;

import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProjectMember {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    public ProjectMember( Project project, Member member ){
        this.project = project;
        this.member = member;
    }
}
