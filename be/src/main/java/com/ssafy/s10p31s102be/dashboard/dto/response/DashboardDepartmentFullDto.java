package com.ssafy.s10p31s102be.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDepartmentFullDto {
    private Integer visitors;
    private Integer percentage;
    private List<String> labels;
    private List< DashboardDepartmentSeriesDto > series;

}
