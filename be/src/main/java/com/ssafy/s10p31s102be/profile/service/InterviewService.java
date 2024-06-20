package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewCreateDto;
import com.ssafy.s10p31s102be.profile.dto.request.InterviewUpdateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Interview;
import java.util.List;

public interface InterviewService {
    Interview create(UserDetailsImpl userDetails, Integer profileId, InterviewCreateDto dto);

    Interview update(UserDetailsImpl userDetails, Integer interviewId, InterviewCreateDto dto);

    Interview updateFavorite(UserDetailsImpl userDetails, Integer interviewId, Boolean isFavorite);

    List<Interview> findAllInterviewByProfileId(UserDetailsImpl userDetails, Integer profileId);

    void delete(UserDetailsImpl userDetails, Integer interviewId);
}
