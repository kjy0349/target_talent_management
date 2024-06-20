package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.Degree;
import com.ssafy.s10p31s102be.profile.infra.enums.GraduateStatus;
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
public class EducationCreateDto {

    @NotNull(message = "학위가 존재하지 않습니다.")
    private Degree degree;

    @NotNull(message = "학교 국가가 존재하지 않습니다.")
    private String schoolCountry;

    @NotNull(message = "학교 이름이 존재하지 않습니다.")
    private String schoolName;

    private String major;

    private LocalDateTime enteredAt;

    private LocalDateTime graduatedAt;

    private String labName;

    private GraduateStatus graduateStatus;

    // Nullable 하지만, labName이 입력되면 Not nullable
    private String labResearchType;

    private String labResearchDescription;

    private String labResearchResult;

    private String labProfessor;

    private List<String> keywords;
}
