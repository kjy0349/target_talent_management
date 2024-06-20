package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.admin.dto.response.JobRankAdminSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileColumnListDto {
    private List<ProfileColumnDto> columns;
    private List<JobRankAdminSummaryDto> jobRanks;
    private Map<String, List<String>> disabledColumns;
}
