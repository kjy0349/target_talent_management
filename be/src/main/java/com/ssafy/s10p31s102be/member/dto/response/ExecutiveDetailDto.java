package com.ssafy.s10p31s102be.member.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutiveDetailDto {
    private String name;
    private String department;
    private String jobRank;

    public static ExecutiveDetailDto fromEntity(Executive executive) {
        return ExecutiveDetailDto.builder()
                .name(executive.getName())
                .department(executive.getDepartment())
                .jobRank(executive.getJobRank())
                .build();
    }
}
