package com.ssafy.s10p31s102be.profile.infra.entity.community;

import com.ssafy.s10p31s102be.member.infra.entity.Executive;
import com.ssafy.s10p31s102be.profile.infra.enums.InterviewResultType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class InterviewResult {
    @Id
    @GeneratedValue
    private Integer id;

    @Builder
    public InterviewResult(Integer id, InterviewResultType interviewResultType, Interview interview, String executiveName) {
        this.interviewResultType = interviewResultType;
        this.interview = interview;
        this.executiveName = executiveName;
    }

    @Enumerated(EnumType.STRING)
    private InterviewResultType interviewResultType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    private String executiveName;
}
