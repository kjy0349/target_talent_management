package com.ssafy.s10p31s102be.profile.infra.repository;

import com.ssafy.s10p31s102be.common.infra.enums.DomainType;
import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardCountrySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardDepartmentSearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardMonthlySearchConditionDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileExternalSearchConditionDto;
import com.ssafy.s10p31s102be.profile.dto.request.ProfileFilterSearchDto;
import com.ssafy.s10p31s102be.profile.dto.response.ProfileFilterSearchedDto;
import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileSearchRepository {
    public ProfileFilterSearchedDto getAllProfilePreviewByFilter(UserDetailsImpl userDetails, Pageable pageable, ProfileFilterSearchDto profileFilterSearchDto);

    Page<Profile> getAllProfileByDomainType(Integer domainId, DomainType domainType, ProfileExternalSearchConditionDto searchConditionDto, Pageable pageable);
    List<Profile> getAllProfileByDashboardDepartmentDto(DashboardDepartmentSearchConditionDto dashboardDepartmentSearchConditionDto );

    List<Profile> getAllProfileByDashboardMonthlySearchCondition(DashboardMonthlySearchConditionDto searchConditionDto);

    List<Profile> getAllProfileByDashboardCountryDto(DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto);
}
