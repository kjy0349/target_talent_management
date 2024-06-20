package com.ssafy.s10p31s102be.techmap.infra.entity;


import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Techmap  extends BaseTimeEntity {
    @Builder
    public Techmap(String description, Integer targetYear, LocalDateTime dueDate, Boolean isAlarmSend, Member member) {
        this.description = description;
        this.targetYear = targetYear;
        this.dueDate = dueDate;
        this.isAlarmSend = isAlarmSend;
        this.member = member;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    private Integer targetYear;

    private LocalDateTime dueDate;

    private Boolean isAlarmSend;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany( mappedBy = "techmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapKeyword> TechmapKeywords = new ArrayList<>();

    @OneToMany( mappedBy = "techmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechmapProject> techmapProjects = new ArrayList<>();

    @OneToMany( mappedBy = "techmap", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<TechmapDepartment> techmapDepartments = new ArrayList<>();

    public void updatetechmap(LocalDateTime dueDate, Integer targetYear, String description) {
        this.dueDate = dueDate;
        this.targetYear = targetYear;
        this.description = description;
    }
}
