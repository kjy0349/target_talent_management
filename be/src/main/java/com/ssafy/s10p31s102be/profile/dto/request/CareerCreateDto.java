package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.EmployType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerCreateDto {

    @NotNull(message = "회사명이 존재하지 않습니다.")
    private String companyName;

    @NotNull(message = "국가가 존재하지 않습니다.")
    private String companyCountryName;

    private String companyRegion;

    @NotNull(message = "직급이 존재하지 않습니다.")
    private String jobRank;

    private String level;

    @NotNull(message = "매니저 여부가 존재하지 않습니다.")
    private Boolean isManager;

    @NotNull(message = "고용형태가 존재하지 않습니다.")
    private EmployType employType;

    @NotNull(message = "시작년월이 존재하지 않습니다.")
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @NotNull(message = "현재 근무 여부가 존재하지 않습니다.")
    private Boolean isCurrent;

    @NotNull(message = "근무 부서가 존재하지 않습니다.")
    private String dept;

    @NotNull(message = "담당 업무가 존재하지 않습니다.")
    private String role;

    private String description;

    private List<String> keywords;
}