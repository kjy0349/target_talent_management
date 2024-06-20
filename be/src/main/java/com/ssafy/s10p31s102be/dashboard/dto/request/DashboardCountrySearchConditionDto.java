package com.ssafy.s10p31s102be.dashboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCountrySearchConditionDto {
    private Integer viewYear;
    private Integer viewMonth;
}