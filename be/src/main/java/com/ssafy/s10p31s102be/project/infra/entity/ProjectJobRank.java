package com.ssafy.s10p31s102be.project.infra.entity;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ProjectJobRank {
    public ProjectJobRank( Project project, JobRank jobRank ){
        this.project = project;
        this.jobRank = jobRank;
    }
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobRank jobRank;
}
