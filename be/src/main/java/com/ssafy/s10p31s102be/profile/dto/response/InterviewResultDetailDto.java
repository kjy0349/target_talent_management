package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.community.InterviewResult;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewResultType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewResultDetailDto {
    private Integer id;
    private InterviewResultType interviewResultType;
    private String executiveName;

    public static InterviewResultDetailDto fromEntity(InterviewResult interviewResult) {
        return InterviewResultDetailDto.builder()
                .id(interviewResult.getId())
                .interviewResultType(interviewResult.getInterviewResultType())
                .executiveName(interviewResult.getExecutiveName())
                .build();
    }
}