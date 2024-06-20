package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.entity.career.Career;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPreviewDto {
    private String companyName;
    private String role;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long careerPeriodMonth;

    public static CareerPreviewDto fromEntity(Career career) {
        return CareerPreviewDto.builder()
                .companyName(career.getCompany().getName())
                .role(career.getRole())
                .startedAt(career.getStartedAt())
                .endedAt(career.getEndedAt())
                .careerPeriodMonth(career.getCareerPeriodMonth())
                .build();
    }
}
