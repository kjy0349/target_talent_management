package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.EducationCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;

public interface EducationService {
    Education create(UserDetailsImpl userDetails, Integer profileId, EducationCreateDto dto);

    Education read(Long educationId);

    Education update(UserDetailsImpl userDetails, Long educationId, EducationCreateDto dto);

    void delete(UserDetailsImpl userDetails, Long educationId);
}