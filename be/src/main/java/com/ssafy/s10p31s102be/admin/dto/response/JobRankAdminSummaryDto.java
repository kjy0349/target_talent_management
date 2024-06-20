package com.ssafy.s10p31s102be.admin.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRankAdminSummaryDto {
    private Integer id;

    private String description;

    public static JobRankAdminSummaryDto fromEntity(JobRank jobRank ){
        return JobRankAdminSummaryDto.builder()
                .id(jobRank.getId())
                .description(jobRank.getDescription())
                .build();
    }
}
