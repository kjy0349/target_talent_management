package com.ssafy.s10p31s102be.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsagePlanCreateDto {

    private String mainDescription;

    private String detailDescription;

    private LocalDateTime targetEmployDate;

    private String jobRank;

    private String targetDepartmentName;
}