package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutiveResponseDto {
    private Integer id;
    private String name;
    private String department;
    private String jobRank;
    private String email;

    public static ExecutiveResponseDto fromEntity(Executive executive) {
        return ExecutiveResponseDto.builder()
                .id(executive.getId())
                .name(executive.getName())
                .department(executive.getDepartment())
                .jobRank(executive.getJobRank())
                .email(executive.getEmail())
                .build();
    }
}
