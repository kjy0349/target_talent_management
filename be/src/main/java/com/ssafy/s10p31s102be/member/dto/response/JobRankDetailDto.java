package com.ssafy.s10p31s102be.member.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.JobRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRankDetailDto {
    private Integer id;
    private String description;

    public static JobRankDetailDto fromEntity(JobRank jobRank) {
        return JobRankDetailDto.builder()
                .id(jobRank.getId())
                .description(jobRank.getDescription())
                .build();
    }
}
