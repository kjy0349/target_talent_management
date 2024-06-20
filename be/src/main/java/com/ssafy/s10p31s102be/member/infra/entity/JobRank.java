package com.ssafy.s10p31s102be.member.infra.entity;

import com.ssafy.s10p31s102be.admin.dto.request.JobRankUpdateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
public class JobRank {
    @Builder
    public JobRank(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String description;
    @Setter
    private Boolean isDeleted = false;

    public void update(JobRankUpdateDto dto) {
        this.description = dto.getDescription();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
