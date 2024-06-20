package com.ssafy.s10p31s102be.dashboard.service;

import com.ssafy.s10p31s102be.common.util.UserDetailsImpl;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardCountrySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardDepartmentSearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.request.DashboardMonthlySearchConditionDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardCountrySummaryDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardDepartmentFullDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardMainContentFullDto;
import com.ssafy.s10p31s102be.dashboard.dto.response.DashboardMonthlyFullDto;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    DashboardMainContentFullDto getDashboardMainContent(UserDetailsImpl userDetails);

    Long getSumProfilePool(UserDetailsImpl userDetails);

    DashboardDepartmentFullDto getDashboardDepartment(UserDetailsImpl userDetails, DashboardDepartmentSearchConditionDto searchConditionDto);

    DashboardMonthlyFullDto getDashboardMonthly(UserDetailsImpl userDetails, DashboardMonthlySearchConditionDto searchConditionDto);

    List<String> getDashboardProfileSkillMainCategory(UserDetailsImpl userDetails);

    Map<String, DashboardCountrySummaryDto> getDashboardCountry(UserDetailsImpl userDetails, DashboardCountrySearchConditionDto dashboardCountrySearchConditionDto);

    Long getNetworkingValue();
}
