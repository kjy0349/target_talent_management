package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.entity.education.Education;
import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationPreviewDto {
    private String schoolName;
    private String major;
    private Degree degree;
    private LocalDateTime enteredAt;
    private LocalDateTime graduatedAt;

    public static EducationPreviewDto fromEntity(Education education) {
        return EducationPreviewDto.builder()
                .schoolName(education.getSchool().getSchoolName())
                .degree(education.getDegree())
                .major(education.getMajor())
                .enteredAt(education.getEnteredAt())
                .graduatedAt(education.getGraduatedAt())
                .build();
    }
}
