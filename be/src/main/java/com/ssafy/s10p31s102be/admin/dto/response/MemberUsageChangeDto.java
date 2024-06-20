package com.ssafy.s10p31s102be.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUsageChangeDto {
    private Integer countChangeYesterday;
    private Integer visitorChangeYesterday;
    private Integer countChangeLastWeek;
    private Integer visitorChangeLastWeek;
    private Integer countChangeLastMonth;
    private Integer visitorChangeLastMonth;
    private Integer totalCount;
    private Integer totalVisitorCount;
    private Long totalMembers;
}
