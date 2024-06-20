package com.ssafy.s10p31s102be.profile.infra.entity.community;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewUpdateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Interview extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Interview(String interviewDegree, LocalDateTime meetDate, InterviewType interviewType, String place, String description, Profile profile, Member member) {
        this.interviewDegree = interviewDegree;
        this.meetDate = meetDate;
        this.interviewType = interviewType;
        this.place = place;
        this.description = description;
        this.profile = profile;
        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();

        this.isFavorite = Boolean.FALSE;
        this.modifiedAt = LocalDateTime.now();
    }

    private String interviewDegree;

    private LocalDateTime meetDate;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    private String place;

    private Boolean isFavorite;

    private String memberDepartment;

    private String memberName;

    @Lob
    @Size(max = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private final List<InterviewResult> interviewResults = new ArrayList<>();

    public void update(InterviewCreateDto dto) {
        this.interviewDegree = dto.getInterviewDegree();
        this.meetDate = dto.getMeetDate();
        this.interviewType = dto.getInterviewType();
        this.place = dto.getPlace();
        this.description = dto.getDescription();

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
