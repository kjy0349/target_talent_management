package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.EmploymentHistory;
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
public class EmploymentHistoryDetailDto {
    private Long id;
    private EmploymentHistoryType type;
    private String step;
    private String result;
    private String targetDepartmentName;
    private String targetJobRankName;
    private String executiveName;
    private LocalDateTime targetEmployDate;
    private String description;
    private Boolean isFavorite;
    private String memberDepartment;
    private String memberName;
    private LocalDateTime createdAt;

    public static EmploymentHistoryDetailDto fromEntity(EmploymentHistory employmentHistory) {
        return EmploymentHistoryDetailDto.builder()
                .id(employmentHistory.getId())
                .type(employmentHistory.getType())
                .step(employmentHistory.getStep())
                .result(employmentHistory.getResult())
                .targetDepartmentName(employmentHistory.getTargetDepartmentName())
                .targetJobRankName(employmentHistory.getTargetJobRankName())
                .executiveName(employmentHistory.getExecutiveName())
                .targetEmployDate(employmentHistory.getTargetEmployDate())
                .description(employmentHistory.getDescription())
                .isFavorite(employmentHistory.getIsFavorite())
                .memberDepartment(employmentHistory.getMemberDepartment())
                .memberName(employmentHistory.getMemberName())
                .createdAt(employmentHistory.getCreatedAt())
                .build();
    }
}