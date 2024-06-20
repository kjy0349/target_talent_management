package com.ssafy.s10p31s102be.profile.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.profile.dto.request.EmploymentHistoryCreateDto;
import com.ssafy.s10p31s102be.profile.infra.entity.EmploymentHistory;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;

import java.util.List;
import java.util.Map;

public interface EmploymentHistoryService {
    EmploymentHistory create(UserDetailsImpl userDetails, Integer profileId, EmploymentHistoryCreateDto dto);

    List<EmploymentHistory> readEmploymentHistories(UserDetailsImpl userDetails, Integer profileId);

    EmploymentHistory update(UserDetailsImpl userDetails, Long employmentHistoryId, EmploymentHistoryCreateDto dto);

    EmploymentHistory updateFavorite(UserDetailsImpl userDetails, Long employmentHistoryId, Boolean isFavorite);
    void delete(UserDetailsImpl userDetails, Long employmentHistoryId);
}
