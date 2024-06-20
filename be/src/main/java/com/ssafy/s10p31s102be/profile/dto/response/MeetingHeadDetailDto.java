package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.member.dto.response.ExecutiveDetailDto;
import com.ssafy.s10p31s102be.member.dto.response.MemberPreviewDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingHeadDetailDto {
    private Integer id;
    private MeetingType meetingType;
    private LocalDateTime meetAt;
    private Boolean isFace;
    private String place;
    private String country;
    private String description;
    private Boolean isNetworking;
    private String participants;
    private Boolean isFavorite;
    private String memberDepartment;
    private String memberName;
    private LocalDateTime createdAt;

    public static MeetingHeadDetailDto fromEntity(Meeting meeting) {
        return MeetingHeadDetailDto.builder()
                .id(meeting.getId())
                .meetingType(meeting.getMeetingType())
                .meetAt(meeting.getMeetAt())
                .isFace(meeting.getIsFace())
                .place(meeting.getPlace())
                .country(meeting.getCountry())
                .description(meeting.getDescription())
                .isNetworking(meeting.getIsNetworking())
                .participants(meeting.getParticipants())
                .isFavorite(meeting.getIsFavorite())
                .memberDepartment(meeting.getMemberDepartment())
                .memberName(meeting.getMemberName())
                .createdAt(meeting.getCreatedAt())
                .build();
    }
}
