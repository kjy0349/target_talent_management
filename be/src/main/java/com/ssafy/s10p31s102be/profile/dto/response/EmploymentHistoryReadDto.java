package com.ssafy.s10p31s102be.profile.dto.response;

import com.ssafy.s10p31s102be.profile.infra.entity.EmploymentHistory;
import com.ssafy.s10p31s102be.profile.infra.entity.community.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentHistoryReadDto {
    private String type;
    private String step;
    private List<String> contents;
    private LocalDateTime datetime;

    public static EmploymentHistoryReadDto fromEntity(Meeting meeting) {
        List<String> contents = new ArrayList<>();
        contents.add(meeting.getParticipants());

        String meetingType = "";

        switch (meeting.getMeetingType()) {
            case CURRENT:
                meetingType = "현업";
                break;
            case HEAD_RECRUITER:
                meetingType = "채용부서장";
                break;
            case RECRUITER:
                meetingType = "채용담당자";
                break;
        }

        return EmploymentHistoryReadDto.builder()
            .type("면담")
            .step(meetingType)
            .contents(contents)
            .datetime(meeting.getCreatedAt())
            .build();
    }

    public static EmploymentHistoryReadDto fromEntity(Interview interview) {
        return EmploymentHistoryReadDto.builder()
            .type("면접")
            .step(interview.getInterviewDegree())
            .contents(interview.getInterviewResults().stream().map(result -> {
                String interviewResultString = result.getInterviewResultType().toString().replaceAll("_PLUS", "+");

                return result.getExecutiveName() + " ("
                        + interviewResultString + ")";
            }).toList())
            .datetime(interview.getCreatedAt())
            .build();
    }

    public static EmploymentHistoryReadDto fromEntity(EmploymentHistory employmentHistory) {

        String historyType = "";

        switch (employmentHistory.getType()) {
            case USAGE_REVIEW -> historyType = "활용도 검토";
            case APPROVE_E -> historyType = "APPROVE_E";
            case NEGOTIATION -> historyType = "처우 협의";
            case NEGOTIATION_DENIED -> historyType = "처우 결렬";
            case EMPLOY_ABANDON -> historyType = "입사 포기";
            case EMPLOY_WAITING -> historyType = "입사 대기";
            case EMPLOYED -> historyType = "입사";
        }

        return EmploymentHistoryReadDto.builder()
                .type(historyType)
                .step(employmentHistory.getStep())
                .contents(new ArrayList<>()) // TODO: 컨텐츠에 뭘 채워야 할까
                .datetime(employmentHistory.getCreatedAt())
                .build();
    }

//    public static EmploymentHistoryReadDto fromEntity(UsageReview usageReview) {
//        List<String> contents = new ArrayList<>();
//
//        String usageReviewResultString = "";
//
//        switch (usageReview.getReviewResult()) {
//            case SUITABLE:
//                usageReviewResultString = "적합";
//                break;
//            case NOT_SUITABLE:
//                usageReviewResultString = "부적합";
//                break;
//            case NORMAL:
//                usageReviewResultString = "보통";
//                break;
//        }
//
//        contents.add(
//            usageReview.getExecutive().getDepartment() + " "
//            + usageReview.getExecutive().getName() + " "
//            + usageReview.getExecutive().getJobRank()
//        );
//
//        return EmploymentHistoryReadDto.builder()
//            .type(usageReviewResultString)
//            .contents(contents)
//            .datetime(usageReview.getReviewedAt())
//            .build();
//    }
//
//    public static EmploymentHistoryReadDto fromEntity(EmploymentApprove employmentApprove) {
//        List<String> contents = new ArrayList<>();
//
//        String employmentApproveResultString = "";
//
//        switch (employmentApprove.getResultType()) {
//            case COMPLETED:
//                employmentApproveResultString = "완료";
//                break;
//            case UNCOMPLETED:
//                employmentApproveResultString = "미완료";
//                break;
//        }
//
//        contents.add(employmentApproveResultString);
//
//        return EmploymentHistoryReadDto.builder()
//            .type("승인")
//            .step(employmentApprove.getStep() + "차")
//            .contents(contents)
//            .datetime(employmentApprove.getApprovedAt())
//            .build();
//    }
//
//    public static EmploymentHistoryReadDto fromEntity(TreatNegotiation treatNegotiation) {
//        List<String> contents = new ArrayList<>();
//
//        String treatNegotiationResultString = "";
//
//        switch (treatNegotiation.getStep()) {
//            case IN_DISCUSSION:
//                treatNegotiationResultString = "협의 중";
//                break;
//            case COMPLETED:
//                treatNegotiationResultString = "완료";
//                break;
//        }
//
//        contents.add(
//            treatNegotiation.getJobRank().getDescription() + " 직급 / "
//            + treatNegotiation.getEstimatedJoinDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " 입사"
//        );
//
//        return EmploymentHistoryReadDto.builder()
//            .type("처우")
//            .step(treatNegotiationResultString)
//            .contents(contents)
//            .datetime(treatNegotiation.getCreatedAt())
//            .build();
//    }
}
