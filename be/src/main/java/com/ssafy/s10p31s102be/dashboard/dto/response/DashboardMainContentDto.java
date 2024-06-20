package com.ssafy.s10p31s102be.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMainContentDto {
    private Integer totalPoolSize;
    private Integer ePollSize;
    private Integer developerPoolSize;
    private Integer pPollSizePoolSize;
    private Integer networkingPoolSize;
    private Integer techmapPoolSize;
}
