package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.InterestType;
import com.ssafy.s10p31s102be.profile.infra.enums.MeetingType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingCreateRequestDto {
    private MeetingType meetingType;
    private String inChargeMember;
    private String targetDepartment;
    private String targetJobRank;
    private LocalDateTime meetAt;
    private Boolean isFace;
    private String description;
    private String place;
    private Boolean isMemberDirected;
    private String currentTask;
    private String leadershipDescription;
    private InterestType interestType;
    private String interestTech;
    private String interestDescription;
    private String question;
    private String etc;
    private String country;
    private Boolean isNetworking;
    private String participants;
}
