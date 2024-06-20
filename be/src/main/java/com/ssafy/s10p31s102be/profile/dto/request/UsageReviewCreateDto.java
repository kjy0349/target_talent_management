package com.ssafy.s10p31s102be.profile.dto.request;

import com.ssafy.s10p31s102be.profile.infra.enums.ReviewResultType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsageReviewCreateDto {

    @NotNull(message = "부서가 존재하지 않습니다.")
    private Integer departmentId;
    
    @NotNull(message = "직급4이 존재하지 않습니다.")
    private Integer executiveId;

    private String description;

    @NotNull(message = "검토 결과가 존재하지 않습니다.")
    private ReviewResultType result;

    @NotNull(message = "검토 일자가 존재하지 않습니다.")
    private LocalDateTime reviewedAt;
}
