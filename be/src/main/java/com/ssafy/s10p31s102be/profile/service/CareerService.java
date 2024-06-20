package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.CareerCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;

public interface CareerService {
    Career create(UserDetailsImpl userDetails, Integer profileId, CareerCreateDto dto);

    Career read(Long careerId);

    Career update(UserDetailsImpl userDetails, Long careerId, CareerCreateDto dto);

    void delete(UserDetailsImpl userDetails, Long careerId);
}