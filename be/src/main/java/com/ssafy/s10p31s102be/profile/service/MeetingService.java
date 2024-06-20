package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingCreateRequestDto;
import com.ssafy.s10p31s102be.profile.dto.request.MeetingUpdateRequestDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Meeting;
import java.util.List;

public interface MeetingService {
    Meeting create(UserDetailsImpl userDetails, Integer profileId, MeetingCreateRequestDto dto);

    List<Meeting> readMeetings(UserDetailsImpl userDetails, Integer profileId);

    Meeting findById(Integer meetingId);

    Meeting update(UserDetailsImpl userDetails, Integer meetingId, MeetingCreateRequestDto dto);

    Meeting updateFavorite(UserDetailsImpl userDetails, Integer meetingId, Boolean isFavorite);

    void delete(UserDetailsImpl userDetails, Integer meetingId);
}
