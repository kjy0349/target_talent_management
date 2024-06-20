package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.SpecializationCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Specialization;

import java.util.List;

public interface SpecializationService {
    Specialization create(UserDetailsImpl userDetails, Integer profileId, SpecializationCreateDto dto);
    List<Specialization> readSpecializations(UserDetailsImpl userDetails, Integer profileId);
    Specialization update(UserDetailsImpl userDetails, Long specializationId, SpecializationCreateDto dto);
    Specialization updateFavorite(UserDetailsImpl userDetails, Long specializationId, Boolean isFavorite);
    void delete(UserDetailsImpl userDetails, Long specializationId);
}
