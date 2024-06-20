package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileMemoCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.ProfileMemo;

import java.util.List;

public interface ProfileMemoService {
    ProfileMemo create(UserDetailsImpl userDetails, Integer profileId, ProfileMemoCreateDto dto);

    ProfileMemo update(UserDetailsImpl userDetails, Long profileMemoId, ProfileMemoCreateDto dto);

    List<ProfileMemo> readProfileMemos(UserDetailsImpl userDetails, Integer profileId);

    void delete(UserDetailsImpl userDetails, Long profileMemoId);
}