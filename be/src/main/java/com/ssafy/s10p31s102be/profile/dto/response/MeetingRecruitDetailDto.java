package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.member.dto.response.ExecutiveDetailDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberPreviewDto;
import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.member.infra.entity.Member;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.enums.InterestType;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingRecruitDetailDto {
    private Integer id;
    private MeetingType meetingType;
    private LocalDateTime meetAt;
    private Boolean isFace;
    private String place;
    private String country;
    private String description;
    private Boolean isMemberDirected;
    private String currentTask;
    private String leadershipDescription;
    private InterestType interestType;
    private String interestTech;
    private String question;
    private String etc;
    private String inChargeMemberName;
    private String targetDepartment;
    private String targetJobRank;
    private String participants;
    private String memberDepartment;
    private String memberName;
    private LocalDateTime createdAt;
    private Boolean isFavorite;

    public static MeetingRecruitDetailDto fromEntity(Meeting meeting) {
        return MeetingRecruitDetailDto.builder()
                .id(meeting.getId())
                .meetingType(meeting.getMeetingType())
                .meetAt(meeting.getMeetAt())
                .isFace(meeting.getIsFace())
                .place(meeting.getPlace())
                .country(meeting.getCountry())
                .description(meeting.getDescription())
                .isMemberDirected(meeting.getIsMemberDirected())
                .currentTask(meeting.getCurrentTask())
                .leadershipDescription(meeting.getLeadershipDescription())
                .interestType(meeting.getInterestType())
                .interestTech(meeting.getInterestTech())
                .question(meeting.getQuestion())
                .etc(meeting.getEtc())
                .inChargeMemberName(meeting.getInChargeMember())
                .targetDepartment(meeting.getTargetDepartmentName())
                .targetJobRank(meeting.getTargetJobRank())
                .participants(meeting.getParticipants())
                .memberName(meeting.getMemberName())
                .memberDepartment(meeting.getMemberDepartment())
                .createdAt(meeting.getCreatedAt())
                .isFavorite(meeting.getIsFavorite())
                .build();
    }
}
