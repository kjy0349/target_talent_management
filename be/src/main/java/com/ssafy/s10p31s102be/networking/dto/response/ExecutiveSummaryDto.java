package com.ssafy.s10p31s102be.networking.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutiveSummaryDto {
    private Integer executiveId;

    private String name;

    private String department;

    private String jobRank;

    private String email;

    public static ExecutiveSummaryDto fromEntity(Executive executive){
        return ExecutiveSummaryDto.builder()
                .executiveId(executive.getId())
                .name(executive.getName())
                .department(executive.getDepartment())
                .jobRank(executive.getJobRank())
                .email(executive.getEmail())
                .build();
    }
}
