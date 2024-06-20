package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.community.UsagePlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsagePlanReadDto {

    private Integer id;

    private String mainDescription;

    private String detailDescription;

    private LocalDateTime targetEmployDate;

    private String jobRank;

    private String targetDepartmentName;

    private String memberDepartment;

    private String memberName;

    private Boolean isFavorite;

    private LocalDateTime createdAt;

    public static UsagePlanReadDto fromEntity(UsagePlan usagePlan) {
        return UsagePlanReadDto.builder()
                .id(usagePlan.getId())
                .mainDescription(usagePlan.getMainDescription())
                .detailDescription(usagePlan.getDetailDescription())
                .targetEmployDate(usagePlan.getTargetEmployDate())
                .jobRank(usagePlan.getJobRank())
                .targetDepartmentName(usagePlan.getTargetDepartmentName())
                .memberDepartment(usagePlan.getMemberDepartment())
                .memberName(usagePlan.getMemberName())
                .isFavorite(usagePlan.getIsFavorite())
                .createdAt(usagePlan.getCreatedAt())
                .build();
    }
}