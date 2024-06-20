package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.UsagePlanCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;

import java.util.List;

public interface UsagePlanService {

    UsagePlan create(UserDetailsImpl userDetails, Integer profileId, UsagePlanCreateDto dto);

    List<UsagePlan> readUsagePlans(UserDetailsImpl userDetails, Integer profileId);

    UsagePlan update(UserDetailsImpl userDetails, Integer usagePlanId, UsagePlanCreateDto dto);

    UsagePlan updateFavorite(UserDetailsImpl userDetails, Integer usagePlanId, Boolean isFavorite);

    void delete(UserDetailsImpl userDetails, Integer usagePlanId);
}