package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentHistoryCreateDto {
    private EmploymentHistoryType type;
    private String step;
    private String result;
    private String targetDepartmentName;
    private String targetJobRankName;
    private String executiveName;
    private LocalDateTime targetEmployDate;
    private String description;
}
