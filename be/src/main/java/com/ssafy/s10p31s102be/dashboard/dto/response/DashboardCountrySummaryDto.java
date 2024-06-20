package com.ssafy.s10p31s102be.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCountrySummaryDto {
    private Integer visitors;
    private Integer change;
}
