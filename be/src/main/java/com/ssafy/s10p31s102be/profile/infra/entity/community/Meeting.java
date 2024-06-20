package com.ssafy.s10p31s102be.profile.infra.entity.community;

import com.ssafy.s10p31s102be.common.infra.entity.BaseTimeEntity;
import com.ssafy.s10p31s102be.member.infra.entity.Department;
import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingCreateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingUpdateRequestDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import com.ssafy.s10p31s102be.profile.infra.enums.InterestType;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Meeting extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public Meeting(MeetingType meetingType, LocalDateTime meetAt, Boolean isFace, String description, String place,
                   Boolean isMemberDirected, String currentTask, String leadershipDescription, InterestType interestType, String interestTech,
                   String interestDescription, String question, String etc, String country, String departmentName, String inChargeMember, String targetJobRank, String participants, Profile profile, Boolean isNetworking, Member member) {
        this.meetingType = meetingType;
        this.meetAt = meetAt;
        this.isFace = isFace;
        this.description = description;
        this.place = place;
        this.isMemberDirected = isMemberDirected;
        this.currentTask = currentTask;
        this.leadershipDescription = leadershipDescription;
        this.interestType = interestType;
        this.interestTech = interestTech;
        this.interestDescription = interestDescription;
        this.question = question;
        this.etc = etc;
        this.country = country;
        this.targetDepartmentName = departmentName;
        this.inChargeMember = inChargeMember;
        this.targetJobRank = targetJobRank;
        this.participants = participants;

        this.profile = profile;
        this.isNetworking = isNetworking;

        this.memberDepartment = member.getDepartment().getName();
        this.memberName = member.getName();

        this.isFavorite = Boolean.FALSE;
        this.modifiedAt = LocalDateTime.now();
    }

    public void update(MeetingCreateRequestDto dto) {
        this.meetAt = dto.getMeetAt();
        this.isFace = dto.getIsFace();
        this.description = dto.getDescription();
        this.place = dto.getPlace();
        this.isMemberDirected = dto.getIsMemberDirected();
        this.currentTask = dto.getCurrentTask();
        this.leadershipDescription = dto.getLeadershipDescription();
        this.interestType = dto.getInterestType();
        this.interestTech = dto.getInterestTech();
        this.interestDescription = dto.getInterestDescription();
        this.question = dto.getQuestion();
        this.etc = dto.getEtc();
        this.country = dto.getCountry();
        this.isNetworking = dto.getIsNetworking();
        this.targetDepartmentName = dto.getTargetDepartment();
        this.inChargeMember = dto.getInChargeMember();
        this.targetJobRank = dto.getTargetJobRank();
        this.participants = dto.getParticipants();

        this.modifiedAt = LocalDateTime.now();
    }

    public void updateFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    private LocalDateTime meetAt;

    private Boolean isFace;

    @Lob
    @Size(max = 1000)
    private String description;

    private String place;

    private Boolean isMemberDirected;

    private String currentTask;

    private String leadershipDescription;

    @Enumerated(EnumType.STRING)
    private InterestType interestType;

    private String interestTech;

    private String interestDescription;

    private String question;

    private String etc;

    private String country;

    private Boolean isNetworking;

    private String targetDepartmentName;

    private String inChargeMember;

    private String targetJobRank;

    private Boolean isFavorite;

    private String memberDepartment;

    private String memberName;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String participants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
