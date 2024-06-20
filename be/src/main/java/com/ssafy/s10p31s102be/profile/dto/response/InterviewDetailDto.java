package com.ssafy.s10p31s102be.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.s10p31s102be.profile.infra.entity.community.Interview;
import com.ssafy.s10p31s102be.profile.infra.enums.EmploymentHistoryType;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewDetailDto {
    private Integer id;
    private EmploymentHistoryType type;
    private String step;
    private LocalDateTime meetDate;
    private InterviewType interviewType;
    private String place;
    private String description;
    private String memberDepartment;
    private String memberName;
    private LocalDateTime createdAt;
    private Boolean isFavorite;
    private List<InterviewResultDetailDto> interviewResults;

    public static InterviewDetailDto fromEntity(Interview interview) {
        return InterviewDetailDto.builder()
                .id(interview.getId())
                .type(EmploymentHistoryType.INTERVIEW)
                .step(interview.getInterviewDegree())
                .meetDate(interview.getMeetDate())
                .interviewType(interview.getInterviewType())
                .place(interview.getPlace())
                .description(interview.getDescription())
                .memberDepartment(interview.getMemberDepartment())
                .memberName(interview.getMemberName())
                .createdAt(interview.getCreatedAt())
                .isFavorite(interview.getIsFavorite())
                .interviewResults(interview.getInterviewResults().stream().map(InterviewResultDetailDto::fromEntity).toList())
                .build();
    }
}
