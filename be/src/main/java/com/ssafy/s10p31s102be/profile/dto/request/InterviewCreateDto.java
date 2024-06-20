package com.ssafy.s10p31s102be.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewType;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewCreateDto {
    private String interviewDegree;
    private LocalDateTime meetDate;
    private InterviewType interviewType;
    private String place;
    @Size(max = 500)
    private String description;
    private List<InterviewResultCreateDto> interviewResults;
}
